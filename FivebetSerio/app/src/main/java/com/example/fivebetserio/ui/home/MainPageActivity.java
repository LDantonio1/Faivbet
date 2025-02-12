package com.example.fivebetserio.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fivebetserio.R;
import com.example.fivebetserio.adapter.LeaguesRecyclerAdapter;
import com.example.fivebetserio.model.League;
import com.example.fivebetserio.model.Match;
import com.example.fivebetserio.model.Result;
import com.example.fivebetserio.repository.Repository;
import com.example.fivebetserio.ui.home.viewModel.league.LeagueViewModel;
import com.example.fivebetserio.ui.home.viewModel.league.LeagueViewModelFactory;
import com.example.fivebetserio.ui.home.viewModel.match.MatchViewModel;
import com.example.fivebetserio.ui.home.viewModel.match.MatchViewModelFactory;
import com.example.fivebetserio.util.Constants;
import com.example.fivebetserio.util.ServiceLocator;
import com.example.fivebetserio.util.SharedPreferencesUtils;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;

public class MainPageActivity extends AppCompatActivity {

    private static final int initialShimmerElements = 2;
    private RecyclerView recyclerView;
    private LeaguesRecyclerAdapter leagueRecyclerAdapter;
    private List<League> leagueList;
    private List<Match> matchList = new ArrayList<>(); // Inizializza qui
    private LeagueViewModel leagueViewModel;
    private MatchViewModel matchViewModel;
    private SharedPreferencesUtils sharedPreferencesUtils;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        sharedPreferencesUtils = new SharedPreferencesUtils(getApplication());

        // Inizializzazione del repository e dei ViewModel
        Repository repository = ServiceLocator.getInstance().getRepository(
                getApplication(),
                getResources().getBoolean(R.bool.debug_mode)
        );

        leagueViewModel = new ViewModelProvider(this, new LeagueViewModelFactory(repository))
                .get(LeagueViewModel.class);

        matchViewModel = new ViewModelProvider(this, new MatchViewModelFactory(repository))
                .get(MatchViewModel.class);

        // Inizializzazione UI
        recyclerView = findViewById(R.id.recyclerViewLeagues);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Dati iniziali (shimmer effect)
        leagueList = new ArrayList<>();
        for (int i = 0; i < initialShimmerElements; i++) {
            leagueList.add(League.getSampleLeague());
        }

        leagueRecyclerAdapter = new LeaguesRecyclerAdapter(R.layout.item_league, leagueList, matchList, this, leagueViewModel);
        recyclerView.setAdapter(leagueRecyclerAdapter);

        // Controllo connessione internet
        String lastUpdate = sharedPreferencesUtils.readStringData(
                Constants.SHARED_PREFERENCES_FILENAME,
                Constants.SHARED_PREFERENCES_LAST_UPDATE
        );
        if (lastUpdate == null) {
            lastUpdate = "0";
        }

        final long lastUpdataFinal = Long.parseLong(lastUpdate);

        // Osserva i dati delle leghe
        leagueViewModel.getLeagues(Long.parseLong(lastUpdate)).observe(this, result -> {
            if (result.isSuccess()) {
                leagueList.clear();
                leagueList.addAll(((Result.LeagueSuccess) result).getData().getLeagues());
                leagueRecyclerAdapter.setLeaguesList(leagueList);
                recyclerView.setVisibility(View.VISIBLE);

                Log.d("MainPageActivity", "Leghe caricate: " + leagueList.size()); // Aggiungi Log

                // Osserva i match SOLO dopo aver caricato le leghe
                observeMatchesForLeagues(leagueList, lastUpdataFinal);

            } else {
                Snackbar.make(findViewById(android.R.id.content),
                        getString(R.string.error_retrieving_leagues),
                        Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void observeMatchesForLeagues(List<League> leagues, long lastUpdate) {
        matchViewModel.getMatches(leagues, lastUpdate).observe(this, result -> {
            if (result.isSuccess()) {
                List<Match> matches = ((Result.MatchSuccess) result).getData().getMatches();
                synchronized (matchList) { //Sincronizza se necessario
                    matchList.clear();
                    matchList.addAll(matches);
                }

                Log.d("MainPageActivity", "Match caricati: " + matches.size()); // Aggiungi Log
                for (Match match : matches) {
                    Log.d("MainPageActivity", "Match: " + match.getHome_team() + " vs " + match.getAway_team() + ", League UID: " + match.getLeagueUid()); // Aggiungi Log
                }

                leagueRecyclerAdapter.setAllMatchesList(matchList); // Aggiorna la lista completa

                //Chiama notifyDataSetChanged solo dopo aver impostato i match
                leagueRecyclerAdapter.notifyDataSetChanged();
            } else {
                Snackbar.make(findViewById(android.R.id.content),
                        getString(R.string.error_retrieving_matches),
                        Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}