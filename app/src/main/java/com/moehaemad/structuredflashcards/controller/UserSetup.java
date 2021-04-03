package com.moehaemad.structuredflashcards.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.moehaemad.structuredflashcards.model.NetworkRequest;
import com.moehaemad.structuredflashcards.model.Preferences;
import com.moehaemad.structuredflashcards.model.WebsiteInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class UserSetup {

    private SharedPreferences appPreferences;
    private NetworkRequest networkRequest;
    private String login = "";
    private String password = "";
    private Deck userDecks;

    /**
     * Public contructor, create preferences, network request, and Deck information.
     * */
    public UserSetup(Context appCtx){
        //setup sharedPreferences for user information
        this.appPreferences = getAppPreferences(appCtx);
        //initialize only once for request
        this.networkRequest = new NetworkRequest(appCtx);
        //grab the username from system preferences
        this.login = this.appPreferences.getString(WebsiteInterface.USER_NAME,
                "");
        //if constructing somewhere not from login then check shared preferences for stored login
        if (this.login == "" ){
            this.userDecks = new Deck(appCtx);
        }else{
            this.userDecks = new Deck (appCtx, this.login);
        }

    }

    /**
     * Constructor given login and password information, primarily used in Login.
     * */
    public UserSetup (Context appCtx, String login, String password){
        this(appCtx);
        //TODO: deck is created twice
        //set login and password
        this.login = login;
        this.password = password;

    }

    /**
     * This is to access the shared preferences with testing so it can be stubbed.
     * */
    protected SharedPreferences getAppPreferences(Context ctx){
        return ctx.getSharedPreferences(Preferences.PACKAGE,
                Context.MODE_PRIVATE);
    }

    /**
     * Verify the user as being created in shared preferences and synchronize if values inconsistent.
     * */
    class UserVerificationResponse implements Response.Listener<JSONObject>{
        @Override
        public void onResponse(JSONObject response) {
            SharedPreferences.Editor prefEditor = appPreferences.edit();

            try{
                prefEditor.putBoolean(WebsiteInterface.USER_VALIDATED,
                        response.getBoolean("result"));
                prefEditor.apply();
                syncUserToApp();
//                Log.d("Usersetup resp object", response.toString());
            }catch(JSONException e) {
                Log.e("user webverif resp", e.getMessage());
            }
        }
    }

    /**
     * Generic error listener.
     * */
    class ErrorResponse implements Response.ErrorListener{
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("Network Error", error.getMessage());
        }
    }

    /**
     * Login class variable will already have been set in constructor so update accordingly.
     * */
    public void syncUserToApp () {
        SharedPreferences.Editor preferenceEditor = this.appPreferences.edit();
        preferenceEditor.putString(WebsiteInterface.USER_NAME, this.login);
        preferenceEditor.apply();
    }

    /**
     * Check shared preference for if user exists or not.
     * */
    public boolean doesUserExist(){
        //check shared preferences not just this.login because a login can exist without api auth
        String user = this.appPreferences.getString(Preferences.USER_NAME, "");
        //if null then return false
        //if user exists then the preferences will by anything but ""
        return !user.equals("");
    }

    /**
     * Return decks.
     * */
    public Deck getUserDecks() {
        return this.userDecks;
    }

    /**
     * Return username.
     * */
    public String getUsername (){
        return this.login;
    }

    /**
     * If there's no username information given by UI.
     * */
    public void checkEmptyLogin (){
        if (this.login == "" || this.password == ""){
            throw new Error("Error in login: no username or password");
        }
    }

    /**
     * Check if user exists in the api given the login and password information.
     * */
    public Boolean verifyUser (){
        checkEmptyLogin();
        //create network request with app context
        NetworkRequest checkUser = this.networkRequest;
        //get method for HTTP type and set where to send request
        int method = checkUser.getMethod("GET");
        String endpoint = WebsiteInterface.CHECK_USER + this.login + "/" + this.password;

        //user addToRequestQueue instead of having NetworkRequest do implementation
        checkUser.addToRequestQueue(new JsonObjectRequest(
                method,
                endpoint,
                null,
                new UserVerificationResponse(),
                new ErrorResponse()));
/*        TODO: implement listener/callback here from Response.Listener to get immediate feedback
           because it's a cached process.*/

        return checkWebsiteAuth();
    };

    /**
     * Create a network request given the login and password information to create a user in api and
     *  shared preferences.
     * */
    public Boolean createUser (){
        checkEmptyLogin();
        NetworkRequest createLogin = this.networkRequest;
        int method = createLogin.getMethod("POST");
        try{
            JSONObject postData = new JSONObject();
            postData.put("username", this.login);
            postData.put("pass", this.password);
            String endpoint = WebsiteInterface.CREATE_USER;
            createLogin.addToRequestQueue(new JsonObjectRequest(
                    method,
                    endpoint,
                    postData,
                    new UserVerificationResponse(),
                    new ErrorResponse()
            ));
        }catch(JSONException e){
            Log.e("usersetup create post", e.getMessage());
        }
        return checkWebsiteAuth();
    }

    /**
     * Check if the user exists in the database by searching if it was set in one of the network
     *  request methods under a particular key.
     * */
    private synchronized Boolean checkWebsiteAuth (){
        //check whether the website result was verified and return the result accordingly
        Boolean jsonResult;

        jsonResult = this.appPreferences.getBoolean(WebsiteInterface.USER_VALIDATED,
                false);
        //make sure the webResult was atleast given
//        Log.d("checkWebsiteAuth", "query " + jsonResult.toString());
        return jsonResult;
    }
}
