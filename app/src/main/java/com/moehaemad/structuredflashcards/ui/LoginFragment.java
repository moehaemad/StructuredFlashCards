package com.moehaemad.structuredflashcards.ui;

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

public class LoginFragment extends Fragment {

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
            // get user information from login screen
            EditText usernameView = getView().findViewById(R.id.login_menu_username);
            EditText passwordView = getView().findViewById(R.id.login_menu_password);

            //user setup object process in controller
            UserSetup userSetup = new UserSetup(getContext());
            //have controller check for user being verified with text input
            Boolean verified = userSetup.verifyUser(usernameView.getText().toString(),
                    passwordView.getText().toString());

            //display the appropriate notification for authenticating user
            Toast userNotif = new Toast(getContext());
            userNotif.setDuration(Toast.LENGTH_SHORT);
            userNotif.setText(!verified ? "User Authentication failed" : "User Authentication successful");
            userNotif.show();
        }
    };
}
