package com.example.fivebetserio.model;

import com.example.fivebetserio.util.Constants;

import java.util.List;

//Questa classe contiene il risultato ottenuto dall'API convertito in classe java
public class LeaguesAPIResponse {
    List<League> leagues;

    public LeaguesAPIResponse(List<League> leagues) {
        //con questa riga di codice filtro la lista in modo che contenga solo le leghe presenti in Constants.LEAGUES
        leagues.removeIf(league -> !Constants.LEAGUES.contains(league.getTitle()));

        this.leagues = leagues;
    }

    public void setLeagues(List<League> leagues) {
        this.leagues = leagues;
    }

    public List<League> getLeagues() {
        return leagues;
    }
}
