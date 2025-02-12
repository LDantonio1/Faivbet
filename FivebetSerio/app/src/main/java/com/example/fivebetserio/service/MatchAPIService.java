package com.example.fivebetserio.service;

import static com.example.fivebetserio.util.Constants.*;


import com.example.fivebetserio.model.Match;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Path;

/**
 * Interfaccia MatchApiService per la comunicazione con l'API remota.
 */

public interface MatchAPIService {

    @GET("sports/{sport_key}/odds/")
    Call<List<Match>> getMatch(
            @Path("sport_key") String sportKey,
            @Query("apiKey") String apiKey,
            @Query("regions") String regions,
            @Query("markets") String markets,
            @Query("bookmakers") String bookmakers,
            @Query("oddsFormat") String oddsFormat);
}
