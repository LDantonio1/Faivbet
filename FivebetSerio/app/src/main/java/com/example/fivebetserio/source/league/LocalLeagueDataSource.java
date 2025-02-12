package com.example.fivebetserio.source.league;

import static com.example.fivebetserio.util.Constants.UNEXPECTED_ERROR;

import com.example.fivebetserio.database.LeagueDao;
import com.example.fivebetserio.database.LeaguesRoomDatabase;
import com.example.fivebetserio.model.League;
import com.example.fivebetserio.util.Constants;
import com.example.fivebetserio.util.SharedPreferencesUtils;

import java.util.List;
/**
 * Classe LocalLeagueDataSource per la gestione delle leghe nel database locale.
 * *
 * Questa classe funge da intermediario tra il database locale (DAO) e il Repository.
 */

public class LocalLeagueDataSource extends BaseLeagueLocalDataSource {

    private final LeagueDao leagueDAO;
    private final SharedPreferencesUtils sharedPreferencesUtil;
    private final LeaguesRoomDatabase leaguesRoomDatabase;

    public LocalLeagueDataSource(LeaguesRoomDatabase leaguesRoomDatabase, SharedPreferencesUtils sharedPreferencesUtil) {
        this.leaguesRoomDatabase = leaguesRoomDatabase;
        this.leagueDAO = leaguesRoomDatabase.leagueDao();
        this.sharedPreferencesUtil = sharedPreferencesUtil;
    }

    /**
     * Gets the leagues from the local database.
     * The method is executed with an ExecutorService defined in LeaguesRoomDatabase class
     * because the database access cannot be executed in the main thread.
     */
    @Override
    public void getLeagues() {
        LeaguesRoomDatabase.databaseWriteExecutor.execute(() -> {
            leagueCallback.onSuccessFromLocalLeague(leagueDAO.getAllLeagues());
        });
    }

    @Override
    public void getFavoriteLeagues() {
        LeaguesRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<League> favoriteLeagues = leagueDAO.getLiked();
            leagueCallback.onLeaguesFavoriteStatusChanged(favoriteLeagues);
        });
    }

    @Override
    public void updateLeague(League league) {
        LeaguesRoomDatabase.databaseWriteExecutor.execute(() -> {
            int rowUpdatedCounter = leagueDAO.updateLeague(league);

            // It means that the update succeeded because only one row had to be updated
            if (rowUpdatedCounter == 1) {
                League updatedLeagues = leagueDAO.getLeague(league.getUid());
                leagueCallback.onLeaguesFavoriteStatusChanged(updatedLeagues, leagueDAO.getLiked());
            } else {
                leagueCallback.onFailureFromLocalLeague(new Exception(UNEXPECTED_ERROR));
            }
        });
    }

    @Override
    public void deleteFavoriteLeagues() {
        LeaguesRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<League> favoriteLeagues = leagueDAO.getLiked();
            for (League league : favoriteLeagues) {
                league.setLiked(false);
            }
            int updatedRowsNumber = leagueDAO.updateListFavoriteLeagues(favoriteLeagues);

            if (updatedRowsNumber == favoriteLeagues.size()) {
                leagueCallback.onDeleteFavoriteLeaguesSuccess(favoriteLeagues);
            } else {
                leagueCallback.onFailureFromLocalLeague(new Exception(UNEXPECTED_ERROR));
            }
        });
    }

    @Override
    public void insertLeagues(List<League> leaguesList) {
        LeaguesRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<League> allLeagues = leagueDAO.getAllLeagues();

            if (leaguesList != null) {
                // Verifica se le leghe scaricate sono già nel database per preservarne lo stato (es. favorite)
                for (League league : allLeagues) {
                    if (leaguesList.contains(league)) {
                        leaguesList.set(leaguesList.indexOf(league), league);
                    }
                }

                List<Long> insertedLeaguesIds = leagueDAO.insertLeaguesList(leaguesList);
                for (int i = 0; i < leaguesList.size(); i++) {
                    leaguesList.get(i).setUid(insertedLeaguesIds.get(i));
                }

                sharedPreferencesUtil.writeStringData(Constants.SHARED_PREFERENCES_FILENAME,
                        Constants.SHARED_PREFERENCES_LAST_UPDATE, String.valueOf(System.currentTimeMillis()));

                leagueCallback.onSuccessFromLocalLeague(leaguesList);
            }
        });
    }

    public void runInTransaction(Runnable runnable) {
        // Assicurati che questa chiamata venga eseguita in un thread separato, ad esempio usando un Executor
        LeaguesRoomDatabase.databaseWriteExecutor.execute(() -> {
            leaguesRoomDatabase.runInTransaction(runnable);
        });
    }

}
