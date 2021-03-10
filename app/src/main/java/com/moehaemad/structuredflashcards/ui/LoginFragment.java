package com.moehaemad.structuredflashcards.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.moehaemad.structuredflashcards.R;
import com.moehaemad.structuredflashcards.controller.UserSetup;
import com.moehaemad.structuredflashcards.model.UserInput;

public class LoginFragment extends Fragment implements UserInput {
    private SharedPreferences appPreference;
    private final String PREF_NAME = "com.moehaemad.structuredflashcards";

    private static String login = "" ;
    private static String password = "";
    protected UserSetup userSetup;
    private View rootView;
    // no user will be defined as default in app
    private boolean setUserSettings = false;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //TODO change to user settings instead of login page on createView
        //TODO: check if user exists in shared Preferences (used in UserSetup.java)
        //TODO: receive boolean and send the appropriate fragment

        int layoutId = setUserSettings();
        this.rootView = inflater.inflate(layoutId, container, false);

        this.setAppropriateListeners();

        return this.rootView;
    }

    private void setAppropriateListeners(){
        //if the user setup
        if (!this.setUserSettings) this.setupLoginListeners();
    }

    private void setupLoginListeners (){
        Button submitButton = this.rootView.findViewById(R.id.login_menu_submit);
        submitButton.setOnClickListener(this.submitListener);
        Button createUserButton = this.rootView.findViewById(R.id.login_menu_create_user);
        createUserButton.setOnClickListener(createAccountClick);
    }

    private int setUserSettings(){
        if (this.userSetup == null) this.userSetup = new UserSetup(getContext());
        boolean userExists = this.userSetup.doesUserExist();
        this.setUserSettings = userExists;
        return userExists ? R.layout.fragment_user_settings : R.layout.fragment_user_login;
    }


    @Override
    public void notifyProcess(@NonNull Boolean verified) {
        String userNotification = "";
        if (verified){
            userNotification = "User Authentication successful";
        }else{
            userNotification = "User Authentication failed";
        }
        //display the appropriate notification for authenticating user
        Toast.makeText(getContext(), userNotification, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setUserInput(View v) {
        // get user information from login screen
        EditText usernameView = getView().findViewById(R.id.login_menu_username);
        EditText passwordView = getView().findViewById(R.id.login_menu_password);

        // set username information
        this.login = usernameView.getText().toString();
        this.password = passwordView.getText().toString();
        this.userSetup = new UserSetup(getContext(), this.login, this.password);
    }

    protected View.OnClickListener submitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //get the editText input if changed from construction; most likely changed
            setUserInput(v);
            //have controller check for user being verified with text input
            Boolean verified = userSetup.verifyUser();

            notifyProcess(verified);
        }
    };

    protected View.OnClickListener createAccountClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            //get the editText input if changed from construction; most likely changed
            setUserInput(v);
            Boolean verified = userSetup.createUser();
            notifyProcess(verified);

        }
    };
}
