package com.moehaemad.structuredflashcards.model;

import android.view.View;

import androidx.annotation.NonNull;

public interface UserInput {

    String BUNDLE_DECK_ID = "ID";
    String BUNDLE_FRONT = "FRONT";
    String BUNDLE_BACK = "BACK";

    void setUserInput(View v);
    void notifyProcess (@NonNull Boolean verified);

}
