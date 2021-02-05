package com.moehaemad.structuredflashcards;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FlashCard {
    private static Context appContext;
    private int id;
    private String question;
    private String answer;
    private RequestQueue request;
    private int httpType;


    public FlashCard(Context appContext){
        this.id=0;
        this.question = "";
        this.answer = "";
        this.appContext = appContext;
        this.request = getRequestQueue();
    }


    public FlashCard (Context appContext, int id, String question, String answer){
        this(appContext);
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

    protected void setMethod(String request){
        switch(request){
            case "GET":
                this.httpType = Request.Method.GET;
                break;
            case "PUT":
                this.httpType = Request.Method.PUT;
                break;
            case "DELETE":
                this.httpType = Request.Method.DELETE;
                break;
        }
    }

    //create the queue for network calls as per Volley library
    private RequestQueue getRequestQueue (){

        if (this.request == null){
            this.request = Volley.newRequestQueue(this.appContext.getApplicationContext());
        }
        return this.request;
    }

    //start the network request and return the object as result from http GET request
    protected JsonObjectRequest getRequest(String url){
        return new JsonObjectRequest
                       (this.httpType, url,
                               null,
                               new Response.Listener<JSONObject>() {

                                   @Override
                                   public void onResponse(JSONObject response) {
                                       Log.i("Network:", "Response worked");
                                       JSONObject jsonObject = response;
                                       Log.i("JSON Response", "json");
                                   }
                               }, new Response.ErrorListener() {

                                   @Override
                                   public void onErrorResponse(VolleyError error) {
                                       // TODO: Handle error
                                       Log.i("Network Error", "request failed");

                                   }
                               });
    }


}