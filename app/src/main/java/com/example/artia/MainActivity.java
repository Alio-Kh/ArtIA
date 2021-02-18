package com.example.artia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Get Started Button
    private Button getStartedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide the Action bar
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }

        // To navigate from Main Acticity to Search Activity (for pattern detection)
        getStarted();
    }

    // To navigate from Main Acticity to Search Activity (for pattern detection)
    public void getStarted() {
        final Intent searchAct = new Intent(this, HomeActivity.class);
        getStartedBtn = findViewById(R.id.get_started);
        getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(searchAct);
            }
        });

    }
}