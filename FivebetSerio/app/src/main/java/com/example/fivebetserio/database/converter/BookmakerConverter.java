package com.example.fivebetserio.database.converter;

import androidx.room.TypeConverter;

import com.example.fivebetserio.model.Bookmaker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class BookmakerConverter {
    private final Gson gson = new Gson();

    @TypeConverter
    public String fromBookmakerList(List<Bookmaker> bookmakers) {
        return gson.toJson(bookmakers);
    }

    @TypeConverter
    public List<Bookmaker> toBookmakerList(String json) {
        Type listType = new TypeToken<List<Bookmaker>>() {}.getType();
        return gson.fromJson(json, listType);
    }
}
