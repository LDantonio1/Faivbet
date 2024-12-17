package com.example.fivebetserio.model;

import java.util.List;

public class MatchAPIResponse {
    private String status;
    private int totalResults;
    private List<Match> matches;

    public MatchAPIResponse() {}

    public String getStatus() {
        return status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }
}
