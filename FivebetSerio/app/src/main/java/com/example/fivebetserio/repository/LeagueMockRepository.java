package com.example.fivebetserio.repository;

import android.app.Application;
import android.util.Log;

import com.example.fivebetserio.R;
import com.example.fivebetserio.database.LeagueDao;
import com.example.fivebetserio.database.LeaguesRoomDatabase;
import com.example.fivebetserio.database.MatchDao;
import com.example.fivebetserio.model.League;
import com.example.fivebetserio.model.LeaguesAPIResponse;
import com.example.fivebetserio.model.Match;
import com.example.fivebetserio.service.ServiceLocator;
import com.example.fivebetserio.util.JSONParserUtils;
import com.example.fivebetserio.util.Constants;
import com.example.fivebetserio.util.ResponseCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LeagueMockRepository implements ILeagueRepository {
    private final Application application;
    private final ResponseCallback responseCallback;
    private final LeagueDao leagueDao;
    private final MatchDao matchDao;

    private static final String TAG = "LeagueMockRepository";

    public LeagueMockRepository(Application application, ResponseCallback responseCallback) {
        this.application = application;
        this.responseCallback = responseCallback;
        this.leagueDao = ServiceLocator.getInstance().getLeaguesDB(application).leagueDao();
        this.matchDao = ServiceLocator.getInstance().getLeaguesDB(application).matchDao();
    }

    @Override
    public void fetchLeagues(int page, long lastUpdate) {
        JSONParserUtils jsonParserUtils = new JSONParserUtils(application.getAssets());

        try {
            LeaguesAPIResponse leagueApiResponse = jsonParserUtils.parseLeaguesJSONFileWithGSon(Constants.LEAGUES_FILE);
            if (leagueApiResponse != null) {
                saveDataInDatabase(leagueApiResponse.getLeagues());
            } else {
                responseCallback.onFailure(application.getString(R.string.error_retrieving_leagues));
            }
        } catch (IOException e) {
            responseCallback.onFailure(application.getString(R.string.error_retrieving_leagues));
            e.printStackTrace();
        }
    }

    /**
     * Metodo aggiornato per leggere i match da `match.json` invece che dall'API.
     */
    private void fetchMatches(List<League> leagueList) {
        JSONParserUtils jsonParserUtils = new JSONParserUtils(application.getAssets());

        try {
            List<Match> matchList = jsonParserUtils.parseMatchesJSONFileWithGSon(Constants.MATCHES_FILE).getMatches();

            if (matchList != null) {
                List<Match> duplicatedMatches = new ArrayList<>();

                for (League league : leagueList) {
                    for (Match match : matchList) {
                        Match duplicatedMatch = new Match(match); // Copia il match
                        duplicatedMatch.setLeagueUid(league.getUid());
                        duplicatedMatch.setSport_title(league.getTitle());
                        duplicatedMatch.setSport_key(league.getKey());
                        // Crea un ID unico per ogni match associato a una lega
                        String newMatchId = match.getId() + "_" + league.getUid();
                        duplicatedMatch.setId(newMatchId);
                        duplicatedMatches.add(duplicatedMatch);
                    }
                }

                saveMatchesInDatabase(duplicatedMatches);
            } else {
                Log.e(TAG, "Errore nel parsing dei match dal file JSON.");
            }
        } catch (IOException e) {
            Log.e(TAG, "Errore nel caricamento del file match.json", e);
        }
    }

    /**
     * Salva i match nel database.
     */
    private void saveMatchesInDatabase(List<Match> matchList) {
        LeaguesRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Long> existingLeaguesUids = leagueDao.getAllLeagueUids();
            List<Match> validMatches = new ArrayList<>();

            for (Match match : matchList) {
                if (existingLeaguesUids.contains(match.getLeagueUid())) {
                    validMatches.add(match);
                } else {
                    Log.e(TAG, "Match scartato: " + match.getId() + " perché la League " + match.getLeagueUid() + " non esiste nel DB.");
                }
            }

            if (!validMatches.isEmpty()) {
                matchDao.insertMatchesList(validMatches);
                Log.d(TAG, "Match salvati nel database con successo.");
            } else {
                Log.e(TAG, "Nessun match valido da salvare.");
            }
        });
    }

    /**
     * Salva le leghe nel database e avvia il caricamento dei match.
     */
    private void saveDataInDatabase(List<League> newLeagues) {
        LeaguesRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<League> existingLeagues = leagueDao.getAllLeagues();

            for (League league : existingLeagues) {
                if (newLeagues.contains(league)) {
                    newLeagues.set(newLeagues.indexOf(league), league);
                }
            }

            List<Long> insertedLeaguesIds = leagueDao.insertLeaguesList(newLeagues);
            for (int i = 0; i < newLeagues.size(); i++) {
                newLeagues.get(i).setUid(insertedLeaguesIds.get(i));
            }

            Log.d(TAG, "Leagues salvate nel database, ora carico i match...");
            fetchMatches(newLeagues);

            responseCallback.onSuccess(newLeagues, System.currentTimeMillis());
        });
    }

    /**
     * Legge le leghe dal database locale.
     */
    private void readDataFromDatabase(long lastUpdate) {
        LeaguesRoomDatabase.databaseWriteExecutor.execute(() -> {
            responseCallback.onSuccess(leagueDao.getAllLeagues(), lastUpdate);
        });
    }

    @Override
    public void updateLeagues(League league) {
        // Da implementare se necessario
    }

    @Override
    public void getFavoriteLeagues() {
        // Da implementare se necessario
    }

    @Override
    public void deleteFavoriteLeagues() {
        // Da implementare se necessario
    }
}
