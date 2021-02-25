package com.moisegui.artia.ui.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.moisegui.artia.R;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Items> {

    private  int resource;

    public CustomAdapter(@NonNull Context context, int resource, @NonNull List<Items> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    public CustomAdapter(@NonNull Context context, int resource, @NonNull Items[] objects) {
        super(context, resource, objects);
        this.resource=resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(super.getContext());
        convertView = layoutInflater.inflate(this.resource,parent,false);
        ImageView imageView = convertView.findViewById(R.id.img_motif);
        TextView textView = convertView.findViewById(R.id.lib_motif);

        imageView.setImageResource(getItem(position).getImg_motif());
        textView.setText(getItem(position).getLib_motif());

        return convertView;
    }
}
