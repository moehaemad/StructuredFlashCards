package com.moehaemad.structuredflashcards.model;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;


import org.json.JSONObject;

public class NetworkRequest implements Response.Listener<JSONObject>{
    protected static Context appContext;
    protected RequestQueue requestQueue;
    public static JSONObject websiteResponse;

    public NetworkRequest(Context appContext){
        this.appContext = appContext;
        //setup volley request queue
        getRequestQueue();
    }

    @Override
    public void onResponse(JSONObject response) {
        this.websiteResponse = response;
    }


    //create the queue for network calls as per Volley library
    private RequestQueue getRequestQueue (){

        if (this.requestQueue == null){
            this.requestQueue = Volley.newRequestQueue(this.appContext.getApplicationContext());
        }
        return this.requestQueue;
    }

    public int getMethod(String http){
        switch(http){
            case "GET":
                return Request.Method.GET;
            case "POST":
                return Request.Method.POST;
            case "PUT":
                return Request.Method.PUT;
            case "DELETE":
                return Request.Method.DELETE;
            default:
                return Request.Method.GET;
        }
    }

    public void addToRequestQueue(Request<JSONObject> request){
        getRequestQueue().add(request);
    }


}
