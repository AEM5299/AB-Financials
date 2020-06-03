package com.example.abfinancials.ui.stock;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.abfinancials.models.Stock;

import org.json.JSONException;
import org.json.JSONObject;

public class StockViewModel extends ViewModel {
    private MutableLiveData<Stock> mStock = new MutableLiveData<>();

    public LiveData<Stock> getStock() { return mStock; }

    public void setStock(JSONObject stock) throws JSONException {
        mStock.setValue(new Stock(stock));
    }

    // This is a limitation due to the API. It doesn't return the name of company with quote request
    // So we need to pass the name from the search and just print it here
    public void setName(String name) {
        mStock.getValue().name = name;
        mStock.postValue(mStock.getValue());
//        mStock.notify();
    }
}
