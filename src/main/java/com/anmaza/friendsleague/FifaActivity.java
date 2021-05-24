package com.anmaza.friendsleague;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anmaza.friendsleague.entities.LigaUser;
import com.anmaza.friendsleague.entities.Ligas;
import com.anmaza.friendsleague.entities.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FifaActivity extends AppCompatActivity {

    private Button addusers, createliga;
    private TextInputEditText nombre, numero, nombres;
    private ArrayList<LigaUser> usuarios;
    private Task<Void> ref;
    private DatabaseReference reference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifa);

        createliga = findViewById(R.id.btn_createleague);
        addusers = findViewById(R.id.btn_addusers);
        nombre = findViewById(R.id.et_league);
        numero = findViewById(R.id.et_users);
        nombres = findViewById(R.id.et_allusers);
        usuarios = new ArrayList<>();

        //go back activity with back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Método para crear la liga
        createliga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameleague = nombre.getText().toString();
                String numberof = numero.getText().toString();
                String userCurrent = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String currentGame = "Fifa21";

                if (!nameleague.isEmpty() && !numberof.isEmpty()) {

                    if (usuarios.size() == Integer.parseInt(numberof)) {

                        if (usuarios.size() >= 2) {

                            Ligas ligas = new Ligas(nameleague, numberof, usuarios, userCurrent, currentGame);
                            ref = FirebaseDatabase.getInstance().getReference("Ligas")
                                    .push()
                                    .setValue(ligas).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(FifaActivity.this, "Liga Creada!!!", Toast.LENGTH_SHORT).show();
                                                usuarios.clear();
                                                finish();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(FifaActivity.this, "El nº de participantes debe ser mayor de 2", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(FifaActivity.this, "El nº de participantes no coincide", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(FifaActivity.this, "Debes rellenar los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void addUsers(View v) {
        String name = nombres.getText().toString();
        LigaUser ligaUser = new LigaUser(name, 0, 0, 0);

        boolean nameEquals = false;

        if (!name.isEmpty()) {

            for (LigaUser ligauser : usuarios) {
                if (ligauser.username.equals(name)) {
                    nameEquals = true;
                }
            }

            if (!nameEquals) {
                usuarios.add(ligaUser);
                Toast.makeText(this, "Usuario " + name + " añadido!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(FifaActivity.this, "El usuario no puede repetirse", Toast.LENGTH_SHORT).show();
            }
            nombres.setText("");

        } else {
            Toast.makeText(FifaActivity.this, "Debes rellenar antes el campo nombre", Toast.LENGTH_SHORT).show();
        }
    }
}
