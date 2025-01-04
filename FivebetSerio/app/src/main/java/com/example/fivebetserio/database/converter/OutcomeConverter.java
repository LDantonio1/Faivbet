package com.example.fivebetserio.database.converter;

import androidx.room.TypeConverter;

import com.example.fivebetserio.model.Outcome;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class OutcomeConverter {
    private final Gson gson = new Gson();

    @TypeConverter
    public String fromOutcomeList(List<Outcome> outcomes) {
        return gson.toJson(outcomes);
    }

    @TypeConverter
    public List<Outcome> toOutcomeList(String json) {
        Type listType = new TypeToken<List<Outcome>>() {}.getType();
        return gson.fromJson(json, listType);
    }
}

