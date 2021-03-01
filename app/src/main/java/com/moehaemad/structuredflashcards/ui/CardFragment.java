package com.moehaemad.structuredflashcards.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moehaemad.structuredflashcards.R;
import com.moehaemad.structuredflashcards.controller.DeckRecyclerAdapter;
import com.moehaemad.structuredflashcards.controller.FlashCard;
import com.moehaemad.structuredflashcards.model.Preferences;
import com.moehaemad.structuredflashcards.model.WebsiteInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class CardFragment extends Fragment {
    private LinkedList<JSONObject> deckDataWeb;
    private DeckRecyclerAdapter deckRecyclerAdapter;
    private RecyclerView recyclerView;
    private SharedPreferences sharedPreferences;
    private final String prefName = Preferences.PACKAGE;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.deckDataWeb = new LinkedList<JSONObject>();
        //TODO: grab data from UserSetup.java for the website request or as conditional with Bundle
        try {
            this.deckDataWeb.add(new JSONObject("{\"front\": \"working\", \"back\": \"working\"}"));
        } catch (JSONException e) {
            Log.e("cardfragment json", e.getMessage());
        }
        this.sharedPreferences = getContext().getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_deck_recycler_list, container, false);

        //setup adapter here because of different fragment lifecycle as opposed to activities

        //TODO: create FlashCard object and have it setup the ids into sharedpreferences

        //TODO: grab the id from user which will be another recycler view in the deckView
        //this is hardcoded for now, it will setup the cards into the shared preferences
        //TODO: create a test for this or  particular use case
        FlashCard flashCard = new FlashCard(getContext(), 3);

        //setup a new flash card object with the front and back text to display on recycler view


        //insert the card into the linkedlist
        LinkedList<JSONObject> cards = flashCard.getCards();
        this.deckDataWeb.addAll(cards);

        // setup recycler view adapter
        /*use this.getContext() because 'this' will forward a fragment context and in to make
         * more usable, send context through here instead of converting it adapter*/
        this.deckRecyclerAdapter = new DeckRecyclerAdapter(getActivity(), this.deckDataWeb);
        this.recyclerView = root.findViewById(R.id.deck_list_recycler_view);
        recyclerView.setAdapter(this.deckRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return root;
    }


}
