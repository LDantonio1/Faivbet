package com.example.fivebetserio.source.match;

import com.example.fivebetserio.model.Match;
import com.example.fivebetserio.model.MatchesAPIResponse;
import java.util.List;

public interface MatchCallback {
    void onSuccessFromRemoteMatch(MatchesAPIResponse matchesAPIResponse, long lastUpdate);
    void onFailureFromRemoteMatch(Exception exception);
    void onSuccessFromLocalMatch(List<Match> matchesList);
    void onFailureFromLocalMatch(Exception exception);
}
