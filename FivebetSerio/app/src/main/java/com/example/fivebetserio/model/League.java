package com.example.fivebetserio.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//Questa classe è usata da LeagueAPIResponse che crea una lista di oggetti League
@Entity
public class League {
    //i seguenti dati devono essere gli stessi restituiti dall'api
    @PrimaryKey
    @NonNull
    private String key;
    private String group;
    private String title;
    private String description;
    private boolean active;
    private boolean has_outrights;
    private boolean liked;

    public League(@NonNull String key, String group, String title, String description, boolean active, boolean has_outrights) {
        this.key = key;
        this.group = group;
        this.title = title;
        this.description = description;
        this.active = active;
        this.has_outrights = has_outrights;
    }

    public void setKey(@NonNull String key) {
        this.key = key;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setHas_outrights(boolean has_outrights) {
        this.has_outrights = has_outrights;
    }

    @NonNull
    public String getKey() {
        return key;
    }

    public String getGroup() {
        return group;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isHas_outrights() {
        return has_outrights;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
