package com.anmaza.friendsleague;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anmaza.friendsleague.adapter.AdapterGame;
import com.anmaza.friendsleague.adapter.AdapterLeague;
import com.anmaza.friendsleague.entities.Games;
import com.anmaza.friendsleague.entities.LigaUser;
import com.anmaza.friendsleague.entities.Ligas;
import com.anmaza.friendsleague.utils.UsersCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class LeagueFragment extends Fragment{

    private View v;
    private RecyclerView recyclerView;
    AdapterLeague adapterLeague;
    ArrayList<Ligas> ligasList = new ArrayList<>();
    private DatabaseReference reference;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String id = user.getUid();
    private String admin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_league, null);
        recyclerView = v.findViewById(R.id.recycler_leagues);

        getAdminId(new UsersCallback() {
            @Override
            public void onCallback(String admin) {
                getLiga(admin);
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void getLiga(String admin) {
        if (id.equals(admin)) {

            reference.child("Ligas").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d("TAG", "Actualizo");
                    for (DataSnapshot data : snapshot.getChildren()) {

                        if (data.child("admin").getValue().toString().equals(admin)) {
                            Ligas liga = new Ligas();
                            String nameleague = data.child("nombre").getValue().toString();
                            String participantes = data.child("numero").getValue().toString();
                            String users = data.child("usuarios").getValue().toString();
                            String key = data.getKey();
                            liga.setUsersText(users);
                            liga.setNombre(nameleague);
                            liga.setNumero(participantes);
                            liga.setKey(key);

                            ligasList.add(liga);
                        }

                    }

                    adapterLeague = new AdapterLeague(ligasList, getContext());
                    recyclerView.setAdapter(adapterLeague);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    private void getAdminId(final UsersCallback callback) {
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Ligas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (id.equals(dataSnapshot.child("admin").getValue())) {
                        admin = String.valueOf(dataSnapshot.child("admin").getValue());
                    }
                }
                callback.onCallback(admin);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}
