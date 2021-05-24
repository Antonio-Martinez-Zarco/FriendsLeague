package com.anmaza.friendsleague;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class VideogamesActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static VideogamesActivity videoActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videogames);

        videoActivity = this;

        BottomNavigationView navigation = findViewById(R.id.navigationBar);
        navigation.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) this);

        boolean fromTable = getIntent().getBooleanExtra("fromTableActivity", false);

        if (fromTable){
            loadFragment(new LeagueFragment());
            navigation.setSelectedItemId(R.id.action_leagues);
        }else{
            loadFragment(new GamesFragment());
            navigation.setSelectedItemId(R.id.action_videogame);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.action_person:
                fragment = new ProfileFragment();
                break;
            case R.id.action_videogame:
                fragment = new GamesFragment();
                break;
            case R.id.action_leagues:
                fragment = new LeagueFragment();
                break;
        }
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}