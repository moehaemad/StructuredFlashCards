package com.moehaemad.structuredflashcards.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.moehaemad.structuredflashcards.R;
import com.moehaemad.structuredflashcards.controller.FlashCard;
import com.moehaemad.structuredflashcards.model.UserInput;
import com.moehaemad.structuredflashcards.model.WebsiteInterface;

import java.util.HashMap;

/**
 * This fragment is called after selecting a particular card from the recycler view in the 'View
 *  Cards' menu.
 * */
public class SingleCard extends Fragment {

    private int cardId = -1;
    private String cardFront = "";
    private String cardBack = "";
    private boolean passedValue = false;
    private FlashCard flashCard;
    private View rootView;

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
            return;
        }else{
            this.passedValue = true;
        }
        //gather arguments if not null
        this.cardId = mbundle.getInt(UserInput.BUNDLE_DECK_ID);
        this.cardFront = mbundle.getString(UserInput.BUNDLE_FRONT);
        this.cardBack = mbundle.getString(UserInput.BUNDLE_BACK);
        this.flashCard = new FlashCard(getContext(),
                this.cardId,
                this.cardFront,
                this.cardBack);
    }

    /**
     * Inflate the view here and setup the listeners appropriately.
     * */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_single_card, container, false);
        //Set the root view visible as class variable for convenience
        this.rootView = root;
        //Set text based on bundle received values
        setText(root);
        //setup button listeners for the update and delete depending on if bundle was received
        if (this.passedValue) {
            setupListeners(root);
        }else{
            disableListeners(root);
        }

        return root;
    }

    /**
     * Setting the EditText properties of the single card view based on the bundle received.
     * */
    private void setText(View v){
        EditText front = v.findViewById(R.id.singleCard_front_edit);
        EditText back = v.findViewById(R.id.singleCard_back_edit);
        front.setText(this.cardFront);
        back.setText(this.cardBack);
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
     * This is a standard Toast with just a varying message so used as separate method.
     * */
    private WebsiteInterface.WebsiteResult standardResult(final String message){
        return new WebsiteInterface.WebsiteResult() {
            @Override
            public void result(boolean jsonResult) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        };
    }

    /**
     * Update button that creates a Post request to take in text and send to network request.
     * */
    private View.OnClickListener updateAction = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            //get flash card from class variable

            //call the flash card update action from here
            //TODO: implement the update method in FlashCard.java
            //create Hashmap of previous values for card and updated values
            HashMap<String, String> mHash = new HashMap<>();
            //enter in id which will be the same regardless of difference in input
            mHash.put(WebsiteInterface.UPDATE_ID, String.valueOf(cardId));
            //gather the previous values for front and back which are stored as class variables
            mHash.put(WebsiteInterface.PREV_FRONT, cardFront);
            mHash.put(WebsiteInterface.PREV_BACK, cardBack);
            //check whether different from previous input
            if (canUpdate()){
                EditText newFront = rootView.findViewById(R.id.singleCard_front_edit);
                String updatedFront = newFront.getText().toString();
                EditText newBack = rootView.findViewById(R.id.singleCard_back_edit);
                String updatedBack = newBack.getText().toString();
                mHash.put(WebsiteInterface.UPDATE_FRONT, updatedFront);
                mHash.put(WebsiteInterface.UPDATE_BACK, updatedBack);
                flashCard.updateCard(standardResult("Updated Card!"), mHash);
            }
            //add prev values as 'specified columns' and current val as 'columns'

            //send hashmap to flashcard update method
        }
    };

    /**
     * Check whether current edit text values different from previous.
     *
     * Return true if they are not equal, otherwise they are equal and should not send update.
     * */
    private boolean canUpdate(){
        EditText updatedFront = this.rootView.findViewById(R.id.singleCard_front_edit);
        String updatedFrontValue = updatedFront.getText().toString();
        EditText updatedBack = this.rootView.findViewById(R.id.singleCard_back_edit);
        String updatedBackValue = updatedBack.getText().toString();
        //if either equal operator is false then you can update which would require you to return true
        boolean update = !(updatedFrontValue.equals(cardFront) && updatedBackValue.equals(cardBack));
        return update;
    }

    /**
     * Delete button that sends a Delete request for the card given the text.
     * */
    private View.OnClickListener deleteAction = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            //get flash card from class variable
            //call the standardResult to toast with deleted message
            //call the flash card delete action from here
            flashCard.deleteCard(
                    standardResult("Successfully deleted card!"),
                    cardId,
                    cardFront,
                    cardBack);
        }
    };
}