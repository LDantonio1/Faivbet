package com.example.fivebetserio.source.match;

import com.example.fivebetserio.model.League;

import java.util.List;

public abstract class BaseMatchRemoteDataSource {
    protected MatchCallback matchCallback;

    public void setMatchCallback(MatchCallback matchCallback) {
        this.matchCallback = matchCallback;
    }

    public abstract void getMatches(List<League> leaguesList);
}
