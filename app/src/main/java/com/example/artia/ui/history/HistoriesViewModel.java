package com.example.artia.ui.history;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HistoriesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HistoriesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is history fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}