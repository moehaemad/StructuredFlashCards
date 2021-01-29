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
        createRequest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                TextView networkText = findViewById(R.id.user_menu_network_result);
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url ="https://jsonplaceholder.typicode.com/todos/1";

                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("asdasd", "ASd");

                                TextView putNetResponse = findViewById(R.id.user_menu_network_result);
                                try{
                                    String userId = response.getString("userId");
                                    putNetResponse.setText(userId);
                                }catch(JSONException e){
                                    Log.e("error in json", e.getMessage());
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("asdASD","Asdasd");
                            }
                });


                queue.add(jsonRequest);


            }
        });
    }
}
