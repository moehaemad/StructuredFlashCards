package com.moehaemad.structuredflashcards.model;

import android.view.View;

import androidx.annotation.NonNull;

public interface UserInput {

    void setUserInput(View v);
    void notifyProcess (@NonNull Boolean verified);

}
