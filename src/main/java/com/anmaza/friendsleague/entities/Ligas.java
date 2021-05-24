package com.anmaza.friendsleague.entities;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class Ligas {

    private String nombre;
    private String numero;
    private ArrayList<LigaUser> usuarios;
    private String admin;
    private String game;
    private String usersText;
    private String key;

    public Ligas(){}

    public Ligas(String nombre, String numero, ArrayList<LigaUser> usuarios, String admin, String game){
        this.nombre = nombre;
        this.numero = numero;
        this.usuarios = usuarios;
        this.admin = admin;
        this.game = game;
    }

    public Ligas(String misligas) {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public ArrayList<LigaUser> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(ArrayList<LigaUser> usuarios) {
        this.usuarios = usuarios;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getUsersText() {
        return usersText;
    }

    public void setUsersText(String usersText) {
        this.usersText = usersText;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
