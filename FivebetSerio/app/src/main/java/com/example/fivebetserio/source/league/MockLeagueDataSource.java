package com.example.fivebetserio.source.league;

import static com.example.fivebetserio.util.Constants.API_KEY_ERROR;

import com.example.fivebetserio.model.LeaguesAPIResponse;
import com.example.fivebetserio.util.Constants;
import com.example.fivebetserio.util.JSONParserUtils;

import java.io.IOException;
/**
 * Classe MockLeagueDataSource per la simulazione del recupero delle leghe.
 */

public class MockLeagueDataSource extends BaseLeagueRemoteDataSource {

    private final JSONParserUtils jsonParserUtil;

    public MockLeagueDataSource(JSONParserUtils jsonParserUtil) {
        this.jsonParserUtil = jsonParserUtil;
    }

    @Override
    public void getLeagues() {
        LeaguesAPIResponse leaguesAPIResponse = null;

        try {
            leaguesAPIResponse = jsonParserUtil.parseLeaguesJSONFileWithGSon(Constants.LEAGUES_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (leaguesAPIResponse != null) {



            leagueCallback.onSuccessFromRemoteLeague(leaguesAPIResponse, System.currentTimeMillis());
        } else {
            leagueCallback.onFailureFromRemoteLeague(new Exception(API_KEY_ERROR));
        }
    }


}
