package com.moehaemad.structuredflashcards.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moehaemad.structuredflashcards.R;
import com.moehaemad.structuredflashcards.controller.DeckListRecyclerAdapter;

public class DeckListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =inflater.inflate(R.layout.fragment_deck_list, container, false);
        setupRecycler(root);
        return root;
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