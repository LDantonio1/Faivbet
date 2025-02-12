package com.example.fivebetserio.ui.home.viewModel.match;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fivebetserio.model.League;
import com.example.fivebetserio.model.Result;
import com.example.fivebetserio.repository.Repository;

import java.util.List;

public class MatchViewModel extends ViewModel {
    private final Repository repository;
    private MutableLiveData<Result> matchesListLiveData;

    public MatchViewModel(Repository repository) {
        this.repository = repository;
    }

    public MutableLiveData<Result> getMatches(List<League> leagues, long lastUpdate) {
        if (matchesListLiveData == null) {
            fetchMatches(leagues, lastUpdate);
        }
        return matchesListLiveData;
    }

    private void fetchMatches(List<League> leagues, long lastUpdate) {
        matchesListLiveData = repository.fetchMatches(leagues, lastUpdate);
    }
}
