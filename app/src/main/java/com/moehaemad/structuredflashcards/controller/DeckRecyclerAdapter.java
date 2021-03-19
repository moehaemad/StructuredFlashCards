package com.moehaemad.structuredflashcards.controller;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.moehaemad.structuredflashcards.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;


/**
 *
 * */

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
        public DeckRecyclerAdapter adapter;
        public JSONObject card;
        private Context ctx;
        public DeckRecyclerViewHolder(@NonNull View itemView, DeckRecyclerAdapter adapter, Context ctx){
            super(itemView);
            this.adapter = adapter;
            wordViewText = itemView.findViewById(R.id.deck_list_recycler_item_single);
            itemView.setOnClickListener(toggleCardFace);
            itemView.setOnLongClickListener(startNewView);
            this.ctx = ctx;
        }
        protected View.OnClickListener toggleCardFace = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String toSetText = checkCardFlip();
                wordViewText.setText(toSetText);
            }
        };

        protected View.OnLongClickListener startNewView = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //navigate to a new View
                //get Navigation controller
                NavController navController = Navigation.findNavController(v);
                //pass the destination with .navigate(...)
                navController.navigate(R.id.action_checkDeckList_to_singleCard);
                //return true if the view consumed the long click as per documentation
                return true;
            }
        };

        public String checkCardFlip(){
            try {
                String backText = (String) card.getString("back");
                String frontText = (String) card.getString("front");
                String currentViewText = (String) wordViewText.getText();
                if (currentViewText.equals(backText)){
                    return frontText;
                }else if (currentViewText.equals(frontText)){
                    return backText;
                }
            } catch (JSONException e) {
                Log.e("cardViewholder json", e.getMessage());
            }
            return "null";
        };

        public void setJSONCard(JSONObject obj){
            this.card = obj;
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
                .inflate(R.layout.fragment_card_recycler_list_item, parent, false);


        DeckRecyclerViewHolder deckViewHolder = new DeckRecyclerViewHolder(
                singleCard,
                this,
                singleCard.getContext()
        );
        return deckViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DeckRecyclerViewHolder holder, int position) {
        //set value of recycler item
        try {
            //set the card at the position of linkedlist as json object for onclicklistener
            holder.setJSONCard(this.deckData.get(position));
            //get data at position from linkedlist, access the front of card and turn to text
            holder.wordViewText.setText(this.deckData.get(position).getString("front"));
        } catch (JSONException e) {
            Log.e("deckrecycler settext", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return this.deckData == null ? 0 : this.deckData.size();
    }
}
