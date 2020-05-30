package com.example.abfinancials.ui.my_stocks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.abfinancials.entities.WatchList;

import java.util.List;

public class MyStocksViewModel extends ViewModel {

    private MutableLiveData<List<WatchList>> mWatchList;

    public MyStocksViewModel() {
        mWatchList = new MutableLiveData<>();
    }

    public LiveData<List<WatchList>> getWatchList() { return mWatchList; }

    public void setWatchList(List<WatchList> l) {
        this.mWatchList.postValue(l);
    }
}
