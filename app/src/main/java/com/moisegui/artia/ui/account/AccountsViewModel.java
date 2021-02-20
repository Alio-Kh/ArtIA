package com.moisegui.artia.ui.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccountsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AccountsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Your are not connected");
    }

    public LiveData<String> getText() {
        return mText;
    }

}