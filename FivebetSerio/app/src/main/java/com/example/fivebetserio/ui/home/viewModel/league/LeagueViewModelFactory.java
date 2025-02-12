package com.example.fivebetserio.ui.home.viewModel.league;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.fivebetserio.repository.Repository;

public class LeagueViewModelFactory implements ViewModelProvider.Factory {

    private final Repository repository;

    public LeagueViewModelFactory(Repository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new LeagueViewModel(repository);
    }
}
