package com.moehaemad.structuredflashcards.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class NetworkRequest {
    public static Context appContext;
    public RequestQueue requestQueue;

    public NetworkRequest(Context appContext){
        this.appContext = appContext;
        getRequestQueue();
    }

    //create the queue for network calls as per Volley library
    private RequestQueue getRequestQueue (){

        if (this.requestQueue == null){
            this.requestQueue = Volley.newRequestQueue(this.appContext.getApplicationContext());
        }
        return this.requestQueue;
    }



    public void getRequest (String url, int httpType, final Network <JSONObject> listener){
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
        this.requestQueue.add(new JsonObjectRequest(httpType, url, null,
                response, error));
    }

}