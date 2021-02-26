package com.moisegui.artia.filters;

import org.opencv.core.Mat;

import java.util.Map;

public class NoneFilter implements Filter {


    @Override
    public Map<String, Object> recherche(Mat src, Mat dst) {
        return null;
    }
}