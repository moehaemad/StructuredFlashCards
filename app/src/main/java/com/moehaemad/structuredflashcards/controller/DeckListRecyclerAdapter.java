package com.moehaemad.structuredflashcards.controller;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

public class DeckListRecyclerAdapter extends RecyclerView.Adapter<DeckListRecyclerAdapter
        .DeckListRecyclerViewHolder>{

    private HashMap<String, String> deckInformation;

    public class DeckListRecyclerViewHolder extends RecyclerView.ViewHolder{
        public DeckListRecyclerViewHolder(@NonNull View itemview){
            super(itemview);

        }
    }


    @NonNull
    @Override
    public DeckListRecyclerAdapter.DeckListRecyclerViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(
            @NonNull DeckListRecyclerAdapter.DeckListRecyclerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void setupDeckInformation(){
        //TODO: wait for implementation to receive hashmap of deck ids and their descriptions
        //get username from shared preferences
        //create deck with context and username
        //call the method to get deckinformation as hashmap

    }
}
