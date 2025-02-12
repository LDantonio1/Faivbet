package com.example.fivebetserio.source.league;

import com.example.fivebetserio.model.League;
import com.example.fivebetserio.model.LeaguesAPIResponse;

import java.util.List;

public interface LeagueCallback {
    void onSuccessFromRemoteLeague(LeaguesAPIResponse leaguesAPIResponse, long lastUpdate);
    void onFailureFromRemoteLeague(Exception exception);
    void onSuccessFromLocalLeague(List<League> leaguesList);
    void onFailureFromLocalLeague(Exception exception);
    void onLeaguesFavoriteStatusChanged(League league, List<League> favoriteLeagues);
    void onLeaguesFavoriteStatusChanged(List<League> leagues);
    void onDeleteFavoriteLeaguesSuccess(List<League> favoriteLeagues);
}
