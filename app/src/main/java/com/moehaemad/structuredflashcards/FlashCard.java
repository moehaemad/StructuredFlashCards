package com.moehaemad.structuredflashcards;


public class FlashCard {
    private int id;
    private String question;
    private String answer;




    public FlashCard(){
        this.id = 0;
        this.question = "";
        this.answer = "";
    }


    public FlashCard (int id, String question, String answer){

        this.id = id;
        this.question = question;
        this.answer = answer;
    }


    private void getFlashCard (int Id){

        //TODO: create endpoint on REST API on website
        String url = "https://moehaemad.ca/someEndpoint";


    }


}