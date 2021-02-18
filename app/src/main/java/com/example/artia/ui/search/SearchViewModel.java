package com.example.artia.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchViewModel extends ViewModel {

    private MutableLiveData<CameraPreview> cameraPreview;

    public SearchViewModel() {
        cameraPreview = new MutableLiveData<>();
    }

    public LiveData<CameraPreview> getCameraPreview() {
        return cameraPreview;
    }
}