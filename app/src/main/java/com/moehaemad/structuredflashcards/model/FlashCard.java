package com.moehaemad.structuredflashcards.model;


import android.content.Context;
import com.android.volley.RequestQueue;


import org.json.JSONObject;

public class FlashCard{
    private static Context appContext;
    private int id;
    private String question;
    private String answer;
    private RequestQueue requestQueue;
    private int httpType;
    public static JSONObject queryResult;


    //TODO: clear away methods for network requests and reorganize for just Flash Card info

    public FlashCard(Context appContext){
        this.id=0;
        this.question = "";
        this.answer = "";
        this.appContext = appContext;
    }


    public FlashCard (Context appContext, int id, String question, String answer){
        this(appContext);
        this.id = id;
        this.question = question;
        this.answer = answer;
    }





}