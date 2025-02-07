package com.example.fivebetserio.util;

import com.example.fivebetserio.model.League;

import java.util.List;

public interface ResponseCallback {
    void onSuccess(List<League> leaguesList, long lastUpdate);
    void onFailure(String errorMessage);
}

