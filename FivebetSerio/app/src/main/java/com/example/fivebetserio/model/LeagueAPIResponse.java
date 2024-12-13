package com.example.fivebetserio.model;

import java.util.List;

public class LeagueAPIResponse {
    private String status;
    private int totalResults;
    private List<League> leagues;

    public LeagueAPIResponse() {}

    public String getStatus() {
        return status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public List<League> getLeagues() {
        return leagues;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public void setLeagues(List<League> leagues) {
        this.leagues = leagues;
    }
}
