package com.moehaemad.structuredflashcards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected View.OnClickListener startingDeck = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent startDeck = new Intent(Intent.ACTION_VIEW);
            startDeck.setClass(getApplicationContext(), DeckMenu.class);

            startActivity(startDeck);
        }
    };

    protected View.OnClickListener startingUserView = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent startDeck = new Intent(Intent.ACTION_VIEW);
            startDeck.setClass(getApplicationContext(), DeckMenu.class);

            startActivity(startDeck);
        }
    };

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
                changeView(UserMenu.class);
                break;
        }
    }
}
