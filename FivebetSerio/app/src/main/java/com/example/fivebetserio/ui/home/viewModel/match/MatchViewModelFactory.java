package com.example.fivebetserio.ui.home.viewModel.match;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.fivebetserio.repository.Repository;
import com.example.fivebetserio.ui.home.viewModel.match.MatchViewModel;

public class MatchViewModelFactory implements ViewModelProvider.Factory {

    private final Repository repository;

    public MatchViewModelFactory(Repository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MatchViewModel.class)) {
            return (T) new MatchViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
