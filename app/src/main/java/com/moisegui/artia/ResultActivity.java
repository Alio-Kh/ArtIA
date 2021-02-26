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
    private TextView pattern;
    private TextView desc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        image = findViewById(R.id.image_result_fragment);
        title = findViewById(R.id.title_result_fragment);


        pattern = findViewById(R.id.pattern_result_fragment);
        desc = findViewById(R.id.desc_result_fragment);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int image_data = bundle.getInt("image", -1);
            String title_data = bundle.getString("title");
            String date_data = bundle.getString("date");
            String origin_ = "origin";
            String pattern_ = "pattern";
            String desc_ = bundle.getString("desc");
            image.setImageResource(image_data);
            title.setText(title_data);

            pattern.setText(title_data);
            desc.setText(desc_);
        }



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