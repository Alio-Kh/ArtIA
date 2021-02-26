package com.moisegui.artia.filters;

import org.opencv.core.Mat;

import java.util.Map;

public class NoneFilter implements Filter {

    @Override
    public void apply(final Mat src, final Mat dst) {
        // Do nothing.

    }

    @Override
    public Map<String, Mat> recherche(Mat src, Mat dst) {
        return null;
    }
}