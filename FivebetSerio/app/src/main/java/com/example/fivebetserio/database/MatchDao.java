package com.example.fivebetserio.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.fivebetserio.model.League;
import com.example.fivebetserio.model.Match;

import java.util.List;

@Dao
public interface MatchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMatch(Match match);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMatchesList(List<Match> MatchesList);

    @Query("SELECT * FROM `match` WHERE leagueUid = :leagueUid")
    List<Match> getMatchesByLeague(long leagueUid);

    @Query("SELECT * FROM `match`")
    List<Match> getAllMatches();
}

