package com.example.fivebetserio.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.fivebetserio.util.Constants;

import java.util.List;
import java.util.Objects;

@Entity
public class League {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long uid;

    private String key;
    private String group;
    private String title;
    private String description;
    private boolean active;
    private boolean has_outrights;
    private boolean liked = false;

    public League() {}

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

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public static void filterLeagues(List<League> leagueList) {
        for (int i = 0; i < leagueList.size(); i++) {
            if (leagueList.get(i).getTitle().equals(Constants.REMOVED_ARTICLE_TITLE)) {
                leagueList.remove(i);
                i--;
            }
        }
    }

    public static League getSampleLeague() {
        League sample = new League();
        sample.setTitle("Not so long title sample");
        return sample;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        League league = (League) o;
        return active == league.active &&
                has_outrights == league.has_outrights &&
                Objects.equals(key, league.key) &&
                Objects.equals(group, league.group) &&
                Objects.equals(title, league.title) &&
                Objects.equals(description, league.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, group, title, description, active, has_outrights);
    }

    public static Long findLeagueUidByKey(String leagueKey, List<League> leaguesList) {
        for (League league : leaguesList) {
            if (league.getKey().equals(leagueKey)) {
                return league.getUid();
            }
        }
        return null; // Nessuna corrispondenza trovata
    }

}
