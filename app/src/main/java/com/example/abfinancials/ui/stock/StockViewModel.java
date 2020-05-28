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
}
