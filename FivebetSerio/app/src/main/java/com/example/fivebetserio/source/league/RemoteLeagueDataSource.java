package com.example.fivebetserio.source.league;

import static com.example.fivebetserio.util.Constants.API_KEY_ERROR;
import static com.example.fivebetserio.util.Constants.RETROFIT_ERROR;

import androidx.annotation.NonNull;

import com.example.fivebetserio.model.League;
import com.example.fivebetserio.model.LeaguesAPIResponse;
import com.example.fivebetserio.service.LeagueAPIService;
import com.example.fivebetserio.util.ServiceLocator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteLeagueDataSource extends BaseLeagueRemoteDataSource {

    private final LeagueAPIService leagueAPIService;
    private final String apiKey;

    public RemoteLeagueDataSource(String apiKey) {
        this.apiKey = apiKey;
        this.leagueAPIService = ServiceLocator.getInstance().getLeagueAPIService();
    }

    @Override
    public void getLeagues() {
        Call<List<League>> leaguesResponseCall = leagueAPIService.getLeagues(apiKey);

        leaguesResponseCall.enqueue(new Callback<List<League>>() {
            @Override
            public void onResponse(@NonNull Call<List<League>> call,
                                   @NonNull Response<List<League>> response) {
                LeaguesAPIResponse leaguesAPIResponse = new LeaguesAPIResponse(response.body());
                if (response.body() != null && response.isSuccessful()) {
                    leagueCallback.onSuccessFromRemoteLeague(leaguesAPIResponse, System.currentTimeMillis());

                } else {
                    leagueCallback.onFailureFromRemoteLeague(new Exception(API_KEY_ERROR));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<League>> call, @NonNull Throwable t) {
                leagueCallback.onFailureFromRemoteLeague(new Exception(RETROFIT_ERROR));
            }
        });
    }
}
