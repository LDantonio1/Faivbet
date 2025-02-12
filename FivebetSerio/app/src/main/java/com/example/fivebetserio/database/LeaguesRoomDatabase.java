package com.example.fivebetserio.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.fivebetserio.model.League;
import com.example.fivebetserio.model.Match;
import com.example.fivebetserio.database.converter.BookmakerConverter;
import com.example.fivebetserio.database.converter.MarketConverter;
import com.example.fivebetserio.database.converter.OutcomeConverter;
import com.example.fivebetserio.util.Constants;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Database Room per la gestione delle leghe e dei match.
 * Fornisce l'accesso ai DAO e gestisce l'istanza del database.
 *
 * Viene utilizzato come fonte di dati locale per l'applicazione, permettendo il caching delle informazioni.
 */

@Database(
        entities = {League.class, Match.class},
        version = Constants.DATABASE_VERSION,
        exportSchema = false
)
@TypeConverters({BookmakerConverter.class, MarketConverter.class, OutcomeConverter.class})
public abstract class LeaguesRoomDatabase extends RoomDatabase {
    public abstract LeagueDao leagueDao();
    public abstract MatchDao matchDao();

    private static volatile LeaguesRoomDatabase INSTANCE;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static LeaguesRoomDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LeaguesRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            LeaguesRoomDatabase.class,
                            Constants.SAVED_LEAGUES_DATABASE
                    ).fallbackToDestructiveMigration()  // Cancella e ricrea il database
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
