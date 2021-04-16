package com.moehaemad.structuredflashcards.model;

public interface WebsiteInterface {
    String USER_VALIDATED = Preferences.USER_VALIDATED;
    String USER_NAME = Preferences.USER_NAME;
    String DECK_ARRAY = Preferences.DECK_ARRAY;


//    String PRODUCTION = "https://moehaemad.ca/structuredFlashCards/";
    String PRODUCTION = "http://192.168.1.71:3000/structuredFlashCards/";
    String CREATE_USER = PRODUCTION + "createUser/";
    String CREATE_DECK = PRODUCTION + "createDeck/";
    String CREATE_CARD = PRODUCTION + "createCard/";
    String CHECK_USER = PRODUCTION + "checkuser/";
    String USER_EXISTS = PRODUCTION + "userexists/";
    String GET_DECKS = PRODUCTION + "getDecks/";
    String GET_CARDS = PRODUCTION + "getCards/";
    String SET_CARD = PRODUCTION + "setCard/";
    String PREV_FRONT = "PREV_FRONT";
    String PREV_BACK = "PREV_BACK";
    String UPDATE_FRONT = "UPDATE_FRONT";
    String UPDATE_BACK = "UPDATE_BACK";
    String UPDATE_ID = "UPDATE_ID";

    String SET_DECK = PRODUCTION + "setDeck/";
    String DELETE_CARD = PRODUCTION + "delCard/";
    String DELETE_DECK = PRODUCTION + "delDeck/";

    int DEPRECATED_GET_OR_POST = -1;
    int GET = 0;
    int POST = 1;
    int PUT = 2;
    int DELETE = 3;
    int HEAD = 4;
    int OPTIONS = 5;
    int TRACE = 6;
    int PATCH = 7;


    Boolean checkWebsiteAuth();

    /**
     * Create nested interface to implement only this method for verification of successful query.
     * */
    public interface WebsiteResult {
        void result(boolean jsonResult);

    }


}
