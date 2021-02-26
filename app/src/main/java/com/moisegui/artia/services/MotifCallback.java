package com.moisegui.artia.services;

import com.moisegui.artia.data.model.Motif;

import java.util.List;

public interface MotifCallback {
    public void onCallback(List<Motif> motifs);
}
