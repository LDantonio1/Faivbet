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

@Database(
        entities = {League.class, Match.class},
        version = 1,
        exportSchema = false
)
@TypeConverters({BookmakerConverter.class, MarketConverter.class, OutcomeConverter.class})
public abstract class LeaguesRoomDatabase extends RoomDatabase {
    public abstract LeagueDao leagueDao();
    public abstract MatchDao matchDao();

    private static volatile LeaguesRoomDatabase INSTANCE;

    public static LeaguesRoomDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LeaguesRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            LeaguesRoomDatabase.class,
                            "app_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
