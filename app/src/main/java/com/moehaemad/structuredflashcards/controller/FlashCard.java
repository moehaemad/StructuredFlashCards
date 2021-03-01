package com.moehaemad.structuredflashcards.controller;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.moehaemad.structuredflashcards.model.NetworkRequest;
import com.moehaemad.structuredflashcards.model.Preferences;
import com.moehaemad.structuredflashcards.model.WebsiteInterface;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class FlashCard{
    private static Context appContext;
    private int id;
    private String question;
    private String answer;
    private SharedPreferences sharedPreferences;
    private final String prefName = Preferences.PACKAGE;

    public FlashCard(@NonNull Context appContext, @NonNull int id){
        this.id=id;
        this.question = "";
        this.answer = "";
        this.appContext = appContext;
        this.sharedPreferences = appContext.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        setupCards();
    }

    public FlashCard (Context appContext, int id, String question, String answer){
        this(appContext, id);
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

    public interface Card {
        HashMap< String, String> fullCard = new HashMap<String, String>();

        public void setMethod();

        public String getFullCard();
    }
    class CardResponse implements Response.Listener{
        @Override
        public void onResponse(Object response) {
            SharedPreferences.Editor prefEditor = sharedPreferences.edit();
            Log.d("FlashCard resp", response.toString());
            prefEditor.apply();
        }
    }



    private Response.ErrorListener error = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("FlashCard", error.getMessage());
        }
    };

    public String getFront(int id){
        return this.question;
    }

    public String getBack(int id){
        return this.answer;
    }

    private void setupCards(){
        NetworkRequest networkRequest = new NetworkRequest(this.appContext);
        String website = WebsiteInterface.GET_CARDS + String.valueOf(this.id);

        networkRequest.addToRequestQueue(new JsonObjectRequest(
                website,
                null,
                new CardResponse(),
                error));
    }



}