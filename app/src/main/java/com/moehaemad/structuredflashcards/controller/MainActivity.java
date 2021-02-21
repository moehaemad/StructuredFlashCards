package com.moehaemad.structuredflashcards.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;
import com.moehaemad.structuredflashcards.R;
import com.moehaemad.structuredflashcards.ui.DeckFragment;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //Use app bar configuration for the navigation drawer
    private AppBarConfiguration topBarConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get Top toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get navigation drawer layout
        DrawerLayout drawerLayout = findViewById(R.id.main_nav_drawer);

        //Get the navigation View in the navigation drawer
        NavigationView navView = findViewById(R.id.main_nav_view);

        //setup the navigation controller for the fragment started the navigation graph
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                                                                    .findFragmentById(R.id.fragment_host);

        NavController navController = navHostFragment.getNavController();

        //setup the top bar configuration with the nav controller graph and set the drawer layout
        this.topBarConfig = new  AppBarConfiguration.Builder(
                R.id.startContent, R.id.createCardFragment, R.id.loginFragment, R.id.checkDeckList)
                                    .setDrawerLayout(drawerLayout)
                                    .build();

        // setup the navigationUI for the navigation drawer using navigation view and nav controller
        NavigationUI.setupActionBarWithNavController(this, navController, this.topBarConfig);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setup buttons
        Button startDeck = findViewById(R.id.main_start_deck);
        startDeck.setOnClickListener(this);
        Button loginView = findViewById(R.id.main_login);
        loginView.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragment_host);
        return NavigationUI.navigateUp(navController, this.topBarConfig) || super.onSupportNavigateUp();
    }



    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        NavHostController navController = (NavHostController) Navigation.findNavController(this, R.id.fragment_host);
        switch(viewId){
            case R.id.main_start_deck:
                navController.navigate(R.id.createCardFragment);
                break;
            case R.id.main_login:
                navController.navigate(R.id.loginFragment);
                break;
        }
    }
}
