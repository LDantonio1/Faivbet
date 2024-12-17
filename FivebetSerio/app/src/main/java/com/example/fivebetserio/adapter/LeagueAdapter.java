package com.example.fivebetserio.adapter;

import android.util.Log;

import com.example.fivebetserio.model.League;
import com.example.fivebetserio.model.Match;

import java.util.ArrayList;
import java.util.List;

public class LeagueAdapter {
    public static List<League> getLeagues() {
        List<League> leagues = new ArrayList<>();

        //per ora ho fatto che mi genera delle liste fittizie ma devo capire bene a cosa serve nell'implementazione del database

        List<Match> matches = new ArrayList<>();
        //matches.add(new Match("INT", "EMP"));
        //matches.add(new Match("MLN", "FIO"));
        //matches.add(new Match("JVN", "NPL"));

        leagues.add(new League("Serie A", matches));
        leagues.add(new League("Champions League", matches));
        leagues.add(new League("Europa League", matches));

        Log.d("prova", "getLeagues: " + leagues);
        return leagues;
    }
}
