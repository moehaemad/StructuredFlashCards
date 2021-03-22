package com.moehaemad.structuredflashcards.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.moehaemad.structuredflashcards.R;
import com.moehaemad.structuredflashcards.model.UserInput;

/**
 * This fragment is called after selecting a particular card from the recycler view in the 'View
 *  Cards' menu.
 * */
public class SingleCard extends Fragment {

    private int cardId;
    private String cardFront;
    private String cardBack;
    private boolean passedValue = false;

    /**
     * Setup the single card by receiving the data that instantiates the view in a bundle.
     *
     * Will be receiving arguments as int id, String front, String back with keys as described.
     * */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if arguments are null then disable the update and delete buttons by setting class
        //  variable
        Bundle mbundle = getArguments();
        //TODO: check between getArguments() vs savedInstanceState
        if (mbundle == null) {
            //have to wait to inflate the view so set a class variable before disabling listeners
            this.passedValue = false;
//            return;
        }else{
            this.passedValue = true;
        }
        //gather arguments if not null
        this.cardId = mbundle.getInt(UserInput.BUNDLE_DECK_ID);
        this.cardFront = mbundle.getString(UserInput.BUNDLE_FRONT);
        this.cardBack = mbundle.getString(UserInput.BUNDLE_BACK);
    }

    /**
     * Inflate the view here and setup the listeners appropriately.
     * */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_single_card, container, false);

        //setup button listeners for the update and delete depending on if bundle was received
        if (this.passedValue) {
            setupListeners(root);
        }else{
            disableListeners(root);
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //TODO: receive bundle with safeArgs arguments that are passed from previous backstack entry
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Set clickable behavior for listeners to false.
     * */
    private void disableListeners(View v){
        Button update = v.findViewById(R.id.singleCard_update);
        update.setEnabled(false);
        Button delete = v.findViewById(R.id.singleCard_delete);
        delete.setEnabled(false);
    }

    /**
     * Set the onClick listeners for the update and delete
     * */
    private void setupListeners(View v){
       Button update = v.findViewById(R.id.singleCard_update);
       update.setOnClickListener(updateAction);
       Button delete = v.findViewById(R.id.singleCard_delete);
       delete.setOnClickListener(deleteAction);


    }

    /**
     * Update button that creates a Post request to take in text and send to network request.
     * */
    private View.OnClickListener updateAction = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            //get flash card from class variable

            //implement the WebsiteInterface.WebsiteResult interface for Toast

            //call the flash card update action from here

        }
    };

    /**
     * Delete button that sends a Delete request for the card given the text.
     * */
    private View.OnClickListener deleteAction = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            //get flash card from class variable

            //implement the WebsiteInterface.WebsiteResult interface for Toast

            //call the flash card delete action from here

        }
    };
}