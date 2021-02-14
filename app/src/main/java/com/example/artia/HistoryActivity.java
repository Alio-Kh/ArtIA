package com.example.artia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.artia.ui.login.LoginActivity;

public class HistoryActivity extends AppCompatActivity {

    //    Navigation Bar Buttons
    private ImageButton searchImgBtn;
    private ImageButton resultImgBtn;
    private TextView search;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Hide the Action bar
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }

        searchNavi();
        loginNavi();
    }

    // To navigate from History Activity to Search Activity (for pattern detection)
    private void searchNavi() {
        final Intent searchIntent = new Intent(this, SearchActivity.class);

//      When the User Click on the Search Icon (ImageButton) he will be navigated to the Search Activity
        searchImgBtn = findViewById(R.id.search_btn);
        searchImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(searchIntent);
            }
        });

//      When the User Click on the Search TextView he will be navigated to the Search Activity
        search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(searchIntent);
            }
        });

    }


    /********** not finished yet (c'est pout tester l'activité Result) ************/
    // To navigate from History Activity to Login Activity
    private void loginNavi() {
        final Intent resultIntent = new Intent(this, ResultActivity.class);

//      When the User Click on the Login Icon (ImageButton) he will be navigated to the Login Activity
        resultImgBtn = findViewById(R.id.result_btn);
        resultImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(resultIntent);
            }
        });

//      When the User Click on the Login TextView he will be navigated to the Login Activity
        result = findViewById(R.id.result_txt);
        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(resultIntent);
            }
        });
    }
}