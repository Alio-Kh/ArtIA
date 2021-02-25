package com.moisegui.artia.ui.history;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.moisegui.artia.R;
import com.moisegui.artia.ResultActivity;

public class AppAdapter extends BaseAdapter {

    HistoryFragment historyFragment = new HistoryFragment();


    Context context;
    int[] images;
    String[] titles;
    String[] dates;

    public AppAdapter(Context context, String[] titles, int[] images, String[] dates) {
        this.context = context;
        this.images = images;
        this.titles = titles;
        this.dates = dates;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewItems items = null;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.item_history, null);
        items = new ViewItems(view);

        items.cancel_button_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
                builder.setTitle(context.getString(R.string.delete_the_item) + titles[position] + context.getString(R.string.from_your_history));
                builder.setMessage(R.string.verify_delete);
                builder.setIcon(images[position]);
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });
        items.more_button_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(context);
                Intent intent = new Intent(context, ResultActivity.class);
                intent.putExtra("image", images[position]);
                intent.putExtra("title", titles[position]);
                intent.putExtra("date", dates[position]);

                context.startActivity(intent);

            }
        });
        items.image_history_item.setImageResource(images[position]);
        items.title_history_item.setText(titles[position]);
        items.date_history_item.setText(context.getString(R.string.date) + " : " + dates[position]);
        return view;
    }


}