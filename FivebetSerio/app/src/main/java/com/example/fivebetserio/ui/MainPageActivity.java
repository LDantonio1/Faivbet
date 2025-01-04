package com.example.fivebetserio.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fivebetserio.R;
import com.example.fivebetserio.adapter.LeaguesRecyclerAdapter;
import com.example.fivebetserio.database.LeaguesRoomDatabase;
import com.example.fivebetserio.model.League;
import com.example.fivebetserio.model.LeaguesAPIResponse;
import com.example.fivebetserio.model.Match;
import com.example.fivebetserio.model.MatchesAPIResponse;
import com.example.fivebetserio.util.Constants;
import com.example.fivebetserio.util.JSONParserUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainPageActivity extends AppCompatActivity {

    public static final String TAG = MainPageActivity.class.getName();

    private RecyclerView recyclerView;
    private LeaguesRecyclerAdapter adapter;
    private LeaguesRoomDatabase db;
    private Executor executor;
    private JSONParserUtils jsonParserUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        // Inizializzazione RecyclerView
        recyclerView = findViewById(R.id.recyclerViewLeagues);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // Inizializzazione strumenti
        db = LeaguesRoomDatabase.getInstance(this);
        executor = Executors.newSingleThreadExecutor();
        jsonParserUtils = new JSONParserUtils(getAssets());

        loadDataFromJsonAndDatabase();
        observeLeagues();
    }

    private void loadDataFromJsonAndDatabase() {
        executor.execute(() -> {
            try {
                LeaguesAPIResponse leaguesAPIResponse = jsonParserUtils.parseLeaguesJSONFileWithGSon(Constants.LEAGUES_FILE);
                for (League league : leaguesAPIResponse.getLeagues()) {
                    db.leagueDao().insertLeague(league);
                    MatchesAPIResponse matchesAPIResponse = jsonParserUtils.parseMatchesJSONFileWithGSon(Constants.MATCHES_FILE);
                    for (Match match : matchesAPIResponse.getMatches()) {
                        match.setLeagueKey(league.getKey());
                        db.matchDao().insertMatch(match);
                    }
                }
            } catch (IOException e) {
                Log.e(TAG, "Errore durante il caricamento dei dati JSON: " + e.getMessage());
            }
        });
    }

    private void observeLeagues() {
        db.leagueDao().getAllLeagues().observe(this, new Observer<List<League>>() {
            @Override
            public void onChanged(List<League> leagues) {
                if (leagues != null && !leagues.isEmpty()) {
                    if (adapter == null) {
                        adapter = new LeaguesRecyclerAdapter(R.layout.item_league, leagues, MainPageActivity.this);
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.setLeaguesList(leagues);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e(TAG, "La lista delle leghe è vuota o null");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart chiamato");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume chiamato");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause chiamato");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop chiamato");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.leagueDao().getAllLeagues().removeObservers(this);
        }
        Log.d(TAG, "onDestroy chiamato");
    }
}
