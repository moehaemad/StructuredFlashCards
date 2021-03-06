package com.moehaemad.structuredflashcards.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.moehaemad.structuredflashcards.model.NetworkRequest;
import com.moehaemad.structuredflashcards.model.Preferences;
import com.moehaemad.structuredflashcards.model.WebsiteInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Deck {
    private String username;
    private JSONArray deckArray;
    private SharedPreferences sharedPreferences;
    private Context ctx;

    public Deck(Context ctx){
        this.sharedPreferences = ctx.getSharedPreferences(Preferences.PACKAGE, Context.MODE_PRIVATE);
        this.ctx = ctx;
        //set the class variable to include these ids
        setDeck();
    }

    public Deck(@NonNull Context ctx, @NonNull  String username){
        this(ctx);
        this.username = username;
        //make a network request to get the ids
        syncDeck();
    }

    class GetDeckResponse implements Response.Listener<JSONObject>{
        @Override
        public void onResponse(JSONObject response) {
            SharedPreferences.Editor prefEditor = sharedPreferences.edit();
            Log.d("Deck response", response.toString());
            //TODO: put the list of deck id's in here
            //TODO: check the output received as JSON request
            try{
                prefEditor.putString(Preferences.DECK_ARRAY, response.get("ids").toString());
                Log.d("Deck ids", response.get("ids").toString());
                Log.d("deckArray", deckArray.toString());
            }catch(JSONException e){
                Log.e("Deck json error", e.getMessage());
            }
            prefEditor.apply();
        }
    }

    private Response.ErrorListener error = new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("Network error", "Deck.java get ids");
        }
    };


    public void syncDeck(){
        //create network request for GET of the ids
        NetworkRequest networkRequest = new NetworkRequest(this.ctx);
        int method = networkRequest.getMethod("GET");

        //construct JSONobjectrequest for queue
        networkRequest.addToRequestQueue(new JsonObjectRequest(
                method,
                WebsiteInterface.GET_DECKS + this.username,
                null,
                new GetDeckResponse(),
                error));
    }

    private void setDeck(){
        //get deck ids from shared preferences
        String decks = this.sharedPreferences.getString(Preferences.DECK_ARRAY, "[]");
        //if none exist set the deck ids for this user to be empty
        if (decks.equals("[]")){
            this.deckArray = new JSONArray();
        }else{
            //if deck ids exist set the deck ids accordingly
            try {
                this.deckArray = new JSONArray(decks);
            } catch (JSONException e) {
                Log.e("setting deck ids", e.getMessage());
            }
        }
    }


    public JSONArray getDeckIds(){
        return this.deckArray == null ? new JSONArray() : this.deckArray;
    };

}
