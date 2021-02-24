package com.moehaemad.structuredflashcards.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.moehaemad.structuredflashcards.R;
import com.moehaemad.structuredflashcards.controller.UserSetup;

public class LoginFragment extends Fragment {

    public void LoginFragment(){
    //    TODO: get user input from here
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // please refer to DeckFragment for explanation why attachToRoot = false
        View root = inflater.inflate(R.layout.fragment_login_menu, container, false);
        Button submitButton = root.findViewById(R.id.login_menu_submit);
        submitButton.setOnClickListener(this.submitListener);

        return root;
    }


    protected View.OnClickListener submitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("onClick button", "the onclick listener is working correctly");
            //TODO: get username and password EditText values
            EditText usernameView = getView().findViewById(R.id.login_menu_username);
            EditText passwordView = getView().findViewById(R.id.login_menu_password);
            UserSetup userSetup = new UserSetup(getContext());
        //    TODO: create an instance of userSetup with the login and password
            Boolean verification = userSetup.verifyUser(usernameView.getText().toString(),
                    passwordView.getText().toString());
        //    TODO: save into bundle
            Log.d("submit button", "inside of button and verification " + verification.toString());
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        EditText userInfo = getView().findViewById(R.id.login_menu_username);
        EditText passInfo = getView().findViewById(R.id.login_menu_password);


    }
}
