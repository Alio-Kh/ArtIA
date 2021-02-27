package com.moisegui.artia.filters;

import org.opencv.core.Mat;

import java.util.Map;

public interface Filter {
//    public abstract void apply(final Mat src, final Mat dst);

    public Map<String, Object> recherche(final Mat src, Mat dst);
}

