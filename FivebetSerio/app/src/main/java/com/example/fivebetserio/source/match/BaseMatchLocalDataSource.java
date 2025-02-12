package com.example.fivebetserio.source.match;

import com.example.fivebetserio.model.Match;

import java.util.List;

public abstract class BaseMatchLocalDataSource {
    protected MatchCallback matchCallback;

    public void setMatchCallback(MatchCallback matchCallback) {
        this.matchCallback = matchCallback;
    }

    public abstract void getMatches();

    public abstract void insertMatches(List<Match> matchesList);
}
