package com.example.fivebetserio.repository;

import com.example.fivebetserio.model.League;

public interface ILeagueRepository {

    void fetchLeagues(int page, long lastUpdate);

    void updateLeagues(League league);

    void getFavoriteLeagues();

    void deleteFavoriteLeagues();
}