package com.example.fivebetserio.source.league;

public abstract class BaseLeagueRemoteDataSource {
    protected LeagueCallback leagueCallback;

    public void setLeagueCallback(LeagueCallback leagueCallback) {
        this.leagueCallback = leagueCallback;
    }

    public abstract void getLeagues();
}

