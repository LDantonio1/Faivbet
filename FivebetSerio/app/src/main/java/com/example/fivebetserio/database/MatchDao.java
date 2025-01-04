package com.example.fivebetserio.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.fivebetserio.model.Match;

import java.util.List;

@Dao
public interface MatchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMatch(Match match);

    @Query("SELECT * FROM `match` WHERE leagueKey = :leagueKey")
    LiveData<List<Match>> getMatchesByLeague(String leagueKey);
}

