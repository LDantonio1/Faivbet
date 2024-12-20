package com.example.fivebetserio.model;

import java.util.List;

//Questa classe contiene il risultato ottenuto dall'API convertito in classe java
public class MatchesAPIResponse {
    private List<Match> matches;

    public MatchesAPIResponse(List<Match> matches) {
        for (Match match: matches) {
           match.setCommence_time(formatDate(match.getCommence_time()));
        }
        this.matches = matches;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public static String formatDate(String date) {
        //la data restituita dall'API ha questo formato: 2024-12-13T19:45:00Z
        // Sostituisce l'11° carattere(T) con uno spazio e rimuove l'ultimo carattere(Z)
        return date.substring(0, 10) + " " + date.substring(11, date.length() - 4);
    }
}
