package com.example.abfinancials.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchResultStock {
    public String name;

    public SearchResultStock(JSONObject stock) throws JSONException {
        this.name = stock.getString("2. name");
    }

    public static ArrayList<SearchResultStock> fromJson(JSONArray jsonObjects) {
        ArrayList<SearchResultStock> stocks = new ArrayList<>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                stocks.add(new SearchResultStock(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return stocks;
    }
}
