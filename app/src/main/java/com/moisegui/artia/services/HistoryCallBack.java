package com.moisegui.artia.services;

import com.moisegui.artia.data.model.History;

import java.util.List;

public interface HistoryCallBack {
    public void onCallback(List<History> histories);
}
