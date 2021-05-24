package com.anmaza.friendsleague;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.anmaza.friendsleague.adapter.AdapterGame;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button login;
    private TextInputEditText username, password;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.btn_login);
        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        auth = FirebaseAuth.getInstance();

        login();
    }

    //Método para iniciar sesión
    public void login(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sign_username = username.getText().toString();
                String sign_password = password.getText().toString();
                if (sign_username.isEmpty() || sign_password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Los campos están vacíos", Toast.LENGTH_SHORT).show();
                }

                try{
                    auth.signInWithEmailAndPassword(sign_username,sign_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Usuario correcto!", Toast.LENGTH_SHORT).show();
                                goActivity();
                            }else{
                                Toast.makeText(MainActivity.this, "Usuario inválido", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    //Ir a la actividad de registro
    public void goRegister(View view){
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

    //Ir a la pantalla principal
    public void goActivity(){
        Intent i = new Intent(this, VideogamesActivity.class);
        startActivity(i);
        finish();
    }

}