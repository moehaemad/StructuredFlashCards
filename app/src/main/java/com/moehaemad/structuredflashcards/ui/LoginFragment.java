package com.moehaemad.structuredflashcards.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.moehaemad.structuredflashcards.R;
import com.moehaemad.structuredflashcards.controller.UserSetup;
import com.moehaemad.structuredflashcards.model.UserInput;



public class LoginFragment extends Fragment implements UserInput {
    private static String login = "" ;
    private static String password = "";
    protected UserSetup userSetup;
    private View rootView;
    // no user will be defined as default in app
    private boolean setUserSettings = false;

    /**
     * Setup the user from the context.
     * */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.userSetup = new UserSetup(getContext());

    }


    /**
     * inflate the view from the appropriate view depending on if the user is logged in.
     * */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //grab the layout id that is appropriate to the user being logged in or not
        int layoutId = setUserSettings();
        //inflate the view
        this.rootView = inflater.inflate(layoutId, container, false);
        //set the listeners for the layout that will be shown
        this.setAppropriateListeners();

        return this.rootView;
    }

    /**This will create button listeners if user is logged in.*/
    private void setAppropriateListeners(){
        //if setUserSettings is true then set listeners for user settings otherwise login setup
        if (this.setUserSettings) {
            this.setUserInformation();
        } else {
            this.setupLoginListeners();
        }

    }

    /**This will implement the listeners given the rootView is a login menu.*/
    private void setupLoginListeners (){
        Button submitButton = this.rootView.findViewById(R.id.login_menu_submit);
        submitButton.setOnClickListener(this.submitListener);
        Button createUserButton = this.rootView.findViewById(R.id.login_menu_create_user);
        createUserButton.setOnClickListener(createAccountClick);
    }

    /**
     * This will implement the listeners given the rootView is a user settings menu.
     * */
    private void setUserInformation(){
        //The user setup object will be created in onCreate
        String username = this.userSetup.getUsername();
        TextView usernameView = this.rootView.findViewById(R.id.user_settings_username);
        //set the TextView of the user to be the username
        usernameView.setText(username);
    }

    /**This will return the layout depending on the user being logged in.*/
    private int setUserSettings(){
        //user setup will be created in onCreate
        //if (this.userSetup == null) this.userSetup = new UserSetup(getContext());
        boolean userExists = this.userSetup.doesUserExist();
        this.setUserSettings = userExists;
        return userExists ? R.layout.fragment_user_settings : R.layout.fragment_user_login;
    }

    /**
     * Add a progress toast message to the user depending on the network request.
     * */
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


    /**
     * Grab the user information so it is set as class variables to be accessed.
     * */
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

    /**
     * Add a button listener to submit a network request to very the user.
     * */
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


    /**
     * Add a button to send a network request to create a user.
     * */
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
