package com.example.fivebetserio.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fivebetserio.model.League;

import java.util.List;

@Dao
public interface LeagueDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLeague(League league);

    @Query("SELECT * FROM league")
    LiveData<List<League>> getAllLeagues();

    @Query("SELECT * FROM league ORDER BY liked DESC, title ASC")
    LiveData<List<League>> getAllLeaguesOrderedByLiked();

    @Update
    void updateLeague(League league);
}

