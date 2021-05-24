package com.anmaza.friendsleague.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.anmaza.friendsleague.R;
import com.anmaza.friendsleague.TableActivity;
import com.anmaza.friendsleague.VideogamesActivity;
import com.anmaza.friendsleague.entities.LigaUser;
import com.anmaza.friendsleague.entities.Ligas;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AdapterLeague extends RecyclerView.Adapter<AdapterLeague.ViewHolder> {

    private ArrayList<Ligas> ligasList;
    Context context;

    public AdapterLeague(ArrayList<Ligas> ligasList, Context context){
        this.ligasList = ligasList;
        this.context = context;
    }
    @NonNull
    @Override
    public AdapterLeague.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leagues, parent, false);
        return new AdapterLeague.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull AdapterLeague.ViewHolder holder, int position) {

        Ligas ligas = ligasList.get(position);
        holder.txt_league.setText(ligas.getNombre());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, TableActivity.class);
                i.putExtra("nameleague", ligas.getNombre());
                i.putExtra("usersList", ligas.getUsersText());
                i.putExtra("key", ligas.getKey());
                context.startActivity(i);
                VideogamesActivity.videoActivity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return ligasList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView txt_league;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_view);
            txt_league = itemView.findViewById(R.id.txt_league);

        }
    }
}
