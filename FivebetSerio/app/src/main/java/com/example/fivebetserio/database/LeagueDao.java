package com.example.fivebetserio.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fivebetserio.model.League;

import java.util.List;

/**
 * Data Access Object (DAO) per la gestione delle operazioni sulle leghe nel database locale.
 * Fornisce metodi per l'inserimento, l'aggiornamento, l'eliminazione e il recupero delle leghe.
*
 * Questo DAO viene utilizzato dal Repository per accedere ai dati in modo asincrono.
 */
@Dao
public interface LeagueDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLeague(League league);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertLeaguesList(List<League> LeaguesList);

    @Query("SELECT * FROM League WHERE uid = :id")
    League getLeague(long id);

    @Query("SELECT uid FROM League")
    List<Long> getAllLeagueUids();

    @Query("SELECT * FROM League WHERE liked = 1")
    List<League> getLiked();

    @Query("SELECT * FROM league")
    List<League> getAllLeagues();

    @Update
    int updateLeague(League league);

    @Update
    int updateListFavoriteLeagues(List<League> leagues);


}

