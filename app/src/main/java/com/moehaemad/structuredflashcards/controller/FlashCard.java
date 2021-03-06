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
import com.moehaemad.structuredflashcards.ui.DeckFragment;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

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

    public FlashCard (Context appContext, @NonNull int id, @NonNull String question, @NonNull String answer){
        this(appContext, id);
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

    private Response.Listener<JSONObject> storeWebCards = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {
                changePreferences(
                        Preferences.CARDS_ARRAY + "_" + String.valueOf(id),
                        response.get("cards").toString()
                );
                Log.d("FlashCard storeWebCards", response.toString());

            } catch (JSONException e) {
                Log.e("flashcard insertcards", e.getMessage());
            }
        }
    };

    private void changePreferences (String prefItem, String prefValue){
        //TODO: get previous value of sharedpreferences and append
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        //store the cards array and append the deck id
        prefEditor.putString(prefItem, prefValue);
        prefEditor.apply();
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

    public void createCard(final DeckFragment.Verified verification){
        //will not be able to create without instantiating constructor with question/answer values
        Response.Listener<JSONObject> createCard = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    verification.result(response.getBoolean("result"));
                } catch (JSONException e) {
                    Log.e("Flash Card createCard", e.getMessage());
                }
            }
        };
        NetworkRequest networkRequest = new NetworkRequest(this.appContext);
        String api = WebsiteInterface.CREATE_CARD;
        int method = networkRequest.getMethod("POST");
        try {
            JSONObject toPost = new JSONObject(
                    "{\"id\": "+ id + ", \"front\": "+ question + ", \"back\": "+ answer + "}"
            );
            networkRequest.addToRequestQueue(new JsonObjectRequest(
                    method,
                    api,
                    toPost,
                    createCard,
                    error
            ));
        } catch (JSONException e) {
            Log.e("FlashCard insertcard", e.getMessage());
        }
    }

    private void setupCards(){
        NetworkRequest networkRequest = new NetworkRequest(this.appContext);
        String website = WebsiteInterface.GET_CARDS + String.valueOf(this.id);
        networkRequest.addToRequestQueue(new JsonObjectRequest(
                website,
                null,
                storeWebCards,
                error));
    }

    public LinkedList<JSONObject> getCards(){
        //return a two dimensional string that gives back all the cards
        //get String from shared preferences using the id it was stored with
        String cardsAsString = this.sharedPreferences.getString(
                Preferences.CARDS_ARRAY + "_" + this.id,
                ""
        );
        //check if shared preferences is null -> return empty 2d array
        if (cardsAsString.equals("")) {
            return new LinkedList<JSONObject>();
        }
        try{
            //Create JSON array object from preferences string
            JSONArray listOfCards = new JSONArray(cardsAsString);
            //the array contains a size of the number of cards and each card has a front and back
            LinkedList<JSONObject> toReturn = new LinkedList<JSONObject>();

            for (int i=0, size = listOfCards.length(); i < size; i++){
                //iterate through json array
                //construct a JSON object from each index of JSON array
                JSONObject card = (JSONObject) listOfCards.get(i);
                ////put 'front' and 'back' of each object into the index of the iteration
                toReturn.add(card);

            }
            //return the array that was iterated
            return toReturn;
        }catch(JSONException error){
            Log.e("getCards creatJson", error.getMessage());
        }
        //should not get here because of JSON iteration. Used for default
        return new LinkedList<JSONObject>();
    }



}