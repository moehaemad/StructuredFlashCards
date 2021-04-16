package com.moehaemad.structuredflashcards.controller;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;

import com.moehaemad.structuredflashcards.model.Preferences;
import com.moehaemad.structuredflashcards.model.WebsiteInterface;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class FlashCardTest {
    private SharedPreferences mPreferences;
    private Context mContext;
    private FlashCard mFlashCard;
    private LinkedList<String> prefKeys;
    private Deck mDeck;
    private UserSetup mUser;
    private final String testUser = "testUser";
    private final String testPass = "testPass";

    /**
     * Create a user
     * */
    @BeforeClass
    public void setupUserInformation(){
        this.mContext = ApplicationProvider.getApplicationContext();
        this.mUser = new UserSetup(this.mContext,
                this.testUser,
                this.testPass);
        //create a username in the api with the given username and password
        this.mUser.createUser();
        //setup deck information for user
        this.mDeck = new Deck(this.mContext, this.testUser);
        this.mDeck.createId(0, "test Deck");
        this.mDeck.createId(1, "test Deck");

    }

    @Before
    public void setUp() throws Exception {
        this.mContext = ApplicationProvider.getApplicationContext();
        this.mPreferences = this.mContext.getSharedPreferences(Preferences.PACKAGE,
                Context.MODE_PRIVATE);
        this.mFlashCard = new FlashCard(this.mContext);
        this.prefKeys = new LinkedList();
        this.prefKeys.add(Preferences.CARDS_ARRAY);
        //setup user with decks as well
    }

    /**
     * boring case: Create an active id for the user and test that it's set in shared preferences.
     * */
    @Test
    public void setActiveId_normal(){
        //create flash card object with value of id to be 0
        this.mFlashCard = new FlashCard(this.mContext, 0);
        //should set the active id in the preferences
        this.mFlashCard.setActiveId(1);

    }

    @After
    public void tearDown() throws Exception {
        this.removeKeys();
    }

    /**
     * Teardown
     * */
    private void removeKeys(){
        //change deck ids to be empty, which means not set
        SharedPreferences.Editor mEditor = this.mPreferences.edit();
        //for each values used in shared preferences, tear all down
        for (String value : this.prefKeys){
            mEditor.remove(value);
        }
        mEditor.apply();
    }
}