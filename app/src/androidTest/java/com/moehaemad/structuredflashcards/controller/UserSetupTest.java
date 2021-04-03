package com.moehaemad.structuredflashcards.controller;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;
import androidx.test.core.app.ApplicationProvider;

import com.moehaemad.structuredflashcards.model.Preferences;
import com.moehaemad.structuredflashcards.model.WebsiteInterface;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class UserSetupTest {
    private Context mContext;
    private SharedPreferences sharedPreferences;
    private UserSetup mUserSetup;
    private final String testPackage = "com.moehaemad.testing";


    @Before
    public void setupContextAndSharedPreferences(){
        this.mContext = ApplicationProvider.getApplicationContext();
        /*not sure how to inject shared preferences into UserSetup object so will change the
        *   default shared Preferences object the UserSetup.java class is accessing. Creating
        *   a separate shared preferences not registered because SharedPrefences accesses by
        *   name and not solely by context.*/
        this.sharedPreferences = this.mContext.getSharedPreferences(Preferences.PACKAGE,
                Context.MODE_PRIVATE);

        /**
         * This code is for testing user setup by attempting to use Mockito Spy. For now
         *  sharedPreferences are messy and need to be encapsulated in a single class. Resuming
         *  testing with the default package.
         * */
/*        this.sharedPreferences = this.mContext.getSharedPreferences(testPackage,
                Context.MODE_PRIVATE);*/
//        UserSetup userSetupPreSpy = new UserSetup(this.mContext);
        //setup spy to stub one method
//        this.mUserSetup = spy(userSetupPreSpy);
//        when (mUserSetup.getAppPreferences(this.mContext)).thenReturn(this.sharedPreferences);
        this.mUserSetup = new UserSetup(this.mContext);

    }

    @Test
    public synchronized void checkIfUserExists_False(){
        //create user in shared preferences
        SharedPreferences.Editor editPref =  this.sharedPreferences.edit();
        editPref.putString(Preferences.USER_NAME, "");
        editPref.apply();
        //create user in here given the provided context
        Boolean userExists = this.mUserSetup.doesUserExist();
        //setup User
        assertFalse(userExists);
    }

    @Test
    public void checkIfUserExists_True(){
//        this.mUserSetup = mock(UserSetup.class);
        SharedPreferences.Editor editPref =  this.sharedPreferences.edit();
        editPref.putString(WebsiteInterface.USER_NAME, "TestUser");
        editPref.apply();
        //create user in here given the provided context
        Boolean userExists = this.mUserSetup.doesUserExist();
        //setup User
        assertTrue(userExists);
    }

    @Test
    public void createUser_True(){
        //uninsteresting case, just create user
        this.mUserSetup = new UserSetup(this.mContext, "testUser", "testUser");
        assertTrue(this.mUserSetup.createUser());
    }

    @Test(expected = Error.class)
    public void createUser_Exception(){
        this.mUserSetup = new UserSetup(this.mContext, "", "");
        this.mUserSetup.createUser();
    }


    @After
    public synchronized void teardown(){
        SharedPreferences.Editor myEditor = this.sharedPreferences.edit();
        myEditor.remove(WebsiteInterface.USER_NAME);
        myEditor.apply();
    }

}