package com.example.fivebetserio.database.converter;

import androidx.room.TypeConverter;

import com.example.fivebetserio.model.Market;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class MarketConverter {
    private final Gson gson = new Gson();

    @TypeConverter
    public String fromMarketList(List<Market> markets) {
        return gson.toJson(markets);
    }

    @TypeConverter
    public List<Market> toMarketList(String json) {
        Type listType = new TypeToken<List<Market>>() {}.getType();
        return gson.fromJson(json, listType);
    }
}

