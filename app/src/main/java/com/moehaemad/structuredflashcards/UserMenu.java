package com.moehaemad.structuredflashcards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Button createRequest = findViewById(R.id.user_menu_network);
        createRequest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FlashCard newRequest = new FlashCard(getApplicationContext());
                newRequest.getRequest("https://jsonplaceholder.typicode.com/todos/1");
                

            }
        });
    }
}
