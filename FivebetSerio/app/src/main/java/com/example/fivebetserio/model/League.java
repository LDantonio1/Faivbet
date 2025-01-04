package com.example.fivebetserio.model;

//Questa classe è usata da LeagueAPIResponse che crea una lista di oggetti League
public class League {
    //i dati devono essere gli stessi restituiti dall'api
    private String key;
    private String group;
    private String title;
    private String description;
    private boolean active;
    private boolean has_outrights;

    public League(String key, String group, String title, String description, boolean active, boolean has_outrights) {
        this.key = key;
        this.group = group;
        this.title = title;
        this.description = description;
        this.active = active;
        this.has_outrights = has_outrights;
    }

    public void setKey(String key) {
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
}
