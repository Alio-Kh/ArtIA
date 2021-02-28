package com.moisegui.artia;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class ResultActivity extends AppCompatActivity {

    private ImageView image;
    private TextView title;
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
            String image_data = bundle.getString("image");
            String title_data = bundle.getString("title");
            String desc_ = bundle.getString("desc");
            Picasso.get().load(image_data).placeholder(R.drawable.mx_bg_gradient1).into(image);
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