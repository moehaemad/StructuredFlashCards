package com.moehaemad.structuredflashcards.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.moehaemad.structuredflashcards.model.Network;
import com.moehaemad.structuredflashcards.model.NetworkRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class UserSetup {
    private Context ctx;
    private JSONObject webResult;

    private SharedPreferences appPreferences;
    private String preferenceName = "com.moehaemad.structuredflashcards";

    public UserSetup (Context appCtx){
        this.ctx = appCtx;
        //setup sharedPreferences for user information
        this.appPreferences = appCtx.getSharedPreferences(this.preferenceName, Context.MODE_PRIVATE);
    }

    public Boolean verifyUser (String login, String pass){
        //create network request with app context
        NetworkRequest checkUser = new NetworkRequest(this.ctx);
        //set the method
        checkUser.setMethod("GET");
        //get the user information as asynchronous item
        checkUser.getRequest("checkuser/" + login + "/" + pass,
                new Network<JSONObject>() {
                    @Override
                    public void getResult(JSONObject object) {
                        webResult = object;
                        Log.d("result query", object.toString());
                    }
                });
        //check whether the website result was verified and return the result accordingly
        return checkWebsiteAuth();
    };

    private Boolean checkWebsiteAuth (){
        Boolean jsonResult = false;
        try{
            if (this.webResult != null) {
                jsonResult = this.webResult.getBoolean("result");
            }
        }catch(JSONException e){
            return false;
        }
        //make sure the webResult was atleast given
        Log.d("checkWebsiteAuth", "query " + jsonResult.toString());
        return jsonResult;
    }
}
