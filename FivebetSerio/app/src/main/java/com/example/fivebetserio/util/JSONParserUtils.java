package com.example.fivebetserio.util;
import android.content.Context;

import com.google.gson.Gson;
import com.example.fivebetserio.model.LeagueAPIResponse;
import com.example.fivebetserio.model.MatchAPIResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JSONParserUtils {

    public Context context;

    public JSONParserUtils(Context context) {
        this.context = context;
    }

    public LeagueAPIResponse parseLeaguesJSONFileWithGSon(String filename) throws IOException {
        InputStream inputStream = context.getAssets().open(filename);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        return new Gson().fromJson(bufferedReader, LeagueAPIResponse.class);
    }

    public MatchAPIResponse parseMatchesJSONFileWithGSon(String filename) throws IOException {
        InputStream inputStream = context.getAssets().open(filename);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        return new Gson().fromJson(bufferedReader, MatchAPIResponse.class);
    }
}
