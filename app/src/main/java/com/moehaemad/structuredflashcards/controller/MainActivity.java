package com.moehaemad.structuredflashcards.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;
import com.moehaemad.structuredflashcards.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //Use app bar configuration for the navigation drawer
    private AppBarConfiguration topBarConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get Top toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);

        // get navigation drawer layout
        DrawerLayout drawerLayout = findViewById(R.id.main_nav_drawer);

        //Get the navigation View in the navigation drawer
        NavigationView navView = findViewById(R.id.main_nav_view);
/*
        //setup the navigation controller for the fragment started the navigation graph
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                                                                    .findFragmentById(R.id.start_fragment);
        NavController navController =  navHostFragment.getNavController();*/

        NavController navController = Navigation.findNavController(this, R.id.start_fragment);
        //setup the top bar configuration with the nav controller graph and set the drawer layout
        this.topBarConfig = new  AppBarConfiguration.Builder()
                                    .setDrawerLayout(drawerLayout).build();

        // setup the navigationUI for the navigation drawer using navigation view and nav controller
        NavigationUI.setupActionBarWithNavController(this, navController, this.topBarConfig);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Button startDeck = findViewById(R.id.main_start_deck);
        startDeck.setOnClickListener(this);
        Button loginView = findViewById(R.id.main_login);
        loginView.setOnClickListener(this);
    }

    public void changeView(Class toStart){
        Intent toStartView = new Intent (Intent.ACTION_VIEW);
        toStartView.setClass(getApplicationContext(), toStart);
        startActivity(toStartView);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.main_start_deck:
                changeView(DeckMenu.class);
                break;
            case R.id.main_login:
                changeView(LoginMenu.class);
                break;
        }
    }
}
