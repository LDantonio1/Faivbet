package com.example.fivebetserio.service;

import static com.example.fivebetserio.util.Constants.*;

import com.example.fivebetserio.model.League;
import com.example.fivebetserio.model.LeaguesAPIResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Interfaccia LeagueApiService per la comunicazione con l'API remota.
 * Definisce i metodi per recuperare le informazioni sulle leghe disponibili.
 *
 * Viene utilizzata da Repository per recuperare i dati aggiornati.
 */

public interface LeagueAPIService {

    @GET(TOP_HEADLINES_ENDPOINT)
    Call<List<League>> getLeagues(
            @Query("apiKey") String apiKey);
}
