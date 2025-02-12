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

    //public static String formatDate(String date) {
    //    //la data restituita dall'API ha questo formato: 2024-12-13T19:45:00Z
        // Sostituisce l'11° carattere(T) con uno spazio e rimuove l'ultimo carattere(Z)
    //    return date.substring(0, 10) + " " + date.substring(11, date.length() - 4);
    //}

    public static String formatDate(String date) {
        // La data restituita dall'API ha questo formato: 2024-12-13T19:45:00Z
        // Estrai il mese, il giorno e l'ora
        String[] parts = date.split("T"); // Divide la data in due parti: "2024-12-13" e "19:45:00Z"
        String datePart = parts[0]; // "2024-12-13"
        String timePart = parts[1].substring(0, 5); // "19:45" (rimuovi i secondi e la "Z")

        // Estrai il mese e il giorno dalla data
        String[] dateParts = datePart.split("-"); // Divide "2024-12-13" in ["2024", "12", "13"]
        String month = dateParts[1]; // "12"
        String day = dateParts[2]; // "13"

        // Combina mese, giorno e ora nel formato desiderato
        return month + "-" + day + " " + timePart; // Formato: "MM-GG HH:mm"
    }

}
