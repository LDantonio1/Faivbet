package com.example.fivebetserio.model;

import java.util.List;

//Questa classe contiene il risultato ottenuto dall'API convertito in classe java
public class MatchesAPIResponse {
    private List<Match> matches;

    public MatchesAPIResponse(List<Match> matches) {
        this.matches = matches;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }
}
