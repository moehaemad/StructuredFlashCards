package com.moehaemad.structuredflashcards.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class NetworkRequest {
    public static Context appContext;
    public RequestQueue requestQueue;
    protected int httpType;
    private String websiteEndpoint;

    public NetworkRequest(Context appContext){
        this.appContext = appContext;
        //setup volley request queue
        getRequestQueue();

        //load the url to append api endpoint and not to retype domain each time
        this.websiteEndpoint = "https://moehaemad.ca/structuredFlashCards/";
    }

    //create the queue for network calls as per Volley library
    private RequestQueue getRequestQueue (){

        if (this.requestQueue == null){
            this.requestQueue = Volley.newRequestQueue(this.appContext.getApplicationContext());
        }
        return this.requestQueue;
    }

    public void setMethod(String http){
        switch(http){
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



    public void getRequest (String endpoint, final Network <JSONObject> listener){
        this.websiteEndpoint += endpoint;
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject jsonObject = response;
                FlashCard.queryResult = response;
                listener.getResult(response);
            }
        };
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Network Error", "request failed");
            }
        };
        this.requestQueue.add(new JsonObjectRequest(this.httpType,
                this.websiteEndpoint,
                null,
                response, error));
    }

}
