package com.moisegui.artia.ui.admin;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moisegui.artia.R;

public class Items {

    int    img_motif;
    String     lib_motif;

    public Items(int img_motif, String lib_motif) {
        this.img_motif = img_motif;
        this.lib_motif = lib_motif;
    }

    public int getImg_motif() {
        return img_motif;
    }

    public String getLib_motif() {
        return lib_motif;
    }
}
