package com.moisegui.artia.filters.ar;

import com.moisegui.artia.filters.NoneFilter;

public class NoneARFilter extends NoneFilter implements ARFilter {
    @Override
    public float[] getGLPose() {
        return null;
    }
}
