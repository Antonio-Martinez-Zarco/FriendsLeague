package com.anmaza.friendsleague.entities;

import java.io.Serializable;

public class Estadios implements Serializable {

    public String name;
    public int image;
    public String equipo;

    public Estadios(String name, int image, String equipo){
        this.name = name;
        this.image = image;
        this.equipo = equipo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }
}
