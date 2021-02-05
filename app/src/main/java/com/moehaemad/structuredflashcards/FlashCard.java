package com.moehaemad.structuredflashcards;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class FlashCard{
    private static Context appContext;
    private int id;
    private String question;
    private String answer;
    private RequestQueue requestQueue;
    private int httpType;
    public static JSONObject queryResult;


    public FlashCard(Context appContext){
        this.id=0;
        this.question = "";
        this.answer = "";
        this.appContext = appContext;
        this.requestQueue = getRequestQueue();
        // this.queryResult = new JSONObject();
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

        if (this.requestQueue == null){
            this.requestQueue = Volley.newRequestQueue(this.appContext.getApplicationContext());
        }
        return this.requestQueue;
    }


    //start the network request and return the object as result from http GET request
    public void getRequest(String url, final UserRequests<JSONObject> listener){
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("Network:", "Response worked");
                JSONObject jsonObject = response;
                FlashCard.queryResult = response;
                Log.i("JSON Response", "json");
                listener.getResult(response);
            }
        };
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Network Error", "request failed");
            }
        };
        this.requestQueue.add(new JsonObjectRequest(this.httpType, url, null,
                response, error));
    }

}