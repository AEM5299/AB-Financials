package com.example.abfinancials.ui.my_stocks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyStocksViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyStocksViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is mystocks fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}