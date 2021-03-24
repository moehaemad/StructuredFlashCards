package com.moehaemad.structuredflashcards.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moehaemad.structuredflashcards.R;
import com.moehaemad.structuredflashcards.controller.Deck;
import com.moehaemad.structuredflashcards.controller.DeckRecyclerAdapter;
import com.moehaemad.structuredflashcards.controller.FlashCard;
import com.moehaemad.structuredflashcards.model.Preferences;

import org.json.JSONObject;

import java.util.LinkedList;



/**
 * This implementation sets the recycler view for the list of cards under a given id
 * */

public class CardFragment extends Fragment {
    private LinkedList<JSONObject> deckDataWeb;
    private LinkedList<String> deckIds;
    private int activeId = -1;
    private DeckRecyclerAdapter deckRecyclerAdapter;
    private RecyclerView recyclerView;
    private FlashCard flashCard;


    /**
     * onCreate Lifecycle that sets up the data to be displayed and sets shared preferences
     * */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.deckDataWeb = new LinkedList<JSONObject>();
        this.deckIds = new LinkedList<String>();
        //create the flashCard object
        setFlashCard();
        //create the deck information
        setDeck();
    }


    /**
     * sets up recycler adapter and links the data for all cards under given id.
     * */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_card_recycler_list, container, false);
        //TODO: create a test for this or  particular use case
        //TODO: remove hardcoded id
        //TODO grab id from spinner on selectedItem listene
        //setup adapter here because of different fragment lifecycle as opposed to activities
        setRecyclerInformation(root);
        setSpinner(root);
        return root;
    }

    /**
     * Get cards once again from the flash card and update the linkedlist.
     *
     * This is for when back pressed is called from the single card view where the user has either
     *  updated or deleted card data.
     * */
    @Override
    public void onResume() {
        super.onResume();
        this.updateFlashCards();
        //notify the recycler view that the data has been changed
        this.deckRecyclerAdapter.notifyDataSetChanged();
    }

    /**
     * Setup the flashcard object to be used in class variables
     * */
    private void setFlashCard(){

        //TODO: create a test for this or  particular use case
        //TODO: remove hardcoded id
        //TODO grab id from spinner on selectedItem listener
        //TODO: use the active id class variable assuming it's not -1
        this.flashCard = new FlashCard(getContext());
        this.activeId = flashCard.getActiveId();
        //insert the card into the linkedlist
        LinkedList<JSONObject> cards = flashCard.getCards();
        //append the linked list with the result of the network query
        this.deckDataWeb.addAll(cards);
    }

    /**
     * This will update the flash cards in case of update or delete from the single card view.
     * */
    private void updateFlashCards(){
        //TODO: test whether id stays the same on back press call
        //implementing new flashCard object here just in case
        this.flashCard = new FlashCard(getContext());
        this.activeId = this.flashCard.getActiveId();
        //clear all the data the data that might have been there
        this.deckDataWeb.clear();
        LinkedList<JSONObject> cards = flashCard.getCards();
        this.deckDataWeb.addAll(cards);
    }

    /**
     * Setup the deck ids to be used in class variables
     * */
    private void setDeck(){
        Deck mDeck = new Deck(getContext());
        //set the deck ids from the deck object
        this.deckIds = mDeck.getDeckIdsAsString();
    }




    /**
     * Create the recycler view with the custom adapter view given the inflated root view
     * */
    private void setRecyclerInformation (View root){

        // setup recycler view adapter
        //send context through here instead of converting it adapter
        this.deckRecyclerAdapter = new DeckRecyclerAdapter(getActivity(),
                this.deckDataWeb,
                this.activeId);
        //find recycler view in the associated layout
        this.recyclerView = root.findViewById(R.id.card_recycler_list_recyclerview);
        //link the recycler view to custom adapter
        recyclerView.setAdapter(this.deckRecyclerAdapter);
        //set layout manager for android to handle the recycler view items
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    /**
     * Create the spinner adapter given the list of id's from the Deck.
     * */
    private void setSpinner(View root){
        //get Spinner
        Spinner spinner = root.findViewById(R.id.card_recycler_list_spinner);

        //set adapter
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
              getContext(),
              R.layout.deck_view_id_spinner,
              this.deckIds
        );
        //associate spinner adapter
        spinner.setAdapter(spinnerAdapter);
        //set on item selected listeners
        spinner.setOnItemSelectedListener(spinnnerSelectedListener);

    }

    /**
     * Create the spinner listener that updates the active Deck id.
     * */
    AdapterView.OnItemSelectedListener spinnnerSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //setting the active id to be the integer at the position spinner is selected
            activeId = Integer.parseInt(deckIds.get(position));
            //make the flash card object aware of the selected id so it can put into preferences
            flashCard.setActiveId(activeId);
            //notify the adapter that the active Id has changed which will force the view to rebind.
            recyclerView.getAdapter().notifyDataSetChanged();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            activeId = 0;
        }
    };


}
