package com.moisegui.artia.ui.history;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.moisegui.artia.R;
public class ViewItems {
    ImageView image_history_item;
    TextView title_history_item;
    TextView date_history_item;
    Button delete_button_history;
    Button more_button_history;

    ViewItems(View view) {
        image_history_item = view.findViewById(R.id.image_history_item);
        title_history_item = view.findViewById(R.id.title_history_item);
        date_history_item = view.findViewById(R.id.date_history_item);
        delete_button_history = view.findViewById(R.id.delete_button_admin);
        more_button_history = view.findViewById(R.id.update_button_admin);
    }
}