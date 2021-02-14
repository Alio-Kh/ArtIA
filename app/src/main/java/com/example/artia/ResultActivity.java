package com.example.artia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class ResultActivity extends AppCompatActivity {
    //    Navigation Bar Buttons
    private ImageButton searchImgBtn;
    private TextView search;
    private ImageButton historyImgBtn;
    private TextView history;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Hide the Action bar
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        searchNavi();
        historyNavi();
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

    // To navigate from Result Activity to Search Activity (for pattern detection)
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
}