package com.moehaemad.structuredflashcards.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moehaemad.structuredflashcards.R;

public class StartContent extends Fragment {

    private SharedPreferences sharedPreferences;
    private final String sharedPrefName = "com.moehaemad.structuredflashcards";
    private final String USER = "USER_VERIFICATION";

    public StartContent() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.sharedPreferences = getContext().getSharedPreferences(this.sharedPrefName,
                Context.MODE_PRIVATE);
        Boolean userVerified = this.sharedPreferences.getBoolean(USER, false);
        @LayoutRes int verifiedUserLayout = changeToUserSettings(userVerified);
        // Inflate the layout for this fragment
        View root = inflater.inflate(verifiedUserLayout, container, false);
        return root;
    }


    private @LayoutRes int changeToUserSettings(Boolean userVerified){
        //TODO: change the layout to a custom home screen for logged in User

        return R.layout.fragment_constraint_login;
    }


    /*TODO: update the home view by setting the text of buttons and functionality to refer to User
       settings*/
}
