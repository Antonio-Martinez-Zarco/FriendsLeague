package com.anmaza.friendsleague.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.anmaza.friendsleague.R;
import com.anmaza.friendsleague.TableActivity;
import com.anmaza.friendsleague.entities.LigaUser;
import com.anmaza.friendsleague.entities.Ligas;

import java.util.ArrayList;

public class AdapterTable extends RecyclerView.Adapter<AdapterTable.ViewHolder> {

    private ArrayList<LigaUser> ligasUser;
    Context context;

    public AdapterTable(ArrayList<LigaUser> ligasUser, Context context) {
        this.ligasUser = ligasUser;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterTable.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);
        return new AdapterTable.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTable.ViewHolder holder, int position) {

        LigaUser ligas = ligasUser.get(position);
        holder.playerName.setText(ligas.getUsername());
        holder.playerVictories.setText(String.valueOf(ligas.getVictories()));
        holder.playerTies.setText(String.valueOf(ligas.getTie()));
        holder.playerDefeats.setText(String.valueOf(ligas.getDefeats()));

    }

    @Override
    public int getItemCount() {
        return ligasUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView playerName, playerVictories, playerDefeats, playerTies;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            playerName = itemView.findViewById(R.id.txt_player_name);
            playerVictories = itemView.findViewById(R.id.txt_player_victories);
            playerTies = itemView.findViewById(R.id.txt_player_ties);
            playerDefeats = itemView.findViewById(R.id.txt_player_defeats);

        }
    }
}


