package com.example.fivebetserio.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.fivebetserio.database.converter.BookmakerConverter;

import java.util.List;

//questa classe è usata da MatchAPIResponse che crea una lista di Match
@Entity(
        foreignKeys = @ForeignKey(
                entity = League.class,
                parentColumns = "uid",
                childColumns = "leagueUid",
                onDelete = ForeignKey.CASCADE
        )
)
public class Match {
    @PrimaryKey
    @NonNull
    private String id;
    private String sport_key;
    private String sport_title;
    private String commence_time;
    private String home_team;
    private String away_team;
    private long leagueUid; // Chiave esterna per la relazione con League
    @TypeConverters(BookmakerConverter.class)
    private List<Bookmaker> bookmakers;

    public Match(@NonNull String id, String sport_key, String sport_title, String commence_time, String home_team, String away_team, List<Bookmaker> bookmakers, long leagueUid) {
        this.id = id;
        this.sport_key = sport_key;
        this.sport_title = sport_title;
        this.commence_time = commence_time;
        this.home_team = home_team;
        this.away_team = away_team;
        this.bookmakers = bookmakers;
        this.leagueUid = leagueUid;
    }

    public Match(Match match) {
        this.id = match.id;
        this.sport_key = match.sport_key;
        this.sport_title = match.sport_title;
        this.commence_time = match.commence_time;
        this.home_team = match.home_team;
        this.away_team = match.away_team;
        this.bookmakers = match.bookmakers;
        this.leagueUid = match.leagueUid;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getSport_key() {
        return sport_key;
    }

    public void setSport_key(String sport_key) {
        this.sport_key = sport_key;
    }

    public String getSport_title() {
        return sport_title;
    }

    public void setSport_title(String sport_title) {
        this.sport_title = sport_title;
    }

    public String getCommence_time() {
        return commence_time;
    }

    public void setCommence_time(String commence_time) {
        this.commence_time = commence_time;
    }

    public String getHome_team() {
        return home_team;
    }

    public void setHome_team(String home_team) {
        this.home_team = home_team;
    }

    public String getAway_team() {
        return away_team;
    }

    public void setAway_team(String away_team) {
        this.away_team = away_team;
    }

    public List<Bookmaker> getBookmakers() {
        return bookmakers;
    }

    public void setBookmakers(List<Bookmaker> bookmakers) {
        this.bookmakers = bookmakers;
    }

    public long getLeagueUid() {return leagueUid;}

    public void setLeagueUid(long leagueuid) {this.leagueUid = leagueuid;}
}