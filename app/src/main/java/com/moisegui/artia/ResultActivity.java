package com.moisegui.artia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    private ImageView image;
    private TextView title;
    private TextView date;
    private TextView origin;
    private TextView pattern;
    private TextView desc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        image = findViewById(R.id.image_result_fragment);
        title = findViewById(R.id.title_result_fragment);


        origin = findViewById(R.id.origin_result_fragment);
        pattern = findViewById(R.id.pattern_result_fragment);
        desc = findViewById(R.id.desc_result_fragment);

        String origin_ = "origin";
        String pattern_ = "pattern";
        String desc_ = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim i Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. d est laborum.";

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int image_data = bundle.getInt("image", -1);
            String title_data = bundle.getString("title");
            String date_data = bundle.getString("date");

            image.setImageResource(image_data);
            title.setText(title_data);
        }

        origin.setText(origin_);
        pattern.setText(pattern_);
        desc.setText(desc_);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.back, menu);
        getSupportActionBar().setTitle(R.string.Result);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_back_button) {
            this.finish();
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }

}