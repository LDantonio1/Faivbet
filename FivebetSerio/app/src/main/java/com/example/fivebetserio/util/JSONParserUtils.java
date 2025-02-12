package com.example.fivebetserio.util;

import android.content.res.AssetManager;

import com.example.fivebetserio.model.League;
import com.example.fivebetserio.model.LeaguesAPIResponse;
import com.example.fivebetserio.model.Match;
import com.example.fivebetserio.model.MatchesAPIResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Utility per il parsing di dati JSON.
 */

public class JSONParserUtils {
    private AssetManager assetManager;

    public JSONParserUtils(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    // Metodo per analizzare il file JSON delle leghe
    public LeaguesAPIResponse parseLeaguesJSONFileWithGSon(String filename) throws IOException {
        InputStream inputStream = assetManager.open(filename);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        Type listType = new TypeToken<List<League>>(){}.getType();
        List<League> leagues = new Gson().fromJson(bufferedReader, listType);

        return new LeaguesAPIResponse(leagues);
    }

    // Metodo per analizzare il file JSON delle partite
    public MatchesAPIResponse parseMatchesJSONFileWithGSon(String filename) throws IOException {
        InputStream inputStream = assetManager.open(filename);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));


        Type listType = new TypeToken<List<Match>>(){}.getType();
        List<Match> matches = new Gson().fromJson(bufferedReader, listType);

        return new MatchesAPIResponse(matches);
    }
}
