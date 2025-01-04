package com.example.fivebetserio.model;

import java.util.List;

public class Market {
    private String key;
    private String last_update;
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
