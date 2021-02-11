package com.moehaemad.structuredflashcards.model;

import java.util.HashMap;

public interface Card {

    HashMap< String, String> fullCard = new HashMap<String, String>();

    public void setMethod();

    public String getFullCard();

}
