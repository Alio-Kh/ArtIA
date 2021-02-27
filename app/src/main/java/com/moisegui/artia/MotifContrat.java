package com.moisegui.artia;

import android.provider.BaseColumns;

public class MotifContrat {
    private void MotifContrat() {
    }

    public static class MotifTable implements BaseColumns {
        // Table Name
        public static final String TABLE_NAME = "Motifs";

        // Table columns
        public static final String t = "t";
        public static final String w = "w";
        public static final String h = "h";
        public static final String pix = "pix";
        public static final String motifID = "motifID";
        public static final String motifName = "motifName";
        public static final String motifDescription = "motifDescription";
        public static final String motifImageSrc = "motifImageSrc";
    }
}
