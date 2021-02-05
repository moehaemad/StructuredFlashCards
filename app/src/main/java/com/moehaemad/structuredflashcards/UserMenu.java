package com.moehaemad.structuredflashcards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

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
        createRequest.setOnClickListener(checkUser);
    }

    protected View.OnClickListener checkUser = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            FlashCard getUser = new FlashCard(getApplicationContext());
            getUser.setMethod("GET");
            getUser.getRequest("https://moehaemad.ca/structuredFlashCards/checkUser/abc/abc",
                    new UserRequests<JSONObject>() {
                        @Override
                        public void getResult(JSONObject object) {
                            TextView result = findViewById(R.id.user_menu_network_result);
                            result.setText("Received JSON Object");
                        }
                    });
            //TODO: change view according to http request
            // TextView networkResult = findViewById(R.id.user_menu_network_result);
            // networkResult.setText("Something happened");
        }
    };
}
