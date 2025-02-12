package com.example.fivebetserio.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.fivebetserio.model.League;
import com.example.fivebetserio.model.LeaguesAPIResponse;
import com.example.fivebetserio.model.Match;
import com.example.fivebetserio.model.MatchesAPIResponse;
import com.example.fivebetserio.model.Result;
import com.example.fivebetserio.source.league.BaseLeagueLocalDataSource;
import com.example.fivebetserio.source.league.BaseLeagueRemoteDataSource;
import com.example.fivebetserio.source.league.LeagueCallback;
import com.example.fivebetserio.source.match.BaseMatchLocalDataSource;
import com.example.fivebetserio.source.match.BaseMatchRemoteDataSource;
import com.example.fivebetserio.source.match.MatchCallback;
import com.example.fivebetserio.util.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Repository implements LeagueCallback, MatchCallback {

    private final MutableLiveData<Result> allLeaguesMutableLiveData;
    private final MutableLiveData<Result> allMatchesMutableLiveData;
    private final MutableLiveData<Result> favoriteLeaguesMutableLiveData;
    private final BaseLeagueRemoteDataSource remoteLeagueDataSource;
    private final BaseLeagueLocalDataSource localLeagueDataSource;
    private final BaseMatchLocalDataSource localMatchDataSource;
    private final BaseMatchRemoteDataSource remoteMatchDataSource;

    public Repository(BaseLeagueRemoteDataSource remoteDataSource,
                      BaseLeagueLocalDataSource localDataSource,
                      BaseMatchRemoteDataSource remoteMatchDataSource,
                      BaseMatchLocalDataSource localMatchDataSource) {

        allLeaguesMutableLiveData = new MutableLiveData<>();
        allMatchesMutableLiveData = new MutableLiveData<>();
        favoriteLeaguesMutableLiveData = new MutableLiveData<>();
        this.remoteLeagueDataSource = remoteDataSource;
        this.localLeagueDataSource = localDataSource;
        this.localMatchDataSource = localMatchDataSource;
        this.remoteMatchDataSource = remoteMatchDataSource;

        this.remoteLeagueDataSource.setLeagueCallback(this);
        this.localLeagueDataSource.setLeagueCallback(this);
        this.localMatchDataSource.setMatchCallback(this);
        this.remoteMatchDataSource.setMatchCallback(this);
    }

    public MutableLiveData<Result> fetchLeagues(long lastUpdate) {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastUpdate > Constants.FRESH_TIMEOUT) {
            remoteLeagueDataSource.getLeagues();
        } else {
            localLeagueDataSource.getLeagues();
        }

        return allLeaguesMutableLiveData;
    }

    public MutableLiveData<Result> fetchMatches(List<League> leagues, long lastUpdate) {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastUpdate > Constants.FRESH_TIMEOUT) {
            remoteMatchDataSource.getMatches(leagues);
        } else {
            localMatchDataSource.getMatches();
        }

        return allMatchesMutableLiveData;
    }

    public MutableLiveData<Result> getFavoriteLeagues() {
        localLeagueDataSource.getFavoriteLeagues();
        return favoriteLeaguesMutableLiveData;
    }

    public void updateLeague(League league) {
        localLeagueDataSource.updateLeague(league);
    }

    public void deleteFavoriteLeagues() {
        localLeagueDataSource.deleteFavoriteLeagues();
    }

    @Override
    public void onSuccessFromRemoteLeague(LeaguesAPIResponse leaguesAPIResponse, long lastUpdate) {
        localLeagueDataSource.insertLeagues(leaguesAPIResponse.getLeagues());
    }

    @Override
    public void onFailureFromRemoteLeague(Exception exception) {
        allLeaguesMutableLiveData.postValue(new Result.Error(exception.getMessage()));
    }

    @Override
    public void onSuccessFromLocalLeague(List<League> leagueList) {
        allLeaguesMutableLiveData.postValue(new Result.LeagueSuccess(new LeaguesAPIResponse(leagueList)));
    }

    @Override
    public void onFailureFromLocalLeague(Exception exception) {
        Result.Error resultError = new Result.Error(exception.getMessage());
        allLeaguesMutableLiveData.postValue(resultError);
        favoriteLeaguesMutableLiveData.postValue(resultError);
    }

    @Override
    public void onLeaguesFavoriteStatusChanged(League league, List<League> favoriteLeagues) {
        Result allLeaguesResult = allLeaguesMutableLiveData.getValue();
        if (allLeaguesResult != null && allLeaguesResult.isSuccess()) {
            List<League> oldAllLeagues = ((Result.LeagueSuccess) allLeaguesResult).getData().getLeagues();
            if (oldAllLeagues.contains(league)) {
                oldAllLeagues.set(oldAllLeagues.indexOf(league), league);
                allLeaguesMutableLiveData.postValue(allLeaguesResult);
            }
        }
        favoriteLeaguesMutableLiveData.postValue(new Result.LeagueSuccess(new LeaguesAPIResponse(favoriteLeagues)));
    }

    @Override
    public void onLeaguesFavoriteStatusChanged(List<League> favoriteLeagues) {
        favoriteLeaguesMutableLiveData.postValue(new Result.LeagueSuccess(new LeaguesAPIResponse(favoriteLeagues)));
    }

    @Override
    public void onDeleteFavoriteLeaguesSuccess(List<League> favoriteLeagues) {
        Result allLeaguesResult = allLeaguesMutableLiveData.getValue();
        if (allLeaguesResult != null && allLeaguesResult.isSuccess()) {
            List<League> oldAllLeagues = ((Result.LeagueSuccess) allLeaguesResult).getData().getLeagues();
            for (League league : favoriteLeagues) {
                if (oldAllLeagues.contains(league)) {
                    oldAllLeagues.set(oldAllLeagues.indexOf(league), league);
                }
            }
            allLeaguesMutableLiveData.postValue(allLeaguesResult);
        }

        favoriteLeagues.clear();
        favoriteLeaguesMutableLiveData.postValue(new Result.LeagueSuccess(new LeaguesAPIResponse(favoriteLeagues)));
    }

    @Override
    public void onSuccessFromRemoteMatch(MatchesAPIResponse matchesAPIResponse, long lastUpdate) {
        localMatchDataSource.insertMatches(matchesAPIResponse.getMatches());
    }

    @Override
    public void onFailureFromRemoteMatch(Exception exception) {
        allMatchesMutableLiveData.postValue(new Result.Error(exception.getMessage()));
    }

    @Override
    public void onSuccessFromLocalMatch(List<Match> matchList) {
        allMatchesMutableLiveData.postValue(new Result.MatchSuccess(new MatchesAPIResponse(matchList)));
    }

    @Override
    public void onFailureFromLocalMatch(Exception exception) {
        Result.Error resultError = new Result.Error(exception.getMessage());
        allMatchesMutableLiveData.postValue(resultError);
    }
}
