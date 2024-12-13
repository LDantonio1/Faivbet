package com.example.fivebetserio.model;

import java.util.List;

public class League {
    private String name;
    private List<Match> matches;

    public League(String name, List<Match> matches) {
        this.matches = matches;
    }

    public String getName() {
        return name;
    }

    public List<Match> getMatches() {
        return matches;
    }
}

