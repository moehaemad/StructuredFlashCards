package com.moehaemad.structuredflashcards.model;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class NetworkManager
{
    private static final String TAG = "NetworkManager";
    private static NetworkManager instance = null;

    private static final String prefixURL = "http://some/url/prefix/";

    //for Volley API
    public RequestQueue requestQueue;

    private NetworkManager(Context context)
    {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        //other stuf if you need
    }

/*    public static synchronized NetworkManager getInstance(Context context)
    {
        if (null == instance)
            instance = new NetworkManager(context);
        return instance;
    }

    //this is so you don't need to pass context each time
    public static synchronized NetworkManager getInstance()
    {
        if (null == instance)
        {
            throw new IllegalStateException(NetworkManager.class.getSimpleName() +
                                                    " is not initialized, call getInstance(...) first");
        }
        return instance;
    }

    public void somePostRequestReturningString(Object param1, final SomeCustomListener<String> listener)
    {

        String url = prefixURL + "this/request/suffix";

        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("param1", param1);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d(TAG + ": ", "somePostRequest Response : " + response.toString());
                        if(null != response.toString())
                            listener.getResult(response.toString());
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        if (null != error.networkResponse)
                        {
                            Log.d(TAG + ": ", "Error Response code: " + error.networkResponse.statusCode);
                            listener.getResult(false);
                        }
                    }
                });

        requestQueue.add(request);
    }*/
}