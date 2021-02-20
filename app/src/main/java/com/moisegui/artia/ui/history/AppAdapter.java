package com.moisegui.artia.ui.history;

import android.app.Activity;
import android.app.Dialog;
import android.app.Instrumentation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.moisegui.artia.HomeActivity;
import com.moisegui.artia.RegisterActivity;
import com.moisegui.artia.ResultActivity;
import com.moisegui.artia.ui.result.ResultFragment;
import com.moisegui.artia.ui.history.ViewItems;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import com.moisegui.artia.R;

public class AppAdapter extends BaseAdapter {

    HomeActivity homeActivity = new HomeActivity();

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

        items.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
                builder.setTitle("delete the item "+titles[position]+" from history?") ;
                builder.setMessage("Are you sure you want to delete it from your history ?") ;
                builder.setIcon(images[position]);
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }) ;
                builder.setPositiveButton("accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });
        items.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*LayoutInflater factory = LayoutInflater.from(context);
                final View view_ = factory.inflate(R.layout.item_history, null);
                Dialog dialog = new Dialog(context);
                dialog.setContentView(view_);
                dialog.show();*/
                homeActivity.goToResult(images[position],titles[position],dates[position]);
            }
        });
        items.image.setImageResource(images[position]);
        items.title.setText(titles[position]);
        items.date.setText("Date : "+dates[position]);
        return view;
    }
}