package com.moehaemad.structuredflashcards.controller;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.moehaemad.structuredflashcards.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class DeckRecyclerAdapter extends RecyclerView.Adapter<DeckRecyclerAdapter
                                                                      .DeckRecyclerViewHolder>{
    private LinkedList<JSONObject> deckData;
    private LayoutInflater viewInflator;
    private Context ctx;

    public DeckRecyclerAdapter (@NonNull LinkedList<JSONObject> data){
        this.deckData = data;
    }
    public DeckRecyclerAdapter (@Nullable Context appCtx, @NonNull LinkedList<JSONObject> data){
        this(data);
        this.ctx = appCtx;
        //not necessarily needed but will consider for calls from other activities
        this.viewInflator = LayoutInflater.from(appCtx);
    }

    public class DeckRecyclerViewHolder extends RecyclerView.ViewHolder{
        public TextView wordViewText;
        public DeckRecyclerViewHolder(@NonNull View itemView, DeckRecyclerAdapter adapter){
            super(itemView);
            wordViewText = itemView.findViewById(R.id.deck_list_single);
        }
    }
    @NonNull
    @Override
    public DeckRecyclerAdapter.DeckRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                         int viewType) {
        /*setup single item to be inflated and passed to view holder because view holder will
        *   be expecting the inflated single view. The fragment will already have inflated the
        *   recycler view so that doesn't need to be inflated*/
        View singleCard = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.deck_list_recycler_item, parent, false);


        DeckRecyclerViewHolder deckViewHolder = new DeckRecyclerViewHolder(singleCard, this);
        return deckViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DeckRecyclerViewHolder holder, int position) {
        //set value of recycler item
        try {
            //get data at position from linkedlist, access the front of card and turn to text
            holder.wordViewText.setText(this.deckData.get(position).get("front").toString());
        } catch (JSONException e) {
            Log.e("deckrecycler settext", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return this.deckData == null ? 0 : this.deckData.size();
    }
}
