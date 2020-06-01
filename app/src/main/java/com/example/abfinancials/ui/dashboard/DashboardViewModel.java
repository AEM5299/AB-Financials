package com.example.abfinancials.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.abfinancials.entities.WatchList;

import java.util.List;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<List<WatchList>> mWatchList;

    public DashboardViewModel() {
        mWatchList = new MutableLiveData<>();
    }

    public LiveData<List<WatchList>> getWatchList() { return mWatchList; }

    public void setWatchList(List<WatchList> l) {
        this.mWatchList.postValue(l);
    }
}