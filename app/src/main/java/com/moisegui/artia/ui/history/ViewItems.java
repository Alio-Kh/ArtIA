package com.moisegui.artia.ui.history;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.moisegui.artia.R;

public class ViewItems {
    ImageView image;
    TextView title;
    TextView date;
    Button btn1;
    Button btn2;

    ViewItems(View view) {
        image = view.findViewById(R.id.image_history_item);
        title = view.findViewById(R.id.title_history_item);
        date = view.findViewById(R.id.date_history_item);
        btn1 = view.findViewById(R.id.btn1_history_item);
        btn2 = view.findViewById(R.id.btn2_history_item);
    }
}