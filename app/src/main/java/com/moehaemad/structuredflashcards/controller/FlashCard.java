package com.moehaemad.structuredflashcards.controller;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.moehaemad.structuredflashcards.model.NetworkRequest;
import com.moehaemad.structuredflashcards.model.Preferences;
import com.moehaemad.structuredflashcards.model.UserInput;
import com.moehaemad.structuredflashcards.model.WebsiteInterface;
import com.moehaemad.structuredflashcards.ui.DeckFragment;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;

public class FlashCard{
    private static Context appContext;
    private int id = -1;
    private String question;
    private String answer;
    private SharedPreferences sharedPreferences;
    private final String prefName = Preferences.PACKAGE;

    /**
     * Constructor used where full data is not necessarily required.
     * */
    public FlashCard(@NonNull Context appContext){
        this.appContext = appContext;
        //add shared preferences here because it is crucial to everything else
        this.sharedPreferences = appContext.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }

    /**
     * Constructor to start the flash card.
     * */
    public FlashCard(@NonNull Context appContext, @NonNull int id){
        this(appContext);
        this.id=id;
        this.question = "";
        this.answer = "";
        setupCards();
    }

    /**
     * Constructor setting up the full flash card.
     * */
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

    private Response.Listener<JSONObject> deleteCard = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            Toast.makeText(appContext, "Network request worked", Toast.LENGTH_SHORT).show();

            Log.d("FlashC delete", response.toString());
        }
    };


    private Response.ErrorListener error = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("FlashCard", error.getMessage());
        }
    };

    private void changePreferences (String prefItem, String prefValue){
        //TODO: get previous value of sharedpreferences and append
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        //store the cards array and append the deck id
        prefEditor.putString(prefItem, prefValue);
        prefEditor.apply();
    }

    /**
     * On change or creating the deck id, set the id into shared preferences and the class variable
     * */
    public void setActiveId(int id){
        this.id = id;
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putInt(Preferences.ACTIVE_DECK_ID, id);
        editor.apply();
        //if the active id has changed then the cards being retrieved will need to be queried
        this.setupCards();
    }

    /**
     * Return the active id that is set in shared preferences.
     * */
    public int getActiveId(){
        if (this.id == -1){
            //if id is not set which it should be but in the case it isn't grab from shared preferences.
            //check if the shared preferences even has a deck id to begin with
            this.id = this.sharedPreferences.getInt(Preferences.ACTIVE_DECK_ID, -1);
            return this.id;
        }
        return this.id;
    }





    public String getFront(int id){
        return this.question;
    }

    public String getBack(int id){
        return this.answer;
    }

    public void createCard(final WebsiteInterface.WebsiteResult verification){
        //will not be able to create without instantiating constructor with question/answer values
        //create a standard response
        Response.Listener<JSONObject> createCard = setResponse(verification);
        //start the network request
        NetworkRequest networkRequest = new NetworkRequest(this.appContext);
        //Set the api endpoint
        String api = WebsiteInterface.CREATE_CARD;
        //set method to be POST request
        int method = networkRequest.getMethod("POST");
        try {
            //set the body of the json post request
            JSONObject toPost = new JSONObject(
                    "{\"id\": "+ id + ", \"front\": "+ question + ", \"back\": "+ answer + "}"
            );
            //send the network request to the Volley queue
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

    public void updateCard (final WebsiteInterface.WebsiteResult verification,
                            HashMap<String, String> updateParams){
        //Convert parameters to json acceptable to communicate
        JSONObject jsonBody = jsonifyParams(updateParams);
        //set the standard response listener with the verification interface that was given.
        Response.Listener<JSONObject> updateCard = setResponse(verification);
        //create endpoint
        String api = WebsiteInterface.SET_CARD;
        NetworkRequest networkRequest = new NetworkRequest(this.appContext);
        int method = networkRequest.getMethod("PUT");
        networkRequest.addToRequestQueue(new JsonObjectRequest(
                method,
                api,
                jsonBody,
                updateCard,
                error
        ));


    }

    /**
     * Convert Hasmap values into the JSON format that is expected in update request.w
     * */
    private JSONObject jsonifyParams(HashMap<String, String> params){
        try {
            //first create the initial JSONObject
            JSONObject jsonBody = new JSONObject();
            //enter in columns and specify columns as JSONArray each
            //TODO: make code cleaner by sending to for-loop to iterate with different id's
            //new columns
            JSONArray columns = new JSONArray();
            //set the id ex {"id": 0}
            columns.put(new JSONObject("{\"id\":" + params.get(WebsiteInterface.UPDATE_ID) + " }"));
            //set the front ex {"front":... }
            columns.put(new JSONObject("{\"front\":" + params.get(WebsiteInterface.UPDATE_FRONT) + "}"));
            //set the back ex {"back" :...}
            columns.put(new JSONObject("{\"back\":" + params.get(WebsiteInterface.UPDATE_BACK) + "}"));

            //specify columns
            JSONArray specifyColumns = new JSONArray();
            //set the id ex {"id": 0}
            specifyColumns.put(new JSONObject("{\"id\":" + params.get(WebsiteInterface.UPDATE_ID) + " }"));
            //set the front ex {"front":... }
            specifyColumns.put(new JSONObject("{\"front\":" + params.get(WebsiteInterface.PREV_FRONT) + "}"));
            //set the back ex {"back" :...}
            specifyColumns.put(new JSONObject("{\"back\":" + params.get(WebsiteInterface.PREV_BACK) + "}"));
            //attach the json arrays into the returning body
            jsonBody.put("columns", columns);
            jsonBody.put("specifyColumns", specifyColumns);
            /*return this json body if nothing went wrong with json formatting, otherwise empty json
            *   object.*/
            return jsonBody;

        } catch (JSONException e) {
            Log.e("FCard jsonifyParams", e.getMessage());
        }
        return new JSONObject();
    }

    public void deleteCard(final WebsiteInterface.WebsiteResult verification,
                           int id, String front, String back){
        //create the standard response for the UI
        Response.Listener<JSONObject> deleteCard = setResponse(verification);
        //isntantiate network request object
        NetworkRequest networkRequest = new NetworkRequest(this.appContext);
        //set api endpoint; use /:id/:front?/:back?
        //TODO: change the value here to incluse the fron and back
        String api = WebsiteInterface.DELETE_CARD + String.valueOf(id) + "/" + front + "/" + back;
        //set method
        int method = networkRequest.getMethod("DELETE");
        //send the network request off into queue
        networkRequest.addToRequestQueue(new JsonObjectRequest(
                method,
                api,
                null,
                deleteCard,
                error
        ));
    }

    /**
     * Create a standard response method that sets the interface implementation int he UI to Toast
     *  with the JSON result.
     * */
    private Response.Listener<JSONObject> setResponse(final WebsiteInterface.WebsiteResult webResult){
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    webResult.result(response.getBoolean("result"));
                } catch (JSONException e) {
                    Log.e("Flash Card createCard", e.getMessage());
                }
            }
        };
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
            //TODO: create sync function to gather cards from api and not in shared preferences
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