package com.example.fivebetserio.ui.home.viewModel.match;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fivebetserio.model.League;
import com.example.fivebetserio.model.Result;
import com.example.fivebetserio.repository.Repository;

import java.util.List;
/**
 * ViewModel per la gestione dei match.
 *
 * Responsabilità principali:
 * - Fornire i dati dei match alla UI in modo reattivo.
 * - Recuperare i match dal Repository, scegliendo tra database locale e API remota.
 * - Esporre LiveData per permettere alla UI di osservare i cambiamenti dei dati.
 * - Gestire la logica di business per il recupero e l'aggiornamento dei match.
 *
 * Questa classe segue l'architettura MVVM e consente alla UI di ottenere i dati
 * senza interagire direttamente con il Repository.
 */

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
