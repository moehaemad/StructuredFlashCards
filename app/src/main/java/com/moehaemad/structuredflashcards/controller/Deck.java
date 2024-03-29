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

import java.net.UnknownServiceException;
import java.util.HashMap;
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
        //set global variable to username
        this.username = newUser;
        //let the User object being called in checking empty login know as well
        this.mUser.setLogin(newUser);
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
     * Set the valid deck id in shared preferences from api answer.
     * */
    public synchronized void getValidDeckId(){
        //create response listener
        Response.Listener<JSONObject> mListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //open shared preferences editor
                    SharedPreferences.Editor mEditor = sharedPreferences.edit();
                    //if response if true then add the value into the shared preferences
                    if (response.getBoolean("result")){
                        mEditor.putInt(Preferences.VALID_ID, response.getInt("max"));
                        mEditor.apply();
                    }
                } catch (JSONException e) {
                    Log.e("deck getvalidDeckid", e.getMessage());
                }
            }
        };
        //create network request
        NetworkRequest mRequest = new NetworkRequest(this.ctx);
        mRequest.addToRequestQueue(new JsonObjectRequest(
                WebsiteInterface.GET,
                WebsiteInterface.GET_VALID_ID,
                null,
                mListener,
                error
        ));
    };

    private boolean isValidUser(){
        //check if username if empty string then return error
        try{
            //check if username fields are empty
            this.mUser.checkEmptyLogin();
        }catch(Error err){
            Log.e("Deck createId", "error thrown creating id");
            return false;
        }
        //also check if user exists in api as well
        UserSetup mUserSetup = new UserSetup(this.ctx);
        if (!mUserSetup.isUserInApi(this.username)){
            return false;
        }
        return true;
    }


    /**
     * Associate a deck id with a user.
     *
     *  Change the shared preferences to concatenate the id to the JSON array.
     *  TODO: update tests to accomodate valid id being grabbed from api into preferences
     *  TODO: remove id calls in tests and make description nonnullable
     * */
    public synchronized void createId(@Nullable Integer id, @Nullable String description){
        if (isValidUser()){
            //get the response listener
            Response.Listener createIdListener = this.getCreateIdListener(id, description);
            //grab username because it will already by validated
            this.username = UserSetup.getUserFromPreferences(this.ctx);
            //create network request object
            NetworkRequest mNetworkRequest = new NetworkRequest(this.ctx);
            //grab the valid if from the api
            this.getValidDeckId();
            Integer validId = this.sharedPreferences.getInt(Preferences.VALID_ID, -1);
            //grab the valid deck
            //TODO: create helper function for this
            try {
                //create post data
                JSONObject createDeckId = new JSONObject()
                        .put("id", validId)
                        .put("username", this.username)
                        //if description is null then do add nothing in post
                        .put("description", description == null ? "" : description);
                //invalid username will go to error function which will not touch shared preferences
                //create request and send to queue
                mNetworkRequest.addToRequestQueue(new JsonObjectRequest(
                        WebsiteInterface.POST,
                        WebsiteInterface.CREATE_DECK,
                        createDeckId,
                        createIdListener,
                        error
                ));
            } catch (JSONException e) {
                Log.e("Deck createID", "error in creating JSON body");
            }

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
                        JSONObject toInsert = new JSONObject()
                                .put("id", finalId)
                                .put("username", username)
                                .put("description", finalDescription);
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

    /**
     * Return a linkedlist of a hashmap of the ids and their associated deck descriptions.
     * */
    public LinkedList<HashMap<String, String>> getDeckAsHashmap(){
        //make sure the user is validated
        if (!isValidUser()){
            return new LinkedList<>();
        }
        //initialize a return for the hashmap
        LinkedList<HashMap<String, String>> toReturn = new LinkedList<>();
        //get the json array from the shared preferences
        JSONArray jsonIds = this.getDeckIds();
        //if there's no josn array of ids then return empty hashmap
        if (jsonIds == null) return new LinkedList<>();
        try{
            for (int i=0, size=jsonIds.length(); i < size; i++){
                //grab the object each item of the array holds
                JSONObject jsonObject = jsonIds.getJSONObject(i);
                //add the object's id/description property as a string into the returned hashmap
                HashMap<String, String> currentVal = new HashMap<>();
                //format the hash to put the appropriate information
                currentVal.put(
                        "id",
                        jsonObject.getString("id")
                );
                currentVal.put(
                        "description",
                        jsonObject.getString("description")
                );
                //add the hashmap into the linked list
                toReturn.add(currentVal);
            }
            //return hashmap
            return toReturn;
        }catch(JSONException e){
            Log.e("Deck getDeckAsHashmap", e.getMessage());
        }
        //will never get here but used to satisfy method signature.
        return new LinkedList<>();
    };


}
