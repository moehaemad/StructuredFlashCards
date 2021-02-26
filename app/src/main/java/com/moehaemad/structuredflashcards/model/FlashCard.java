package com.moehaemad.structuredflashcards.model;


import android.content.Context;
import com.android.volley.RequestQueue;


import org.json.JSONObject;

public class FlashCard{
    private static Context appContext;
    private int id;
    private String question;
    private String answer;

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

    public String getFront(int id){
        return this.question;
    }

    public String getBack(int id){
        return this.answer;
    }



}