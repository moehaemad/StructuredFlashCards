package com.moehaemad.structuredflashcards.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moehaemad.structuredflashcards.R;

public class StartContent extends Fragment {

    public StartContent() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // please refer to DeckFragment for explanation why attachToRoot = false
        return inflater.inflate(R.layout.fragment_constraint_login, container, false);
    }
}
