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

import java.util.LinkedList;

public class Deck {
    private String username;
    private JSONArray deckArray;
    private SharedPreferences sharedPreferences;
    private Context ctx;

    /**
     * Create deck from context without specifying username in case it's presumed to be set.
     * */
    public Deck(Context ctx){
        this.sharedPreferences = ctx.getSharedPreferences(Preferences.PACKAGE, Context.MODE_PRIVATE);
        this.ctx = ctx;
        //set the class variable to include these ids
        setDeck();
    }

    /**
    * Create deck and synchronise the shared preferences in case changes to ids have been made.
    * */
    public Deck(@NonNull Context ctx, @NonNull  String username){
        this(ctx);
        this.username = username;
        //make a network request to get the ids
        syncDeck();
    }

    /**
     * Class used for a generic network request. Intead of anonymous class, this inner class sets
     *  the shared preferences .
     * */
    class GetDeckResponse implements Response.Listener<JSONObject>{
        @Override

        /**
         * Handle the response from the network request.
         * */
        public void onResponse(JSONObject response) {
            //open shared preferences editor
            SharedPreferences.Editor prefEditor = sharedPreferences.edit();
            try{
                // add response as string from json array into shared preferences
                prefEditor.putString(Preferences.DECK_ARRAY, response.get("ids").toString());
                Log.d("Deck ids", response.get("ids").toString());
                Log.d("deckArray", deckArray.toString());
            }catch(JSONException e){
                Log.e("Deck json error", e.getMessage());
            }
            //apply changed from the editor
            prefEditor.apply();
        }
    }

    /**
     * Generic Error response.
     * */
    private Response.ErrorListener error = new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("Network error", "Deck.java get ids");
        }
    };


    /**
     * Create a network request to get the ids from the API.
     * */
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
    /**
     * Set the deck Id's from shared preferences into class variable.
     * */
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

    /**
     * Grab the ids from class property
     * */
    public JSONArray getDeckIds(){
        return this.deckArray == null ? new JSONArray() : this.deckArray;
    };

    /**
     * Returns the deck ids from the network request as strings instead of JSONArray to avoid parsing
     *  the JSONArray in UI.
     * */
    public LinkedList<String> getDeckIdsAsString(){
        //create empty list
        LinkedList<String> toReturn = new LinkedList<>();
        //get deck ids from network request as JSONArray
        JSONArray jsonIds = this.getDeckIds();
        //check if null
        if (jsonIds == null) return toReturn;
        try{
            for (int i=0, size=jsonIds.length(); i < size; i++){
                //grab the object each item of the array holds
                JSONObject jsonObject = jsonIds.getJSONObject(i);
                //add the object's id property as a string into the returned list
                toReturn.add(jsonObject.getString("id"));
            }
            //return list
            return toReturn;
        }catch (JSONException e){
            Log.e("Deck getDeckIdsAsString", e.getMessage());
        }
        //used for convenience but function will not enter this stage because of null check.
        return toReturn;
    }

}
