package com.example.fivebetserio.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fivebetserio.R;
import com.example.fivebetserio.adapter.LeaguesRecyclerAdapter;
import com.example.fivebetserio.database.LeagueDao;
import com.example.fivebetserio.database.LeaguesRoomDatabase;
import com.example.fivebetserio.database.MatchDao;
import com.example.fivebetserio.model.League;
import com.example.fivebetserio.model.Match;
import com.example.fivebetserio.repository.ILeagueRepository;
import com.example.fivebetserio.repository.LeagueAPIRepository;
import com.example.fivebetserio.repository.LeagueMockRepository;
import com.example.fivebetserio.service.ServiceLocator;
import com.example.fivebetserio.util.Constants;
import com.example.fivebetserio.util.JSONParserUtils;
import com.example.fivebetserio.util.ResponseCallback;
import com.example.fivebetserio.util.SharedPreferencesUtils;
import com.example.fivebetserio.util.NetworkUtil;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainPageActivity extends AppCompatActivity implements ResponseCallback {

    public static final String TAG = MainPageActivity.class.getName();
    private static final int viewedElements = 2;
    private RecyclerView recyclerView;
    private LeaguesRecyclerAdapter leagueRecyclerAdapter;
    private Executor executor;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private ILeagueRepository leagueRepository;
    private List<League> leagueList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page); // Imposta il layout

        if (getResources().getBoolean(R.bool.debug_mode)) {
            leagueRepository = new LeagueMockRepository(getApplication(), this);
        } else {
            leagueRepository = new LeagueAPIRepository(getApplication(), this);
        }

        recyclerView = findViewById(R.id.recyclerViewLeagues);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        leagueList = new ArrayList<>();
        for (int i = 0; i < viewedElements; i++) leagueList.add(League.getSampleLeague());

        leagueRecyclerAdapter = new LeaguesRecyclerAdapter(R.layout.item_league, leagueList, this, getApplication());
        recyclerView.setAdapter(leagueRecyclerAdapter);

        String lastUpdate = "0";
        sharedPreferencesUtils = new SharedPreferencesUtils(this);
        if (sharedPreferencesUtils.readStringData(
                Constants.SHARED_PREFERENCES_FILENAME, Constants.SHARED_PREFERENECES_LAST_UPDATE) != null) {
            lastUpdate = sharedPreferencesUtils.readStringData(
                    Constants.SHARED_PREFERENCES_FILENAME, Constants.SHARED_PREFERENECES_LAST_UPDATE);
        }

        leagueRepository.fetchLeagues(Constants.TOP_HEADLINES_PAGE_SIZE_VALUE, Long.parseLong(lastUpdate));

        /*if (!NetworkUtil.isInternetAvailable(this)) {
            noInternetView.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public void onSuccess(List<League> leagueList, long lastUpdate) {
        if (leagueList == null) {
            Log.d("DEBUG", "League list is null.");
            return;
        }

        Log.d("DEBUG", "Leagues received from API: " + leagueList.size());

        for (League league : leagueList) {
            Log.d("DEBUG", "League: " + league.getKey());

        }

        this.leagueList.clear();
        this.leagueList.addAll(leagueList);

        runOnUiThread(() -> {
            leagueRecyclerAdapter.notifyDataSetChanged();
            recyclerView.setVisibility(View.VISIBLE);
        });

        updateMatchesForLeagues(leagueList);
    }


    private void updateMatchesForLeagues(List<League> leagues) {
        Executor executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            // Ottieni il database
            MatchDao matchDAO = ServiceLocator.getInstance().getLeaguesDB(getApplication()).matchDao();

            for (League league : leagues) {
                // Recupera i match dalla API o dal database
                List<Match> matches = matchDAO.getMatchesByLeague(league.getUid());

                // Aggiorna il `leagueUid` di ogni match
                for (Match match : matches) {
                    match.setLeagueUid(league.getUid());
                }

                // Salva i match aggiornati nel database
                matchDAO.insertMatchesList(matches);
            }

            // Aggiorna la UI dopo il caricamento dei match
            runOnUiThread(() -> leagueRecyclerAdapter.updateMatches());
        });
    }




    @Override
    public void onFailure(String errorMessage) {
        Snackbar.make(findViewById(android.R.id.content), // Rimosso requireActivity()
                errorMessage, Snackbar.LENGTH_LONG).show();
    }

}