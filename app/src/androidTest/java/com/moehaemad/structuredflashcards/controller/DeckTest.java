package com.moehaemad.structuredflashcards.controller;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;
import androidx.test.core.app.ApplicationProvider;

import com.moehaemad.structuredflashcards.model.Preferences;
import com.moehaemad.structuredflashcards.model.WebsiteInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class DeckTest {
    private Deck mDeck;
    private SharedPreferences mPreferences;
    private Context mContext;
    private Map<String, String> prefKeys;
    private UserSetup mUserSetup;
    private final String user = "testUser";
    private final String password = "testUser";

    /**
     * Setup
     * */

    @Before
    public void setup (){
        this.mContext = ApplicationProvider.getApplicationContext();
        this.mPreferences = this.mContext.getSharedPreferences(
                Preferences.PACKAGE,
                Context.MODE_PRIVATE
        );
        this.mDeck = new Deck(this.mContext);
        //use this to remove all variables in each teardown
        this.prefKeys = new HashMap<>();
        this.prefKeys.put("deckIds", Preferences.DECK_ARRAY);
        this.prefKeys.put("username", Preferences.USER_NAME);
        this.prefKeys.put("userSetupValidated", Preferences.USER_VALIDATED);
        //setup the user to create it into the database
        this.createUser();
    }

    private void createUser(){
        this.mUserSetup = new UserSetup(this.mContext,
                this.user,
                this.password);
        this.mUserSetup.createUser();
    }



    @Test
    public synchronized void constructorSyncNoUser_noIds(){
        this.removeKeys();
        //give information to the Deck context without a username
        //this should result in an error
        //invoking sync Deck without a username which should not change preferences DECK_ARRAY
        this.mDeck.setUsername("");
        this.mDeck.syncDeck();
        //check that no DeckId's are set in sharedPreferences
        boolean noIds = this.mPreferences.getString(Preferences.DECK_ARRAY, "noDef") ==
                "";
        assertTrue(noIds);
    }

    @Test
    public synchronized void constructorSyncUser_ids(){
        this.removeKeys();
        //set username to one that exists without necessarily having deck ids in it
        this.mDeck.setUsername(this.user);
        this.mDeck.syncDeck();
        //check that no DeckId's are set in sharedPreferences
        boolean noIds = this.mPreferences.getString(Preferences.DECK_ARRAY, "noDef") ==
                "";
        assertTrue(noIds);
    }

    /**
     * TESTING Creating Ids.
     * */

    //create deck ids given non empty json data and make it append. This is to check api is working.
    @Test
    public synchronized void createId_Preferences(){
        //set previous value because teardown will have destroyed it
        SharedPreferences.Editor mEditor = this.mPreferences.edit();
        mEditor.putString(Preferences.DECK_ARRAY,"[1]");
        mEditor.apply();
        //make request to create an id
        this.mDeck.createId(2);
        //check if the value updated in preferences has appended the create id value
        try {
            JSONArray createResult = new JSONArray(this.mPreferences.getString(Preferences.DECK_ARRAY,
                    "[]"));
            //check if the preferences are now updated
            assertEquals(createResult, new JSONArray("[1, 2]"));
        } catch (JSONException e) {
            //if error thrown just say test is false or not passed
            assert(false);
        }
    }

    //create deck ids given no username, expect error to be thrown
    @Test(expected = Error.class)
    public synchronized void createId_noUsername(){
        this.mDeck.setUsername("");
        //expect error to be throws if creating an id without a username attached
        this.mDeck.createId(0);
    }

    //create deck ids given not registered username in api, expect empty preferences
    @Test
    public synchronized void createId_invalidUsername(){
        this.mDeck.setUsername(" blank ");
        this.mDeck.createId(10);
        /*no change will have happened to shared preferences so assert it's still empty string from
            teardown*/
        assertEquals("", this.mPreferences.getString(Preferences.DECK_ARRAY, ""));
    }

    //boring: create ids with valid user
    @Test
    public synchronized void createId_validUsername(){
        this.mDeck.createId(1);
        //check in shared preferences which will be changed on the network request
        JSONArray actualPreferences = null;
        try {
            actualPreferences = new JSONArray(this.mPreferences.getString(
                    Preferences.DECK_ARRAY,
                    "[]"));
            JSONArray expectedPreferences = new JSONArray("[1]");
            assertEquals(expectedPreferences, actualPreferences);
        } catch (JSONException e) {
            //if error thrown just say test is false or not passed
            assert(false);
        }
    }

    /**
     * TESTING setDeck method and getDeck method.
     * */

    //give deck ids nothing in preferences, expect empty JSONArray. testing setDeck
    @Test
    public synchronized void setDeck_emptyArray(){
        //take out all shared preferences
        this.removeKeys();
        //instantiate deck constructor to trigger setting deck since it's private
        this.mDeck = new Deck(this.mContext);
        assertEquals(this.mDeck.getDeckIds(), new JSONArray());
    }


    //test getDeck ids with null deck array before being able to call setDeck,
    //  expect empty JSONArray
    @Test
    public synchronized void getDeck_noIds(){
        JSONArray mArray;
        //empty out shared preferences so Deck constructor not valid
        this.removeKeys();
        //don't have to instantiate contructor since it will be done as intended in setup.
        mArray = this.mDeck.getDeckIds();
        assertEquals(mArray, new JSONArray());
    }

    //test getDeck non-empty ids by calling setDeck, expect the same ids as set
    //TODO: implement feature or stub at very least
    @Test
    public synchronized void getDeck_ids(){
        try {
            //create id to add into shared preferences
            //don't call methods since this is the way it will be parsed from prefences (i.e. String)
            JSONArray mArray = new JSONArray("[{id: 1}]");
            //change shared preferences without needing to access api for robust test.
            //if api working correctly (independently) then preferences will be changed.
            SharedPreferences.Editor mEditor = this.mPreferences.edit();
            mEditor.putString(Preferences.DECK_ARRAY, "[{id: 1}]");
            mEditor.apply();
            //create new Deck object to trigger change for setting deck since it's private.
            this.mDeck = new Deck(this.mContext);
            JSONArray toCheckArray = this.mDeck.getDeckIds();
            /*
            * JSONArray not invoking equals method despite equal values in array so convert to
            *   String.
            * */
            String expected = mArray.toString();
            String actual = toCheckArray.toString();
            assertEquals(expected, actual);
        } catch (JSONException e) {
            assert(false);
        }
    }

    /**
     * TESTING getDeckIdsAsString
     * */
    //set DeckIds from setDeckIds to  be empty, expect empty LinkedList<String>
    @Test
    public synchronized void getDeckIdsAsString_emptyList(){
        //teardown will have made preferences empty so use as default
        LinkedList<String> actualList = this.mDeck.getDeckIdsAsString();
        assertEquals(new LinkedList<String>(), actualList);
    }

    //set DeckIds with JSONArray with setDeckIds, expect LinkedList<String> of values put in array
    @Test
    public synchronized void getDeckIdsAsString_List(){
        //setup shared preferences to include deck ids
        SharedPreferences.Editor mEditor = this.mPreferences.edit();
        mEditor.putString(Preferences.DECK_ARRAY, "[{id: 1}]");
        mEditor.apply();
        LinkedList<String> expectedList = new LinkedList<>();
        expectedList.add("1");
        assertEquals(expectedList, this.mDeck.getDeckIdsAsString());
    }

    /**
     * Teardown
     * */

    private void removeKeys(){
        //change deck ids to be empty, which means not set
        SharedPreferences.Editor mEditor = this.mPreferences.edit();
        //for each values used in shared preferences, tear all down
        for (String value : this.prefKeys.values()){
            mEditor.remove(value);
        }
        mEditor.apply();
    }

    @After
    public synchronized void teardown(){
        this.removeKeys();
    }

}
