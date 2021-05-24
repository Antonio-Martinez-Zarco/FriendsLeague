package com.anmaza.friendsleague.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.anmaza.friendsleague.FifaActivity;
import com.anmaza.friendsleague.NbaActivity;
import com.anmaza.friendsleague.R;
import com.anmaza.friendsleague.RocketActivity;
import com.anmaza.friendsleague.entities.Games;

import java.util.ArrayList;
import java.util.List;

public class AdapterGame extends RecyclerView.Adapter<AdapterGame.ViewHolder>{

    private List<Games> gamesList;
    Context context;

    public AdapterGame(List<Games> gamesList, Context context){
        this.context = context;
        this.gamesList = gamesList;
    }

    @NonNull
    @Override
    public AdapterGame.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_games, parent, false);
        return new AdapterGame.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterGame.ViewHolder holder, int position) {
        holder.txt_name.setText(gamesList.get(position).getName());
        holder.img_game.setImageResource(gamesList.get(position).getImage());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position){
                    case 0:
                        Intent intent=new Intent(context, FifaActivity.class);
                        context.startActivity(intent);
                        break;
                    case 1:
                        Intent intent2=new Intent(context, NbaActivity.class);
                        context.startActivity(intent2);
                        break;
                    case 2:
                        Intent intent3=new Intent(context, RocketActivity.class);
                        context.startActivity(intent3);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return gamesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView txt_name;
        ImageView img_game;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            txt_name = itemView.findViewById(R.id.txt_game);
            img_game = itemView.findViewById(R.id.img_game);
        }
    }

}
