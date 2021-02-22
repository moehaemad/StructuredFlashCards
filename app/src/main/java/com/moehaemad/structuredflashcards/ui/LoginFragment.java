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
        submitButton.setOnClickListener(this.buttonListener);

        return root;
    }

    protected View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("onClick button", "the onclick listener is working correctly");
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        EditText userInfo = getView().findViewById(R.id.login_menu_username);
        EditText passInfo = getView().findViewById(R.id.login_menu_password);


    }
}
