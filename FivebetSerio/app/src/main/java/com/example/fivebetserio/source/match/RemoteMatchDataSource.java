package com.example.fivebetserio.source.match;

import static com.example.fivebetserio.util.Constants.API_KEY_ERROR;
import static com.example.fivebetserio.util.Constants.RETROFIT_ERROR;

import androidx.annotation.NonNull;

import com.example.fivebetserio.model.League;
import com.example.fivebetserio.model.Match;
import com.example.fivebetserio.model.MatchesAPIResponse;
import com.example.fivebetserio.service.MatchAPIService;
import com.example.fivebetserio.util.Constants;
import com.example.fivebetserio.util.ServiceLocator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Classe RemoteMatchDataSource per la gestione dei match tramite API remota.
 *
 * Questa classe viene utilizzata dal Repository per garantire l'accesso a dati sempre aggiornati.
 */

public class RemoteMatchDataSource extends BaseMatchRemoteDataSource {

    private final MatchAPIService matchAPIService;
    private final String apiKey;

    public RemoteMatchDataSource(String apiKey) {
        this.apiKey = apiKey;
        this.matchAPIService = ServiceLocator.getInstance().getMatchAPIService();
    }

    @Override
    public void getMatches(List<League> leaguesList) {
        for (League league : leaguesList) {
            Call<List<Match>> matchesResponseCall = matchAPIService.getMatch(
                    league.getKey(),
                    apiKey,
                    Constants.EUROPE,
                    Constants.H2H,
                    Constants.UNIBET,
                    Constants.DECIMAL
            );

            matchesResponseCall.enqueue(new Callback<List<Match>>() {
                @Override
                public void onResponse(@NonNull Call<List<Match>> call, @NonNull Response<List<Match>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Match> matches = response.body();

                        // Associa i match alla lega corrispondente
                        for (Match match : matches) {
                            match.setLeagueUid(league.getUid());
                        }

                        MatchesAPIResponse matchesAPIResponse = new MatchesAPIResponse(matches);

                        if (matchCallback != null) {
                            matchCallback.onSuccessFromRemoteMatch(matchesAPIResponse, System.currentTimeMillis());
                        }
                    } else {
                        if (matchCallback != null) {
                            matchCallback.onFailureFromRemoteMatch(new Exception(API_KEY_ERROR));
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Match>> call, @NonNull Throwable t) {
                    if (matchCallback != null) {
                        matchCallback.onFailureFromRemoteMatch(new Exception(RETROFIT_ERROR));
                    }
                }
            });
        }
    }


}

