package com.moehaemad.structuredflashcards.controller;

import android.content.Context;
import android.os.Bundle;
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
import com.moehaemad.structuredflashcards.model.UserInput;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Creates the Recycler data to bind to the view and sets the behavior for single card transition.
 * */
public class DeckRecyclerAdapter extends RecyclerView.Adapter<DeckRecyclerAdapter
                                                                      .DeckRecyclerViewHolder>{
    private LinkedList<JSONObject> deckData;
    private int deckId;
    private LayoutInflater viewInflator;
    private Context ctx;

    /**
     * Just receiving data, set to class variable.
     * */
    public DeckRecyclerAdapter (@NonNull LinkedList<JSONObject> data, int id){
        this.deckData = data;
        this.deckId = id;
    }

    /**
     * Intended constructor, pass in Context and data to be viewed.
     * */
    public DeckRecyclerAdapter (@Nullable Context appCtx, @NonNull LinkedList<JSONObject> data, @NonNull int id){
        this(data, id);
        this.ctx = appCtx;
        //not necessarily needed but will consider for calls from other activities
        this.viewInflator = LayoutInflater.from(appCtx);
    }

    /**
     * Sets the required View holder for when the Recycler View is called on a single data index.
     * */
    public class DeckRecyclerViewHolder extends RecyclerView.ViewHolder{
        //Need and is referenced outside of view holder
        public TextView wordViewText;
        public DeckRecyclerAdapter adapter;
        public JSONObject card;
        private Context ctx;
        private String frontFace;
        private String backFace;
        /**
         * Constructor that sets the button listeners and class variables to be used.
         * */
        public DeckRecyclerViewHolder(@NonNull View itemView, DeckRecyclerAdapter adapter, Context ctx){
            super(itemView);
            this.adapter = adapter;
            //grab the text for use outside of view holder
            wordViewText = itemView.findViewById(R.id.deck_list_recycler_item_single);
            //set onClick listeners
            itemView.setOnClickListener(toggleCardFace);
            itemView.setOnLongClickListener(startNewView);
            //set Context for class variable
            this.ctx = ctx;
        }

        /**
         * Grab the json data for the front and back faces of each card.
         *
         * Will happen after you set the JSON object on the onBindViewHolder. Cannot have text
         *  without the JSON data to display.
         * */
        private void setViewText(){
            try {
                this.backFace = (String) card.getString("back");
                this.frontFace = (String) card.getString("front");
            } catch (JSONException e) {
                Log.e("cardViewholder json", e.getMessage());
            }
        }

        /**
         * Toggle between front and back.
         * */
        protected View.OnClickListener toggleCardFace = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String toSetText = checkCardFlip();
                wordViewText.setText(toSetText);
            }
        };

        /**
         * Navigates to the single card view given the bundle information for the selected card.
         * */
        protected View.OnLongClickListener startNewView = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //navigate to a new View
                //get Navigation controller
                NavController navController = Navigation.findNavController(v);
                //create a bundle to pass in the data from the view
                Bundle cardData = new Bundle();
                setBundle(cardData);
                //pass the destination with .navigate(...)
                navController.navigate(R.id.action_checkDeckList_to_singleCard, cardData);
                //return true if the view consumed the long click as per documentation
                return true;
            }
        };

        /**
         * Setup the values the bundle will pass when navigating to single card view.
         * */
        private void setBundle(Bundle cardBundle){
            //use the outer class variable deckId to pass the bundle with the appropriate information.
            cardBundle.putInt(UserInput.BUNDLE_DECK_ID, deckId);
            //set the data from the json object passed for front and back of each card
            cardBundle.putString(UserInput.BUNDLE_FRONT, this.frontFace);
            cardBundle.putString(UserInput.BUNDLE_BACK, this.backFace);
        };

        /**
         * Checks which face the card is at at the moment and flips between either one.
         * */
        public String checkCardFlip(){
            String currentViewText = (String) wordViewText.getText();
            if (currentViewText.equals(this.backFace)){
                return this.frontFace;
            }else if (currentViewText.equals(this.frontFace)){
                return this.backFace;
            }
            return "null";
        };

        /**
         * Used for convenience so outer bind function can set the data in view holder to toggle card.
         * */
        public void setJSONCard(JSONObject obj){
            this.card = obj;
            //sets class variables to show
            setViewText();
        }
    }

    /**
     * Inflate the view for the view holder single item using the context given to adapter via ViewGroup.
     * */
    @NonNull
    @Override
    public DeckRecyclerAdapter.DeckRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                         int viewType) {
        /*setup single item to be inflated and passed to view holder because view holder will
        *   be expecting the inflated single view. The fragment will already have inflated the
        *   recycler view so that doesn't need to be inflated*/
        View singleCard = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_card_recycler_list_item, parent, false);

        //Setup the view holder for a single item in the recycler view after layout inflated.
        DeckRecyclerViewHolder deckViewHolder = new DeckRecyclerViewHolder(
                singleCard,
                this,
                singleCard.getContext()
        );
        return deckViewHolder;
    }

    /**
     * Set the data or retrieve it from the single item in the recycler view.
     * */
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

    /**
     * Required method to return size of the recycler view data.
     * */
    @Override
    public int getItemCount() {
        return this.deckData == null ? 0 : this.deckData.size();
    }
}
