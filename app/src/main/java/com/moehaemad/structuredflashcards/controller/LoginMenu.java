package com.moehaemad.structuredflashcards;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.moehaemad.structuredflashcards.model.UserRequests;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Button createRequest = findViewById(R.id.login_menu_network);
        createRequest.setOnClickListener(checkUser);
    }

    private Boolean checkUserInfo(){
        return true;
    }

    protected View.OnClickListener checkUser = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            FlashCard getUser = new FlashCard(getApplicationContext());
            getUser.setMethod("GET");
            EditText user = findViewById(R.id.login_menu_username);
            String username = user.getText().toString();
            // String password = findViewById(R.id.login_menu_password).getText().toString();
            getUser.getRequest("https://moehaemad.ca/structuredFlashCards/checkUser/abc/abc",
                    new UserRequests<JSONObject>() {
                        @Override
                        public void getResult(JSONObject object) {
                            TextView result = findViewById(R.id.login_menu_network);
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
