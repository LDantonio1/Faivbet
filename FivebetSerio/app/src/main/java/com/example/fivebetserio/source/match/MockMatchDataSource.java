package com.example.fivebetserio.source.match;

import static com.example.fivebetserio.util.Constants.API_KEY_ERROR;

import com.example.fivebetserio.model.League;
import com.example.fivebetserio.model.LeaguesAPIResponse;
import com.example.fivebetserio.model.Match;
import com.example.fivebetserio.model.MatchesAPIResponse;
import com.example.fivebetserio.util.Constants;
import com.example.fivebetserio.util.JSONParserUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MockMatchDataSource extends BaseMatchRemoteDataSource {

    private final JSONParserUtils jsonParserUtil;

    public MockMatchDataSource(JSONParserUtils jsonParserUtil) {
        this.jsonParserUtil = jsonParserUtil;
    }

    @Override
    public void getMatches(List<League> leaguesList) {
        MatchesAPIResponse matchesAPIResponse = null;

        for(League league : leaguesList){
            try {
                matchesAPIResponse = jsonParserUtil.parseMatchesJSONFileWithGSon(Constants.MATCHES_FILE);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (matchesAPIResponse != null) {
                MatchesAPIResponse modifiedMatchesAPIResponse = modifyMatches(matchesAPIResponse, league);
                matchCallback.onSuccessFromRemoteMatch(modifiedMatchesAPIResponse, System.currentTimeMillis());
            } else {
                matchCallback.onFailureFromRemoteMatch(new Exception(API_KEY_ERROR));
            }
        }
    }

    public static MatchesAPIResponse modifyMatches(MatchesAPIResponse matchesAPIResponse, League league) {
        List<Match> matchesList = matchesAPIResponse.getMatches();
        for (Match match : matchesList) {
            match.setId(match.getId() + league.getUid());
            match.setSport_key(league.getKey());
            match.setLeagueUid(league.getUid());
            match.setSport_title(league.getTitle());
        }
        matchesAPIResponse.setMatches(matchesList);
        return matchesAPIResponse;
    }

}
