package com.example.fivebetserio.source.match;

import com.example.fivebetserio.database.LeaguesRoomDatabase;
import com.example.fivebetserio.database.MatchDao;
import com.example.fivebetserio.model.Match;

import java.util.List;

public class LocalMatchDataSource extends BaseMatchLocalDataSource {

    private final MatchDao matchDAO;

    public LocalMatchDataSource(LeaguesRoomDatabase leaguesRoomDatabase) {
        this.matchDAO = leaguesRoomDatabase.matchDao();
    }

    /**
     * Gets the leagues from the local database.
     * The method is executed with an ExecutorService defined in LeaguesRoomDatabase class
     * because the database access cannot be executed in the main thread.
     */
    @Override
    public void getMatches() {
        LeaguesRoomDatabase.databaseWriteExecutor.execute(() -> {
            matchCallback.onSuccessFromLocalMatch(matchDAO.getAllMatches());
        });
    }

    @Override
    public void insertMatches(List<Match> matchesList) {
        LeaguesRoomDatabase.databaseWriteExecutor.execute(() -> {
            matchDAO.insertMatchesList(matchesList);
            matchCallback.onSuccessFromLocalMatch(matchesList);
        });
    }
}
