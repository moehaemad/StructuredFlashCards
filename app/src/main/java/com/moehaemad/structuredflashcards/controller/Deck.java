package com.moehaemad.structuredflashcards.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.moehaemad.structuredflashcards.model.NetworkRequest;
import com.moehaemad.structuredflashcards.model.Preferences;
import com.moehaemad.structuredflashcards.model.WebsiteInterface;

import org.json.JSONObject;

import java.util.HashMap;

public class Deck {
    private String username;
    private int[] deckIds;
    private HashMap<Integer, String> deck;
    private SharedPreferences sharedPreferences;
    private Context ctx;

    public Deck(Context ctx){
        this.sharedPreferences = ctx.getSharedPreferences(Preferences.PACKAGE, Context.MODE_PRIVATE);
        this.ctx = ctx;
    }

    public Deck(@NonNull Context ctx, @NonNull  String username){
        this(ctx);
        this.username = username;
        this.deckIds = getDeckIds();
        pairDeckIdNames();
    }

    class GetDeckResponse implements Response.Listener<JSONObject>{
        @Override
        public void onResponse(JSONObject response) {
            SharedPreferences.Editor prefEditor = sharedPreferences.edit();
            Log.d("Deck response", response.toString());
            //TODO: put the list of deck id's in here
            //TODO: check the output received as JSON request
            prefEditor.apply();
        }
    }

    private Response.ErrorListener error = new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("Network error", "Deck.java get ids");
        }
    };


    private int[] getDeckIds(){
    //    TODO: query database for array of deck ids attached to a user using user id
    //    TODO: create networkRequest object and retrieve the json data which will contain ids
        //create network request for GEt
        NetworkRequest networkRequest = new NetworkRequest(this.ctx);
        int method = networkRequest.getMethod("GET");

        //construct JSONobjectrequest for queue
        networkRequest.addToRequestQueue(new JsonObjectRequest(
                method,
                WebsiteInterface.GET_DECKS + this.username,
                null,
                new GetDeckResponse(),
                error));
        int[] ids = {};
        return ids;
    };



    private void pairDeckIdNames(){
        //TODO: for each item in the deck id arrays attach the associated name to the hashmap
        //TODO: take the data from getDeckIds and associate them to a HasMap 'deck' object
        //TODO: consider reading from bundle or something that can be retained without preferences
        String websiteResponse = this.sharedPreferences.getString("id", "[]");
        //turn the string into an array for deck ids
        String[] ids = websiteResponse.split(", ");

    }

    protected HashMap<Integer, String> getDecks(){
        //TODO: pair each deck id to it's associated string
        return this.deck;
    }


}
