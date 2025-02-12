package com.example.fivebetserio.source.match;

import com.example.fivebetserio.database.LeaguesRoomDatabase;
import com.example.fivebetserio.database.MatchDao;
import com.example.fivebetserio.model.Match;

import java.util.List;

/**
 * Classe LocalMatchDataSource per la gestione dei match nel database locale.
 *
 */

public class LocalMatchDataSource extends BaseMatchLocalDataSource {

    private final MatchDao matchDAO;

    public LocalMatchDataSource(LeaguesRoomDatabase leaguesRoomDatabase) {
        this.matchDAO = leaguesRoomDatabase.matchDao();
    }

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
