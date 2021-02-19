package com.moisegui.artia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.moisegui.artia.ui.login.LoginActivity;

public class SearchActivity extends AppCompatActivity {

    //    Navigation Bar Buttons
    private ImageButton loginImgBtn;
    private ImageButton historyImgBtn;
    private TextView login;
    private TextView history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Hide the Action bar
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }

        historyNavi();
        loginNavi();
    }

    // To navigate from Search Activity to History Activity (to see detected patterns )
    private void historyNavi() {
        final Intent historyIntent = new Intent(this, HistoryActivity.class);

//      When the User Click on the History Icon (ImageButton) he will be navigated to the History Activity
        historyImgBtn = findViewById(R.id.histo_btn);
        historyImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(historyIntent);
            }
        });

//      When the User Click on the History TextView he will be navigated to the History Activity
        history = findViewById(R.id.history);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(historyIntent);
            }
        });

    }


    /********** not finished yet ************/
    // To navigate from Search Activity to Login Activity
    private void loginNavi() {
        final Intent loginIntent = new Intent(this, LoginActivity.class);

//      When the User Click on the Login Icon (ImageButton) he will be navigated to the Login Activity
        loginImgBtn = findViewById(R.id.login_btn);
        loginImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(loginIntent);
            }
        });

//      When the User Click on the Login TextView he will be navigated to the Login Activity
        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(loginIntent);
            }
        });
    }


}