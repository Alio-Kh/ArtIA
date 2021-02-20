package com.moisegui.artia;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class ShowPhoto extends AppCompatActivity {

    Button cancel;
    Button submit;
    ImageView img;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);
        cancel= findViewById(R.id.cancel);
        submit= findViewById(R.id.submit);
        img= findViewById(R.id.img);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null){
            String path=bundle.getString("path");

            Picasso.get()
                    .load(new File(path))
                    .fit().centerCrop()
                    .rotate(90)
                    .into(img);
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bundle!=null){
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bundle!=null){
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }
}