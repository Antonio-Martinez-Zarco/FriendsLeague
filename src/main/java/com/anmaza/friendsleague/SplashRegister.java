package com.anmaza.friendsleague;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_register);

        //Genero un splash a la hora de registrarse o logearse, asi de esta manera esperamos a entrar en la app

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashRegister.this, VideogamesActivity.class);
                startActivity(i);
                finish();
            }
        },2000);
    }
}