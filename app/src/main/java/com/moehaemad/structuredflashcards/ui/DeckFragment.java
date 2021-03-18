package com.moehaemad.structuredflashcards.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.moehaemad.structuredflashcards.R;
import com.moehaemad.structuredflashcards.controller.Deck;
import com.moehaemad.structuredflashcards.controller.FlashCard;
import com.moehaemad.structuredflashcards.model.UserInput;
import com.moehaemad.structuredflashcards.model.WebsiteInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * View that allows user to create cards into Database under selectable id (android spinner).
 * */
public class DeckFragment extends Fragment implements UserInput {

    private String front;
    private String back;
    private int id = -1;
    private FlashCard flashCard;

    /**
     * Inflate the view and set the spinner here.
     * */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*The attachToRoot is set to false because the root container is the FragmentContainerView
        *   which only allows fragments to be attached to it and not full Views that the
        *   infalter.inflate would give back.*/
        View root = inflater.inflate(R.layout.fragment_deck_start, container, false);
        //set button listener
        Button submit = root.findViewById(R.id.deck_menu_submit);
        submit.setOnClickListener(toSubmit);
        //set spinner adapter
        Spinner spinner = root.findViewById(R.id.deck_menu_spinner);
        setSpinner(spinner);
        //set spinner listener for the dropdown menu
        spinner.setOnItemSelectedListener(spinnerClicked);
        return root;
    }

    /**This interface is sent to the controller whenever a network request is made with anonymous
     *      class. The boolean value is used to make Toast to inform user of network request.*/
    public interface Verified {
        void result(boolean jsonResult);

    }


    /**
     *Method that sets the user created input into FlashCard object which is used to create a card.
     * */
    @Override
    public void setUserInput(View v) {
        // get user information from login screen
        EditText front = getView().findViewById(R.id.deck_menu_front_input);
        EditText back = getView().findViewById(R.id.deck_menu_back_input);

        // set username information
        this.front = front.getText().toString();
        this.back = back.getText().toString();
        //check if id is -1 which is default and send user notification to select id
        if (this.id == -1 ){
            Toast.makeText(getContext(), "No id Selected", Toast.LENGTH_SHORT).show();
        }else{
        this.flashCard = new FlashCard(
                getContext(),
                this.id,
                this.front,
                this.back
                );
        }
    }

    /**
     * create method to inform user of progress of creating a card.
     * */
    @Override
    public void notifyProcess(@NonNull Boolean verified) {
        String userNotification = "";
        if (verified){
            userNotification = "Successfully Created Card";
        }else{
            userNotification = "Creating Card failed";
        }
        //display the appropriate notification for authenticating user
        Toast.makeText(getContext(), userNotification, Toast.LENGTH_SHORT).show();
    }



    /**
     * Associate the spinner adapter from a generic ArrayAdapter and use linked list of strings.
     * */
    protected void setSpinner(Spinner spinner){
        Deck mDeck = new Deck(getContext());

        //if null then use strings from strings.xml
        LinkedList<String> strings = mDeck.getDeckIdsAsString();
        String[] resourceStrings = getContext().getResources().getStringArray(R.array.deck_spinner);

        //if no items in deck ids then add string-array from resources
        //  use Array.asList to turn String array into collection.
        if (strings == null) strings.addAll(Arrays.asList(resourceStrings));
        //construct adapter from linkedlist of strings and using a textview .xml file
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(
                getContext(),
                R.layout.deck_view_id_spinner,
                strings
                );
        //specify the layout to use when the drop down menu happens (default android)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }


    /**
     * Implement the spinner AdapterView to respond to selected items in the list.
     * */
    private AdapterView.OnItemSelectedListener spinnerClicked = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long itemId) {
            String text = (String) parent.getItemAtPosition(position);
            id = Integer.parseInt(text);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            //set the default id to be -1
            id = -1;
        }
    };



    /**
     * Construct ids from JSON Array into Strings to be passed with a linkedlist
     * */
    private LinkedList<String> setDeckIdsToString(){
        //create deck class
        Deck mDeck = new Deck(getContext());
        //get the ids as a json array from Deck
        JSONArray jsonArray = mDeck.getDeckIds();
        LinkedList<String> deckIdString = new LinkedList<>();
        try{
            for (int i=0, size=jsonArray.length(); i < size; i++){
                //array is of json objects so get object in array index
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //set the string of the id property as a string for deck ids
                deckIdString.add(jsonObject.getString("id"));
            }
            return deckIdString;

        }catch(JSONException e){
            Log.e("Spinner setJSONArray", e.getMessage());
        }
        //if null then use strings from strings.xml
        String[] stringArray = getResources().getStringArray(R.array.deck_spinner);
        deckIdString.add(stringArray[0]);
        return deckIdString;
    };



    /**
     * On Click listener for submitting data in order to create the flash card.
     * */
    private View.OnClickListener toSubmit = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            //setup the flashCard object given the input in EditText fields
            setUserInput(v);
            //implement listener to make toast about network result
            WebsiteInterface.WebsiteResult makeToast = new WebsiteInterface.WebsiteResult() {
                @Override
                public void result(boolean jsonResult) {
                    notifyProcess(jsonResult);
                }
            };
            //create the card and send POST to api
            flashCard.createCard(makeToast);
        }
    };

}
