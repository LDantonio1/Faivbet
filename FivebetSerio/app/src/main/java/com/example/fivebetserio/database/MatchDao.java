package com.example.fivebetserio.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fivebetserio.model.Match;

import java.util.List;

/**
 * Data Access Object (DAO) per la gestione delle operazioni sui match nel database locale.
 * Definisce metodi per inserire, aggiornare, eliminare e recuperare i match in base a diversi criteri.
 *
 * Questo DAO è utilizzato dal Repository per fornire dati aggiornati in modo efficiente.
 */

@Dao
public interface MatchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMatch(Match match);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMatchesList(List<Match> MatchesList);

    @Query("SELECT * FROM `match` WHERE leagueUid = :leagueId")
    List<Match> getMatchesForLeague(long leagueId);

    @Query("SELECT * FROM `match`")
    List<Match> getAllMatches();

    @Query("SELECT * FROM `match` WHERE id = :id")
    Match getMatch(String id);

    @Update
    int updateMatch(Match match);
}

