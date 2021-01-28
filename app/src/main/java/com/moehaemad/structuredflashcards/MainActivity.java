package com.moehaemad.structuredflashcards;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

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

    @Override
    protected void onResume() {
        super.onResume();
        Button startDeck = findViewById(R.id.start_deck);
        startDeck.setOnClickListener(startingDeck);
    }

}
