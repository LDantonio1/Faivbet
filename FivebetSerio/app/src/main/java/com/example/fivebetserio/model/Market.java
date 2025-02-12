package com.example.fivebetserio.model;

import androidx.room.TypeConverters;

import com.example.fivebetserio.database.converter.OutcomeConverter;

import java.util.List;


//classe usata per la decodifica dei dati ritornati dall'API per i match
public class Market {
    private String key;
    private String last_update;

    @TypeConverters(OutcomeConverter.class)
    private List<Outcome> outcomes;

    public Market(String key, String last_update, List<Outcome> outcomes) {
        this.key = key;
        this.last_update = last_update;
        this.outcomes = outcomes;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public List<Outcome> getOutcomes() {
        return outcomes;
    }

    public void setOutcomes(List<Outcome> markets) {
        this.outcomes = markets;
    }
}
