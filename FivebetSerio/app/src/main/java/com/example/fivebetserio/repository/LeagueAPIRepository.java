package com.example.fivebetserio.repository;

import static com.example.fivebetserio.util.Constants.FRESH_TIMEOUT;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.fivebetserio.R;
import com.example.fivebetserio.database.LeagueDao;
import com.example.fivebetserio.database.LeaguesRoomDatabase;
import com.example.fivebetserio.database.MatchDao;
import com.example.fivebetserio.model.League;
import com.example.fivebetserio.model.LeaguesAPIResponse;
import com.example.fivebetserio.model.Match;
import com.example.fivebetserio.model.MatchesAPIResponse;
import com.example.fivebetserio.service.LeagueAPIService;
import com.example.fivebetserio.service.MatchAPIService;
import com.example.fivebetserio.service.ServiceLocator;
import com.example.fivebetserio.util.Constants;
import com.example.fivebetserio.util.ResponseCallback;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeagueAPIRepository implements ILeagueRepository {

    private static final String TAG = LeagueAPIRepository.class.getSimpleName();

    private final Application application;
    private final LeagueAPIService leagueAPIService;
    private final MatchAPIService matchAPIService;
    private final LeagueDao leagueDAO;
    private final MatchDao matchDAO;
    private final ResponseCallback responseCallback;

    public LeagueAPIRepository(Application application, ResponseCallback responseCallback) {
        this.application = application;
        this.leagueAPIService = ServiceLocator.getInstance().getLeagueAPIService();
        this.matchAPIService = ServiceLocator.getInstance().getMatchAPIService();
        this.leagueDAO = ServiceLocator.getInstance().getLeaguesDB(application).leagueDao();
        this.matchDAO = ServiceLocator.getInstance().getLeaguesDB(application).matchDao();
        this.responseCallback = responseCallback;
    }

    @Override
    public void fetchLeagues(int page, long lastUpdate) {

        long currentTime = System.currentTimeMillis();

        // It gets the news from the Web Service if the last download
        // of the news has been performed more than FRESH_TIMEOUT value ago
        if (currentTime - lastUpdate > FRESH_TIMEOUT) {
            Call<List<League>> leagueResponseCall = leagueAPIService.getLeagues(application.getString(R.string.api_key));

            leagueResponseCall.enqueue(new Callback<List<League>>() {
                @Override
                public void onResponse(@NonNull Call<List<League>> call,
                                       @NonNull Response<List<League>> response) {

                    if (response.body() != null && response.isSuccessful()) {
                        List<League> leagueList = response.body();
                        LeaguesAPIResponse leaguesAPIResponse = new LeaguesAPIResponse(leagueList);
                        //leagueList.removeIf(league -> !Constants.LEAGUES.contains(league.getTitle()));
                        League.filterLeagues(leaguesAPIResponse.getLeagues());
                        saveDataInDatabase(leaguesAPIResponse.getLeagues());
                    } else {
                        Log.e(TAG, "Error: " + response.code() + " - " + response.message());
                        responseCallback.onFailure(application.getString(R.string.error_retrieving_leagues));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<League>> call, @NonNull Throwable t) {
                    Log.e(TAG, "Error during API call", t);
                    readDataFromDatabase(lastUpdate);
                }
            });
        } else {
            readDataFromDatabase(lastUpdate);
        }
    }

    private void fetchMatches(List<League> leagueList) {
        for (League league : leagueList) {
            Call<List<Match>> matchResponseCall = matchAPIService.getMatch(
                    league.getKey(), // Supponendo che il League abbia un identificativo `key`
                    application.getString(R.string.api_key),
                    Constants.EUROPE, // Puoi rendere questi parametri dinamici se necessario
                    Constants.H2H,
                    Constants.UNIBET,
                    Constants.DECIMAL
            );

            matchResponseCall.enqueue(new Callback<List<Match>>() {
                @Override
                public void onResponse(@NonNull Call<List<Match>> call,
                                       @NonNull Response<List<Match>> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        List<Match> matchList = response.body();
                        MatchesAPIResponse matchesAPIResponse = new MatchesAPIResponse(matchList);

                        if (matchesAPIResponse.getMatches() != null) {
                            for (Match match : matchesAPIResponse.getMatches()) {
                                // Supponiamo che il Match abbia un campo 'leagueKey' che può essere usato per trovare la League
                                Long correspondingLeagueUid = League.findLeagueUidByKey(match.getSport_key(), leagueList);

                                if (correspondingLeagueUid != null) {
                                    match.setLeagueUid(correspondingLeagueUid); // Assegna il corretto leagueUid
                                } else {
                                    Log.e("MatchUpdate", "Nessuna lega trovata per il match: " + match.getId());
                                }
                            }

                            // Ora possiamo salvare i match nel database con il leagueUid corretto
                            saveMatchesInDatabase(matchesAPIResponse.getMatches());
                        }

                    } else {
                        Log.e(TAG, "Error fetching odds: " + response.code() + " - " + response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Match>> call, @NonNull Throwable t) {
                    Log.e(TAG, "Error fetching odds", t);
                }
            });
        }
    }

    @Override
    public void deleteFavoriteLeagues() {
        LeaguesRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<League> favoriteLeagues = leagueDAO.getLiked();
            for (League league : favoriteLeagues) {
                league.setLiked(false);
            }
            //TODO newsDao.updateListFavoriteNews(favoriteNews);
            responseCallback.onSuccess(leagueDAO.getLiked(), System.currentTimeMillis());
        });
    }

    @Override
    public void updateLeagues(League league) {
        LeaguesRoomDatabase.databaseWriteExecutor.execute(() -> {
            leagueDAO.updateLeague(league);
            //TODO responseCallback.onNewsFavoriteStatusChanged(news);
        });
    }

    /**
     * Gets the list of favorite news from the local database.
     */
    @Override
    public void getFavoriteLeagues() {
        LeaguesRoomDatabase.databaseWriteExecutor.execute(() -> {
            responseCallback.onSuccess(leagueDAO.getLiked(), System.currentTimeMillis());
        });
    }

    private void saveDataInDatabase(List<League> leagueList) {
        LeaguesRoomDatabase.databaseWriteExecutor.execute(() -> {
            // Reads the news from the database
            List<League> allLeagues = leagueDAO.getAllLeagues();

            // Checks if the news just downloaded has already been downloaded earlier
            // in order to preserve the news status (marked as favorite or not)
            if(allLeagues != null) {
                for (League league : allLeagues) {
                    // This check works because News and NewsSource classes have their own
                    // implementation of equals(Object) and hashCode() methods
                    if (leagueList.contains(league)) {
                        // The primary key and the favorite status is contained only in the News objects
                        // retrieved from the database, and not in the News objects downloaded from the
                        // Web Service. If the same news was already downloaded earlier, the following
                        // line of code replaces the the News object in newsList with the corresponding
                        // News object saved in the database, so that it has the primary key and the
                        // favorite status.
                        leagueList.set(leagueList.indexOf(league), league);
                    }
                }
            }

            // Writes the news in the database and gets the associated primary keys
            List<Long> insertedNewsIds = leagueDAO.insertLeaguesList(leagueList);
            for (int i = 0; i < leagueList.size(); i++) {
                // Adds the primary key to the corresponding object News just downloaded so that
                // if the user marks the news as favorite (and vice-versa), we can use its id
                // to know which news in the database must be marked as favorite/not favorite
                leagueList.get(i).setUid(insertedNewsIds.get(i));
            }

            fetchMatches(leagueList);

            responseCallback.onSuccess(leagueList, System.currentTimeMillis());
        });
    }

    private void readDataFromDatabase(long lastUpdate) {
        LeaguesRoomDatabase.databaseWriteExecutor.execute(() -> {
            responseCallback.onSuccess(leagueDAO.getAllLeagues(), lastUpdate);
        });
    }

    private void saveMatchesInDatabase(List<Match> matchList) {
        LeaguesRoomDatabase.databaseWriteExecutor.execute(() -> {
            // Ottiene la lista di tutte le chiavi delle League già presenti nel database
            List<Long> existingLeaguesUids = leagueDAO.getAllLeagueUids();

            // Filtra i match rimuovendo quelli con league_key non presente nel database
            List<Match> validMatches = new ArrayList<>();
            for (Match match : matchList) {
                if (existingLeaguesUids.contains(match.getLeagueUid())) {
                    validMatches.add(match);
                    match.setLeagueUid(match.getLeagueUid());
                } else {
                    Log.e(TAG, "Match scartato: " + match.getId() + " perché la League " + match.getLeagueUid() + " non esiste nel DB.");
                }
            }

            // Se ci sono match validi, salvarli nel database
            if (!validMatches.isEmpty()) {
                matchDAO.insertMatchesList(validMatches);
                Log.d(TAG, "Match odds salvati nel database con successo");
            } else {
                Log.e(TAG, "Nessun match valido da salvare nel database.");
            }
        });
    }


}
