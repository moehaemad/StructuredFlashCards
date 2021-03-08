package com.moehaemad.structuredflashcards.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;


public class DeckFragment extends Fragment implements UserInput {

    private String front;
    private String back;
    private int id;
    private FlashCard flashCard;

    public void DeckFragment(){
    //    TODO: configure deck View here
        //get id's that were already sent for user, using Deck.java
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*The attachToRoot is set to false because the root container is the FragmentContainerView
        *   which only allows fragments to be attached to it and not full Views that the
        *   infalter.inflate would give back.*/
        View root = inflater.inflate(R.layout.fragment_deck_start, container, false);
        Button submit = root.findViewById(R.id.deck_menu_submit);
        submit.setOnClickListener(toSubmit);
        Spinner spinner = root.findViewById(R.id.deck_menu_spinner);
        setSpinner(spinner);
/*        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true *//* enabled by default *//*) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                // Get navigation host and do navigate up
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_host);
                navController.navigateUp();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);*/
        return root;
    }

    @Override
    public void setUserInput(View v) {
        // get user information from login screen
        EditText usernameView = getView().findViewById(R.id.login_menu_username);
        EditText passwordView = getView().findViewById(R.id.login_menu_password);

        // set username information
        this.front = usernameView.getText().toString();
        this.back = passwordView.getText().toString();
        //TODO: get id from scrollable view, default to 0 for now
        this.id = 0;
        //TODO: create scrollable view to select the ids
        this.flashCard = new FlashCard(
                getContext(),
                this.id,
                this.front,
                this.back
                );
    }

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

    //TODO: create interface that allows getting of response
    public interface Verified {
        void result(boolean jsonResult);

    }

    protected void setSpinner(Spinner spinner){
        //TODO: if id null then use createFromResource of ArrayAdapter
        //if null then use strings from strings.xml
        LinkedList<String> strings = setDeckIdsToString();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(
                getContext(),
                R.layout.deck_view_id_spinner,
                strings
                );
        //specify the layout to use when the drop down menu happens (default android)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

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




    private View.OnClickListener toSubmit = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            //TODO: setup call to Deck.java to insert card given the text input
            //TODO: implement listener when calling Deck.createCard
            Verified makeToast = new Verified() {
                @Override
                public void result(boolean jsonResult) {
                    notifyProcess(jsonResult);
                }
            };

            flashCard.createCard(makeToast);
        }
    };

}
