package com.moehaemad.structuredflashcards.model;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.moehaemad.structuredflashcards.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;

public class User {
    private String name;
    private int id;
    private int[] deckId;
    private HashMap<Integer, String> deck;

    public User(String name, int id){
        this.name = name;
        this.id = id;
        this.deckId = getDeckIds();
    }

    private int[] getDeckIds(){
    //    TODO: query database for array of deck ids attached to a user using user id
    //    TODO: create networkRequest object and retrieve the json data which will contain ids
        int[] ids = {};
        return ids;
    };



    private void pairDeckIdNames(){
    //    TODO: for each item in the deck id arrays attach the associated name to the hashmap
    //    TODO: take the data from getDeckIds and associate them to a HasMap 'deck' object
    }

    protected HashMap<Integer, String> getDecks(){
        //TODO: pair each deck id to it's associated string
        return this.deck;
    }

    protected View.OnClickListener checkUser = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            // NetworkRequest checkUser = new NetworkRequest(getApplicationContext());
            NetworkRequest checkUser = new NetworkRequest(null);
            checkUser.setMethod("GET");
            // EditText user = findViewById(R.id.login_menu_username);
            // create use here to be referenced most likely in ui not here
            //THIS IS PASTED CODE TO SAFELY REMOVE LOGINMENU ACTIVITY
            EditText user = new EditText(null);
            String username = user.getText().toString();
            // String password = findViewById(R.id.login_menu_password).getText().toString();
            checkUser.getRequest("https://moehaemad.ca/structuredFlashCards/checkUser/abc/abc",
                    new Network<JSONObject>() {
                        @Override
                        public void getResult(JSONObject object) {
                            // TextView result = findViewById(R.id.login_menu_network);
                            TextView result = new TextView(null);
                            try {
                                result.setText(object.getString("result"));
                            } catch (JSONException e) {
                                Log.e("error", "error accessing json object");
                            }
                        }
                    });
            //TODO: change view according to http request
            // TextView networkResult = findViewById(R.id.user_menu_network_result);
            // networkResult.setText("Something happened");
        }
    };

}
