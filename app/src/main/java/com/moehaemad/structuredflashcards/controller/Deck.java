package com.moehaemad.structuredflashcards.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.Replaceable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
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
    private User mUser;

    /**
     * Create deck from context without specifying username in case it's presumed to be set.
     * */
    public Deck(Context ctx){
        this.sharedPreferences = ctx.getSharedPreferences(Preferences.PACKAGE, Context.MODE_PRIVATE);
        this.ctx = ctx;
        //set the class variable to include these ids
        setDeck();
        syncDeck();
    }

    /**
    * Create deck and synchronise the shared preferences in case changes to ids have been made.
    * */
    public Deck(@NonNull Context ctx, @NonNull  String username){
        this(ctx);
        this.username = username;
        this.mUser = new User(this.username, "");
        //make a network request to get the ids
        syncDeck();
    }

    /**
     * Set the username for testing.
     * */
    public void setUsername (String newUser){
        this.username = newUser;
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
//                Log.d("Deck ids", response.get("ids").toString());
//                Log.d("deckArray", deckArray.toString());
                //apply changed from the editor
                prefEditor.apply();
            }catch(JSONException e){
                Log.e("Deck json error", e.getMessage());
            }
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
        Log.d("Deck syncDeck", "running sync deck");
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
        //format of the deck ids will be an array but more specifically an array of objects
            //containing key-value pairs for the particular deck (i.e. description, username etc.)
        //make sure the thing you're returning exists, don't ever return null
        try {
            this.deckArray = new JSONArray(this.sharedPreferences.getString(
                    Preferences.DECK_ARRAY,
                    "[]"
            ));
            return this.deckArray;
        } catch (JSONException e) {
            //if anything goes wrong with the shared preferences itself then throw error
            //default case already handled
            throw new Error();
        }
    };


    /**
     * Associate a deck id with a user.
     *
     *  Change the shared preferences to concatenate the id to the JSON array.
     * */
    public synchronized void createId(int id, @Nullable String description){
        //check if username if empty string then return error
        try{
            this.mUser.checkEmptyLogin();
        }catch(Error err){
            Log.e("Deck createId", "error thrown creating id");
        }
        //get the response listener
        Response.Listener createIdListener = this.getCreateIdListener(id, description);
        //grab username
        //create network request object
        NetworkRequest mNetworkRequest = new NetworkRequest(this.ctx);
        int method = mNetworkRequest.getMethod("POST");
        //set the url
        String url = WebsiteInterface.CREATE_DECK;
        //TODO: create helper function for this
        try {
            //create post data
            JSONObject createDeckId = new JSONObject();
            createDeckId.put("id", id);
            createDeckId.put("username", this.username);
            //if description is null then do add nothing in post
            createDeckId.put("description", description == null ? "" : description);
            //invalid username will go to error function which will not touch shared preferences
            //create request and send to queue
            mNetworkRequest.addToRequestQueue(new JsonObjectRequest(
                    method,
                    url,
                    createDeckId,
                    createIdListener,
                    error
            ));
        } catch (JSONException e) {
            Log.e("Deck createID", "error in creating JSON body");
        }


    }

    /**
     * Helper function for creating id to return the JSON object request listener
     * */
    private synchronized Response.Listener<JSONObject> getCreateIdListener (int id,
                                                                    @Nullable String description){

        final Integer finalId = id;
        final String finalDescription = description;

        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("result")){
                        //get current JSON Array for the deck ids
                        JSONArray mArray = new JSONArray(sharedPreferences.getString(
                                Preferences.DECK_ARRAY,
                                "[]"
                        ));
                        //create JSON Object of the id, username, and description
                        JSONObject toInsert = new JSONObject();
                        toInsert.put("id", finalId);
                        toInsert.put("username", username);
                        toInsert.put("description", finalDescription);
                        //insert the object into the json array
                        mArray.put(toInsert);
                        //open shared preferences and update result for id, username, and description
                        SharedPreferences.Editor mEditor = sharedPreferences.edit();
                        //send the value of the json array as string to preferences
                        mEditor.putString(Preferences.DECK_ARRAY, mArray.toString());
                        mEditor.apply();
                        //append result to JSON Array:
                    }
                } catch (JSONException e) {
                    Log.e("Deck createID", "error in response JSON");
                }
            }
        };
    }

    /**
     * Delete the associated deck id to a user.
     *
     * Change the shared preferences to delete from JSON array.
     * */
    public void deleteId(){

    }


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
