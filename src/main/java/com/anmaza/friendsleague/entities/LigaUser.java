package com.anmaza.friendsleague.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LigaUser implements Serializable {

    @SerializedName("username")
    public String username;
    @SerializedName("victories")
    public int victories;
    @SerializedName("defeats")
    public int defeats;
    @SerializedName("tie")
    public int tie;

    public LigaUser(){}

    public LigaUser(String username, int victories, int defeats, int tie){
        this.username = username;
        this.victories = victories;
        this.defeats = defeats;
        this.tie = tie;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getVictories() {
        return victories;
    }

    public void setVictories(int victories) {
        this.victories = victories;
    }

    public int getDefeats() {
        return defeats;
    }

    public void setDefeats(int defeats) {
        this.defeats = defeats;
    }

    public int getTie() {
        return tie;
    }

    public void setTie(int tie) {
        this.tie = tie;
    }
}
