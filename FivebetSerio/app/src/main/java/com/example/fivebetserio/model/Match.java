package com.example.fivebetserio.model;

//questa classe è usata da MatchAPIResponse che crea una lista di Match
public class Match {
    private String id;
    private String sport_key;
    private String sport_title;
    private String commence_time;
    private String home_team;
    private String away_team;

    public Match(String id, String sport_key, String sport_title, String commence_time, String home_team, String away_team) {
        this.id = id;
        this.sport_key = sport_key;
        this.sport_title = sport_title;
        this.commence_time = commence_time;
        this.home_team = home_team;
        this.away_team = away_team;
    }

    public String getId() {
        return id;
    }

    public String getSport_key() {
        return sport_key;
    }

    public String getSport_title() {
        return sport_title;
    }

    public String getCommence_time() {
        return commence_time;
    }

    public String getHome_team() {
        return home_team;
    }

    public String getAway_team() {
        return away_team;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSport_key(String sport_key) {
        this.sport_key = sport_key;
    }

    public void setSport_title(String sport_title) {
        this.sport_title = sport_title;
    }

    public void setCommence_time(String commence_time) {
        this.commence_time = commence_time;
    }

    public void setHome_team(String home_team) {
        this.home_team = home_team;
    }

    public void setAway_team(String away_team) {
        this.away_team = away_team;
    }
}