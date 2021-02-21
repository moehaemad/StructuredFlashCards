package com.moehaemad.structuredflashcards.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moehaemad.structuredflashcards.R;
import com.moehaemad.structuredflashcards.controller.DeckRecyclerAdapter;

import java.util.LinkedList;

public class CardFragment extends Fragment {
    private LinkedList<String> deckDataWeb;
    private DeckRecyclerAdapter deckRecyclerAdapter;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.deckDataWeb = new LinkedList<String>();
        this.deckDataWeb.add("working");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_deck_recycler_list, container, false);

        //setup adapter here because of different fragment lifecycle as opposed to activities

        // setup recycler view adapter
        /*use this.getContext() because 'this' will forward a fragment context and in to make
         * more usable, send context through here instead of converting it adapter*/
        this.deckRecyclerAdapter = new DeckRecyclerAdapter(getActivity(), this.deckDataWeb);
        this.recyclerView = root.findViewById(R.id.deck_list_recycler_view);
        recyclerView.setAdapter(this.deckRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
