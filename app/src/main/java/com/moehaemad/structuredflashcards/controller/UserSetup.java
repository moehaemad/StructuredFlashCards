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

public class UserSetup {
    private Context ctx;
    private JSONObject webResult;
    private final String USER_VERIFIED = "USER_SETUP";

    private SharedPreferences appPreferences;
    private String preferenceName = "com.moehaemad.structuredflashcards";

    public UserSetup (Context appCtx){
        this.ctx = appCtx;
        //setup sharedPreferences for user information
        this.appPreferences = appCtx.getSharedPreferences(this.preferenceName, Context.MODE_PRIVATE);
    }

    class UserVerificationResponse implements Response.Listener<JSONObject>{

        @Override
        public void onResponse(JSONObject response) {
            SharedPreferences.Editor prefEditor = appPreferences.edit();

            try{
                prefEditor.putBoolean(USER_VERIFIED, response.getBoolean("result"));
                prefEditor.apply();
            }catch(JSONException e) {
                Log.e("user webverif resp", e.getMessage());
            }
        }
    }

    public Boolean verifyUser (String login, String pass){
        //create network request with app context
        NetworkRequest checkUser = new NetworkRequest(this.ctx);
        //set the method
        checkUser.setMethod("GET");

        //TODO: user addToRequestQueue instead of having NetworkRequest do implementation
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Network Error", "request failed");
            }
        };
        checkUser.addToRequestQueue(new JsonObjectRequest(Request.Method.GET,
                "https://moehaemad.ca/structuredFlashCards/checkuser/"+ login + "/" + pass, null,
                new UserVerificationResponse(), error));
        //check whether the website result was verified and return the result accordingly
        return checkWebsiteAuth();
    };

    private Boolean checkWebsiteAuth (){
        Boolean jsonResult;

        jsonResult = this.appPreferences.getBoolean(USER_VERIFIED, false);
        //make sure the webResult was atleast given
        Log.d("checkWebsiteAuth", "query " + jsonResult.toString());
        return jsonResult;
    }
}
