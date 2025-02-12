package com.example.fivebetserio.source.league;

import com.example.fivebetserio.model.League;

import java.util.List;

public abstract class BaseLeagueLocalDataSource {
    protected LeagueCallback leagueCallback;

    public void setLeagueCallback(LeagueCallback leagueCallback) {
        this.leagueCallback = leagueCallback;
    }

    public abstract void getLeagues();

    public abstract void getFavoriteLeagues();

    public abstract void updateLeague(League league);

    public abstract void deleteFavoriteLeagues();

    public abstract void insertLeagues(List<League> leagueList);

}