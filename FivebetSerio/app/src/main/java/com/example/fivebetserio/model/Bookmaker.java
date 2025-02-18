package com.example.fivebetserio.model;

import androidx.room.TypeConverters;

import com.example.fivebetserio.database.converter.MarketConverter;

import java.util.List;

//classe usata per la decodifica dei dati ritornati dall'API per i match
public class Bookmaker {
    private String key;
    private String title;
    private String last_update;
    @TypeConverters(MarketConverter.class)
    private List<Market> markets;

    public Bookmaker(String key, String title, String last_update, List<Market> markets) {
        this.key = key;
        this.title = title;
        this.last_update = last_update;
        this.markets = markets;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public List<Market> getMarkets() {
        return markets;
    }

    public void setMarkets(List<Market> markets) {
        this.markets = markets;
    }
}
