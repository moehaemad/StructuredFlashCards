package com.moehaemad.structuredflashcards.model;

import com.android.volley.Response;

public interface WebsiteInterface {
    String USER_VALIDATED = Preferences.USER_VALIDATED;
    String USER_NAME = Preferences.USER_NAME;

//    String PRODUCTION = "https://moehaemad.ca/structuredFlashCards/";
    String PRODUCTION = "http://localhost:3000/";
    String CREATE_USER = PRODUCTION + "createUser";
    String CREATE_DECK = PRODUCTION + "createDeck";
    String CREATE_CARD = PRODUCTION + "createCard";
    String CHECK_USER = PRODUCTION + "checkuser";
    String GET_DECKS = PRODUCTION + "getDecks";
    String GET_CARDS = PRODUCTION + "getCards";
    String SET_CARD = PRODUCTION + "setCard";
    String SET_DECK = PRODUCTION + "setDeck";
    String DELETE_CARD = PRODUCTION + "delCard";
    String DELETE_DECK = PRODUCTION + "delDeck";


    Boolean checkWebsiteAuth();



}
