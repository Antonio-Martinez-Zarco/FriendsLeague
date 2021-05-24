
package com.anmaza.friendsleague;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.anmaza.friendsleague.adapter.AdapterTable;
import com.anmaza.friendsleague.entities.LigaUser;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class TableActivity extends AppCompatActivity {

    private DatabaseReference reference;
    private TextView name;
    private String data;
    private TextInputEditText jugador1, jugador2, resultado1, resultado2;
    private TextInputLayout player1, player2, result1, result2;
    private ArrayList<LigaUser> listUsers;
    private Button delete;
    private RecyclerView recyclerView;
    private AdapterTable adapterTable;
    private LinearLayout header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        reference = FirebaseDatabase.getInstance().getReference();
        delete = findViewById(R.id.delete_league);

        //Variables añadir resultado
        jugador1 = findViewById(R.id.edit_player_one);
        jugador2 = findViewById(R.id.edit_player_two);
        resultado1 = findViewById(R.id.edit_res_one);
        resultado2 = findViewById(R.id.edit_res_two);

        //Inputs
        player1 = findViewById(R.id.layout_player_one);
        player2 = findViewById(R.id.layout_player_two);
        result1 = findViewById(R.id.layout_res_one);
        result2 = findViewById(R.id.layout_res_two);

        //Recycler
        recyclerView = findViewById(R.id.recycler_table);

        //Header
        header = findViewById(R.id.layout_header_table);

        name = findViewById(R.id.name_league);
        data = getIntent().getExtras().getString("nameleague");
        name.setText("Liga " + data);

        //Get arrayList
        Gson gson = new Gson();
        listUsers = gson.fromJson(getIntent().getStringExtra("usersList"), new TypeToken<ArrayList<LigaUser>>() {
        }.getType());

        if (!listUsers.isEmpty()) {
            header.setVisibility(View.VISIBLE);
        }

        adapterTable = new AdapterTable(listUsers, this);
        recyclerView.setAdapter(adapterTable);

        //Eliminar liga con alert dialog
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View mDialogView = LayoutInflater.from(TableActivity.this).inflate(R.layout.popup_general, null);
                Button btn_yes = mDialogView.findViewById(R.id.btn_yes);
                Button btn_no = mDialogView.findViewById(R.id.btn_no);
                TextView txt_title = mDialogView.findViewById(R.id.txt_welcome);
                TextView txt_info = mDialogView.findViewById(R.id.txt_information);

                txt_title.setText(R.string.delete_league_popup);
                txt_info.setText(R.string.text_league_delete);

                AlertDialog.Builder builder = new AlertDialog.Builder(TableActivity.this);
                builder.setView(mDialogView).setCancelable(false);
                AlertDialog showAlert = builder.show();

                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteLeague();
                        finish();
                        Intent i = new Intent(TableActivity.this, VideogamesActivity.class);
                        startActivity(i);
                        showAlert.dismiss();
                    }
                });

                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAlert.dismiss();
                    }
                });

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            case R.id.icon_help:
                recuerda();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void deleteLeague() {

        String key = getIntent().getExtras().getString("key");
        reference.child("Ligas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (dataSnapshot.getKey().equals(key)) {
                        reference.child("Ligas").child(dataSnapshot.getKey()).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addResults(View v) {

        if (jugador1.getText().toString().isEmpty() || jugador2.getText().toString().isEmpty() || resultado1.getText().toString().isEmpty() || resultado2.getText().toString().isEmpty()) {
            Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
        } else {

            String jug1 = jugador1.getText().toString();
            String jug2 = jugador2.getText().toString();
            int res1 = Integer.parseInt(resultado1.getText().toString());
            int res2 = Integer.parseInt(resultado2.getText().toString());

            if (jug1.equals(jug2)) {
                Toast.makeText(this, "No se puede añadir dos resultados al mismo jugador", Toast.LENGTH_SHORT).show();
            } else {
                refreshTable(jug1, jug2, res1, res2);
            }
        }

    }

    private void refreshTable(String jug1, String jug2, int res1, int res2) {

        final boolean[] foundPlayerOne = {false};
        final boolean[] foundPlayerTwo = {false};
        final boolean[] updated = {false};

        reference.child("Ligas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!updated[0]) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String keyMain = getIntent().getExtras().getString("key");
                        if (dataSnapshot.getKey().equals(keyMain)) {
                            String keyjug1 = "", keyjug2 = "";
                            for (DataSnapshot item : dataSnapshot.child("usuarios").getChildren()) {

                                if (item.child("username").getValue().equals(jug1)) {
                                    keyjug1 = item.getKey();
                                    foundPlayerOne[0] = true;
                                }

                                if (item.child("username").getValue().equals(jug2)) {
                                    keyjug2 = item.getKey();
                                    foundPlayerTwo[0] = true;
                                }

                            }

                            if (!foundPlayerOne[0] || !foundPlayerTwo[0]) {

                                if (!foundPlayerOne[0]) {
                                }

                                if (!foundPlayerTwo[0]) {
                                }

                            } else {

                                for (DataSnapshot item : dataSnapshot.child("usuarios").getChildren()) {

                                    if (item.child("username").getValue().equals(jug1)) {
                                        updated[0] = true;
                                        if (res1 > res2) {
                                            int vic = Integer.parseInt(item.child("victories").getValue().toString());
                                            reference.child("Ligas").child(keyMain).child("usuarios").child(keyjug1).child("victories").setValue(vic + 1);
                                        } else if (res1 < res2) {
                                            int der = Integer.parseInt(item.child("defeats").getValue().toString());
                                            reference.child("Ligas").child(keyMain).child("usuarios").child(keyjug1).child("defeats").setValue(der + 1);
                                        } else if (res1 == res2) {
                                            int tie = Integer.parseInt(item.child("tie").getValue().toString());
                                            reference.child("Ligas").child(keyMain).child("usuarios").child(keyjug1).child("tie").setValue(tie + 1);
                                        }

                                    }

                                    if (item.child("username").getValue().equals(jug2)) {
                                        updated[0] = true;
                                        if (res2 > res1) {
                                            int vic = Integer.parseInt(item.child("victories").getValue().toString());
                                            reference.child("Ligas").child(keyMain).child("usuarios").child(keyjug2).child("victories").setValue(vic + 1);
                                        } else if (res2 < res1) {
                                            int der = Integer.parseInt(item.child("defeats").getValue().toString());
                                            reference.child("Ligas").child(keyMain).child("usuarios").child(keyjug2).child("defeats").setValue(der + 1);
                                        } else if (res1 == res2) {
                                            int tie = Integer.parseInt(item.child("tie").getValue().toString());
                                            reference.child("Ligas").child(keyMain).child("usuarios").child(keyjug2).child("tie").setValue(tie + 1);
                                        }
                                    }
                                }

                                jugador1.setText("");
                                jugador2.setText("");
                                resultado1.setText("");
                                resultado2.setText("");
                                updateRecords();
                            }
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void updateRecords() {

        reference.child("Ligas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    String keyMain = getIntent().getExtras().getString("key");

                    if (dataSnapshot.getKey().equals(keyMain)) {

                        ArrayList<LigaUser> array = new ArrayList<>();

                        for (DataSnapshot item : dataSnapshot.child("usuarios").getChildren()) {

                            array.add(new LigaUser(item.child("username").getValue().toString(),
                                    Integer.parseInt(item.child("victories").getValue().toString()),
                                    Integer.parseInt(item.child("defeats").getValue().toString()),
                                    Integer.parseInt(item.child("tie").getValue().toString())));

                        }

                        adapterTable = new AdapterTable(array, getApplicationContext());
                        recyclerView.setAdapter(adapterTable);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, VideogamesActivity.class);
        i.putExtra("fromTableActivity", true);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.table_menu, menu);
        return true;
    }

    private void recuerda(){

        View mDialogView = LayoutInflater.from(TableActivity.this).inflate(R.layout.popup_general, null);
        Button btn_yes = mDialogView.findViewById(R.id.btn_yes);
        Button btn_no = mDialogView.findViewById(R.id.btn_no);
        TextView txt_title = mDialogView.findViewById(R.id.txt_welcome);
        TextView txt_info = mDialogView.findViewById(R.id.txt_information);

        txt_title.setText("¡Recuerda!");
        txt_info.setText("El nombre de los jugadores debe ser idéntico al nombre de la tabla.");
        btn_no.setVisibility(View.INVISIBLE);
        btn_yes.setText("Entendido");

        AlertDialog.Builder builder = new AlertDialog.Builder(TableActivity.this);
        builder.setView(mDialogView).setCancelable(false);
        AlertDialog showAlert = builder.show();

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert.dismiss();
            }
        });

    }
}

