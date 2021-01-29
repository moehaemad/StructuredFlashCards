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


    public FlashCard(Context appContext){
        this.id=0;
        this.question = "";
        this.answer = "";
        this.request = getRequestQueue();
        this.appContext = appContext;
    }


    public FlashCard (Context appContext, int id, String question, String answer){
        this(appContext);
        this.id = id;
        this.question = question;
        this.answer = answer;
    }


    private RequestQueue getRequestQueue (){

        //TODO: create endpoint on REST API on website
        String url = "https://moehaemad.ca/someEndpoint";
        if (this.request == null){
            this.request = Volley.newRequestQueue(this.appContext);
        }
        return this.request;
    }

    protected JsonObjectRequest getRequest(String url){
        return new JsonObjectRequest
                       (Request.Method.GET, "https://jsonplaceholder.typicode.com/todos/1",
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