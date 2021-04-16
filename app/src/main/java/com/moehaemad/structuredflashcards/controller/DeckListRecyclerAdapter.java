package com.moehaemad.structuredflashcards.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moehaemad.structuredflashcards.R;
import com.moehaemad.structuredflashcards.model.Preferences;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.LinkedList;

public class DeckListRecyclerAdapter extends RecyclerView.Adapter<DeckListRecyclerAdapter
        .DeckListRecyclerViewHolder>{

    private String username;
    private LinkedList<HashMap<String, String>> deckInformation;
    private Context ctx;
    private LayoutInflater mInflator;

    public DeckListRecyclerAdapter(@NonNull Context ctx){
        this.ctx = ctx;
        setupDeckInformation();
        this.mInflator = LayoutInflater.from(this.ctx);
    }

    public class DeckListRecyclerViewHolder extends RecyclerView.ViewHolder{
        private DeckListRecyclerAdapter mAdapter;
        private TextView id;
        private TextView description;
        public DeckListRecyclerViewHolder(@NonNull View itemview,
                                          DeckListRecyclerAdapter adapter){
            super(itemview);
            this.mAdapter = adapter;
            this.id = itemview.findViewById(R.id.deck_list_item_id);
            this.description = itemview.findViewById(R.id.deck_list_item_description);
        }

        /**
         * This will set the textview of the id and description to their respective values.
         * */
        public void setDeckInformation(String id, String description){
            this.id.setText(id);
            this.description.setText(description);
        }
    }


    @NonNull
    @Override
    public DeckListRecyclerAdapter.DeckListRecyclerViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View mySingleView = this.mInflator.inflate(R.layout.fragment_card_recycler_list_item,
                parent,
                false);
        DeckListRecyclerViewHolder mViewHolder = new DeckListRecyclerViewHolder(mySingleView,
                this);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(
            @NonNull DeckListRecyclerAdapter.DeckListRecyclerViewHolder holder, int position) {
        //TODO: change the text to fit the view of that particular deck
        String id = this.deckInformation.get(position).get("id");
        String description = this.deckInformation.get(position).get("description");
        holder.setDeckInformation(id, description);
    }

    @Override
    public int getItemCount() {
        return this.deckInformation.size();
    }

    /**
     * This will gather the deck id and information from the api.
     * */
    public void setupDeckInformation(){
        //get username from shared preferences
        SharedPreferences mPreferences = this.ctx.getSharedPreferences(Preferences.PACKAGE,
                Context.MODE_PRIVATE);
        this.username  = mPreferences.getString(Preferences.USER_NAME, "");
        //create deck with context and username
        Deck mDeck = new Deck(this.ctx, this.username);
        //call the method to get deckinformation as hashmap
        this.deckInformation = mDeck.getDeckAsHashmap();
    }
}
