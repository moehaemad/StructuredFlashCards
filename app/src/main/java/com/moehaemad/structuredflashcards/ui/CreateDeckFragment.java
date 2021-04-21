package com.moehaemad.structuredflashcards.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.moehaemad.structuredflashcards.R;
import com.moehaemad.structuredflashcards.controller.Deck;
import com.moehaemad.structuredflashcards.controller.User;
import com.moehaemad.structuredflashcards.controller.UserSetup;

import org.w3c.dom.Text;

public class CreateDeckFragment extends Fragment {

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreateDeckFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_create_deck, container, false);
        createId(root);
        return root;
    }



    //TODO: create listener to send network request to create a deck
    //TODO: navigate back to the previous activity
    /**
     * onClickListener for sending an api request to create a deck.
     */
    public void createId(View v){
        //TODO: get valid id value from database
        FloatingActionButton fab = v.findViewById(R.id.create_deck_fab);
        EditText description = v.findViewById(R.id.create_deck_edittext);
        final String descriptionText = description.getText().toString();
        //check if the edit text button is enabled
        //TODO: implement change of text listeners for editText so no empty values sent out
        description.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        View.OnClickListener createDeckListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get username from shared preferences
                String username = UserSetup.getUserFromPreferences(getContext());
                //create the deck object with username if it's not empty
                if (!username.equals("")){
                    Deck mDeck = new Deck (getContext(), username);
                    //send the request to include the description
                    mDeck.createId(null, descriptionText);
                }
            }
        };
    }

}