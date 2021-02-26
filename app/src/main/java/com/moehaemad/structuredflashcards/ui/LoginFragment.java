package com.moehaemad.structuredflashcards.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import java.util.LinkedList;
import java.util.Stack;

public class LoginFragment extends Fragment {
    private SharedPreferences appPreference;
    private final String PREF_NAME = "com.moehaemad.structuredflashcards";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // please refer to DeckFragment for explanation why attachToRoot = false
        View root = inflater.inflate(R.layout.fragment_login_menu, container, false);
        Button submitButton = root.findViewById(R.id.login_menu_submit);
        submitButton.setOnClickListener(this.submitListener);
        Button createUserButton = root.findViewById(R.id.login_menu_create_user);
        createUserButton.setOnClickListener(createAccountClick);

        //TODO change to user settings instead of login page on createView

        return root;
    }


    public String[] getUserInput (View v){
        String[] userInformation = new String[2];

        // get user information from login screen
        EditText usernameView = getView().findViewById(R.id.login_menu_username);
        EditText passwordView = getView().findViewById(R.id.login_menu_password);
        //push onto Stack
        userInformation [0] = usernameView.getText().toString();
        userInformation [1] = passwordView.getText().toString();


        return userInformation;
    }

    public void notifyProcess(Boolean verified){
        String userNotification = "";
        if (verified){
            userNotification = "User Authentication successful";
        }else{
            userNotification = "User Authentication failed";
        }
        //display the appropriate notification for authenticating user
        Toast.makeText(getContext(), userNotification, Toast.LENGTH_SHORT).show();
    }



    protected View.OnClickListener submitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String[] userInfo = getUserInput(v);

            //user setup object process in controller
            UserSetup userSetup = new UserSetup(getContext());
            //have controller check for user being verified with text input
            Boolean verified = userSetup.verifyUser(userInfo[0], userInfo[1]);

            notifyProcess(verified);
        }
    };

    protected View.OnClickListener createAccountClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            String[] userInfo = getUserInput(v);
            //create setup
            UserSetup userSetup = new UserSetup(getContext());
            Boolean verified = userSetup.createUser(userInfo[0], userInfo[1]);
            notifyProcess(verified);

        }
    };
}
