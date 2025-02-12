package com.example.fivebetserio.ui;

import static com.example.fivebetserio.util.Constants.FIREBASE_REALTIME_DATABASE;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fivebetserio.R;
import com.example.fivebetserio.adapter.LeaguesRecyclerAdapter;
import com.example.fivebetserio.database.MatchDao;
import com.example.fivebetserio.model.League;
import com.example.fivebetserio.model.Match;
import com.example.fivebetserio.repository.ILeagueRepository;
import com.example.fivebetserio.repository.LeagueAPIRepository;
import com.example.fivebetserio.repository.LeagueMockRepository;
import com.example.fivebetserio.service.ServiceLocator;
import com.example.fivebetserio.util.Constants;
import com.example.fivebetserio.util.ResponseCallback;
import com.example.fivebetserio.util.SharedPreferencesUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            // Aggiungi una nuova lega alla lista dell'utente dopo il login
            toggleFavoriteLeague(currentUser);
        }
    }


    // metodo di prova per vedere se viene aggiornata la lista al db
    public void toggleFavoriteLeague(FirebaseUser currentUser) {
        if (currentUser == null) return;

        FirebaseDatabase database = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE);
        DatabaseReference leaguesRef = database.getReference("users")
                .child(currentUser.getUid())
                .child("favoriteLeagues");

        // Recupera la lista attuale
        leaguesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                List<String> favoriteLeagues = new ArrayList<>();

                for (DataSnapshot ds : task.getResult().getChildren()) {
                    String league = ds.getValue(String.class);
                    favoriteLeagues.add(league);
                }

                // Se la lega è già presente, la rimuove. Altrimenti, la aggiunge
                if (favoriteLeagues.contains("serie A")) {
                    favoriteLeagues.remove("serie A");
                    Log.d("ToggleLeague", "Campionato rimosso: ");
                } else {
                    favoriteLeagues.add("serie A");
                    Log.d("ToggleLeague", "Campionato aggiunto: ");
                }

                // Aggiorna il database con la lista modificata
                leaguesRef.setValue(favoriteLeagues)
                        .addOnSuccessListener(aVoid -> Log.d("ToggleLeague", "Lista aggiornata con successo"))
                        .addOnFailureListener(e -> Log.e("ToggleLeague", "Errore nell'aggiornamento", e));
            } else {
                Log.e("ToggleLeague", "Errore nel recupero delle leghe", task.getException());
            }
        });
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



