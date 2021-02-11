package com.moehaemad.structuredflashcards;

import java.util.HashMap;

public class User {
    private String name;
    private int id;
    private int[] deckId;
    private HashMap<Integer, String> deck;

    public User(String name, int id){
        this.name = name;
        this.id = id;
        this.deckId = getDeckIds();
    }

    private int[] getDeckIds(){
    //    TODO: query database for array of deck ids attached to a user using user id
        int[] ids = {};
        return ids;
    };



    private void pairDeckIdNames(){
    //    TODO: for each item in the deck id arrays attach the associated name to the hashmap
    }

    protected HashMap<Integer, String> getDecks(){
        //TODO: pair each deck id to it's associated string
        return this.deck;
    }


}
