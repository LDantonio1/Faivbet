package com.example.fivebetserio.ui.home.viewModel.league;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fivebetserio.model.League;
import com.example.fivebetserio.model.Result;
import com.example.fivebetserio.repository.Repository;

public class LeagueViewModel extends ViewModel {

    private static final String TAG = LeagueViewModel.class.getSimpleName();

    private final Repository repository;
    private MutableLiveData<Result> leaguesListLiveData;
    private MutableLiveData<Result> favoriteLeaguesListLiveData;

    public LeagueViewModel(Repository repository) {
        this.repository = repository;
    }

    public MutableLiveData<Result> getLeagues(long lastUpdate) {
        if (leaguesListLiveData == null) {
            fetchLeagues(lastUpdate);
        }
        return leaguesListLiveData;
    }

    public MutableLiveData<Result> getFavoriteArticlesLiveData() {
        if (favoriteLeaguesListLiveData == null) {
            getFavoriteLeagues();
        }
        return favoriteLeaguesListLiveData;
    }


    public void updateLeague(League league) {
        repository.updateLeague(league);
    }

    private void fetchLeagues(long lastUpdate) {
        leaguesListLiveData = repository.fetchLeagues(lastUpdate);
    }

    private void getFavoriteLeagues() {
        favoriteLeaguesListLiveData = repository.getFavoriteLeagues();
    }

    public void removeFromFavorite(League league) {
        repository.updateLeague(league);
    }

    public void deleteAllFavoriteLeagues() {
        repository.deleteFavoriteLeagues();
    }
}
