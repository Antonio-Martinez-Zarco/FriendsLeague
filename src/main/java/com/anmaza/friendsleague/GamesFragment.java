package com.anmaza.friendsleague;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anmaza.friendsleague.adapter.AdapterGame;
import com.anmaza.friendsleague.entities.Games;

import java.util.ArrayList;
import java.util.List;

public class GamesFragment extends Fragment {

    private View v;
    private RecyclerView recyclerView;
    AdapterGame adapterGame;
    List<Games> gamesList = new ArrayList<>();

    public GamesFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.fragment_games, null);

        recyclerView = v.findViewById(R.id.recycler_games);
        adapterGame = new AdapterGame(gamesList, getContext());
        recyclerView.setAdapter(adapterGame);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gamesList = new ArrayList<>();
        gamesList.add(new Games("Fifa 21", R.drawable.fifa21));
        gamesList.add(new Games("Nba 2k21", R.drawable.nba2k21));
        gamesList.add(new Games("Rocket League", R.drawable.rocketleague));
    }
}
