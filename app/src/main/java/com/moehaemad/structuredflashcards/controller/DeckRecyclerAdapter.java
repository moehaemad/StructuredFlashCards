package com.moehaemad.structuredflashcards.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moehaemad.structuredflashcards.R;

import java.util.LinkedList;

public class DeckRecyclerAdapter extends RecyclerView.Adapter<DeckRecyclerAdapter
                                                                      .DeckRecyclerViewHolder>{
    private LinkedList<String> deckData;
    private LayoutInflater viewInflator;

    public DeckRecyclerAdapter (Context appCtx, LinkedList<String> data){
        this.deckData = data;
        //set the layout inflator to inflate a recycler item as required for onCreateView and changes
        this.viewInflator = LayoutInflater.from(appCtx);

    }

    public class DeckRecyclerViewHolder extends RecyclerView.ViewHolder{

        public TextView wordViewText;
        public DeckRecyclerViewHolder(@NonNull View itemView, DeckRecyclerAdapter adapter){
            super(itemView);
            this.wordViewText = itemView.findViewById(R.id.deck_list_single);
        }
    }
    @NonNull
    @Override
    public DeckRecyclerAdapter.DeckRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                         int viewType) {
        TextView deckTextView = (TextView) this.viewInflator.inflate(
                R.layout.deck_list_recycler_item, parent, false);
        DeckRecyclerViewHolder deckViewHolder = new DeckRecyclerViewHolder(deckTextView, this);
        return deckViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DeckRecyclerViewHolder holder, int position) {
        holder.wordViewText.setText("Some Deck Front Text");
    }

    @Override
    public int getItemCount() {
        return this.deckData.size();
    }
}
