package com.moehaemad.structuredflashcards.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.moehaemad.structuredflashcards.model.NetworkRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class UserSetup {
    private final String USER_VERIFIED = "USER_VERIFICATION";

    private SharedPreferences appPreferences;
    private String preferenceName = "com.moehaemad.structuredflashcards";
    private String websiteEndpoint = "https://moehaemad.ca/structuredFlashCards/";
    private NetworkRequest networkRequest;

    public UserSetup (Context appCtx){
        //setup sharedPreferences for user information
        this.appPreferences = appCtx.getSharedPreferences(this.preferenceName, Context.MODE_PRIVATE);
        //initialize only once for request
        this.networkRequest = new NetworkRequest(appCtx);
    }

    class UserVerificationResponse implements Response.Listener<JSONObject>{
        @Override
        public void onResponse(JSONObject response) {
            SharedPreferences.Editor prefEditor = appPreferences.edit();

            try{
                prefEditor.putBoolean(USER_VERIFIED, response.getBoolean("result"));
                prefEditor.apply();
                Log.d("response object", response.toString());
            }catch(JSONException e) {
                Log.e("user webverif resp", e.getMessage());
            }
        }
    }

    class ErrorResponse implements Response.ErrorListener{
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("Network Error", "request failed");
        }
    }

    public Boolean verifyUser (String login, String pass){
        //create network request with app context
        NetworkRequest checkUser = this.networkRequest;
        //get method for HTTP type and set where to send request
        int method = checkUser.getMethod("GET");
        String endpoint = this.websiteEndpoint + "checkuser/" + login + "/" + pass;

        //user addToRequestQueue instead of having NetworkRequest do implementation
        checkUser.addToRequestQueue(new JsonObjectRequest(method,
                endpoint,
                null,
                new UserVerificationResponse(),
                new ErrorResponse()));
/*        TODO: implement listener/callback here from Response.Listener to get immediate feedback
           because it's a cached process.*/

        return checkWebsiteAuth();
    };

    public Boolean createUser (String login, String pass){
        NetworkRequest createLogin = this.networkRequest;
        int method = createLogin.getMethod("POST");
        try{
            JSONObject postData = new JSONObject();
            postData.put("username", login);
            postData.put("pass", pass);
            String endpoint = this.websiteEndpoint + "createUser";
            createLogin.addToRequestQueue(new JsonObjectRequest(
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

    private Boolean checkWebsiteAuth (){
        //check whether the website result was verified and return the result accordingly
        Boolean jsonResult;

        jsonResult = this.appPreferences.getBoolean(USER_VERIFIED, false);
        //make sure the webResult was atleast given
        Log.d("checkWebsiteAuth", "query " + jsonResult.toString());
        return jsonResult;
    }
}
