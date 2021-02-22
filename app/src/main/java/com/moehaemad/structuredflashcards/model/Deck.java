package com.moehaemad.structuredflashcards.model;

import java.util.HashMap;

public class Deck {
    private String name;
    private int id;
    private int[] deckId;
    private HashMap<Integer, String> deck;

    public Deck(String name, int id){
        this.name = name;
        this.id = id;
        this.deckId = getDeckIds();
    }

    private int[] getDeckIds(){
    //    TODO: query database for array of deck ids attached to a user using user id
    //    TODO: create networkRequest object and retrieve the json data which will contain ids
        int[] ids = {};
        return ids;
    };



    private void pairDeckIdNames(){
    //    TODO: for each item in the deck id arrays attach the associated name to the hashmap
    //    TODO: take the data from getDeckIds and associate them to a HasMap 'deck' object
    }

    protected HashMap<Integer, String> getDecks(){
        //TODO: pair each deck id to it's associated string
        return this.deck;
    }


}
