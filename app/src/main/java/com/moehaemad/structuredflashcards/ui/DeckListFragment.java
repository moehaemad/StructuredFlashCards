package com.moehaemad.structuredflashcards.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.moehaemad.structuredflashcards.R;
import com.moehaemad.structuredflashcards.controller.DeckListRecyclerAdapter;

public class DeckListFragment extends Fragment {
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.root =inflater.inflate(R.layout.fragment_deck_list, container, false);
        setupRecycler(root);
        createDeck(root);
        return root;
    }

    /**
     * Navigate to view to create a deck
     * */
    public void createDeck(View root){
        Button createButton = root.findViewById(R.id.deck_list_recycler_create_button);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_checkDeckList_to_createDeckFragment);
            }
        };
        createButton.setOnClickListener(onClickListener);
    }

    /**
     * Setup the recycler view adapter information.
     * */
    public void setupRecycler(View root){
        RecyclerView deckList = root.findViewById(R.id.deck_list_recycler);
        DeckListRecyclerAdapter deckListAdapter = new DeckListRecyclerAdapter(getActivity());
        deckList.setAdapter(deckListAdapter);
        deckList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}