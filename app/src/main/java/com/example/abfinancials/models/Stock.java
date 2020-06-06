package com.example.abfinancials.models;

import org.json.JSONException;
import org.json.JSONObject;


public class Stock {
    public String name, change_percent, symbol;

    public double price, open, high, low, change, previous_close;

    public int volume;

    public Stock(JSONObject obj) throws JSONException {
        if (obj.has("01. symbol")) {
            this.symbol = obj.getString("01. symbol");
        }

        if (obj.has("05. price")) {
            this.price = Double.parseDouble(obj.getString("05. price"));
        }

        if (obj.has("02. open")) {
            this.open = Double.parseDouble(obj.getString("02. open"));
        }

        if (obj.has("03. high")) {
            this.high = Double.parseDouble(obj.getString("03. high"));
        }

        if (obj.has("04. low")) {
            this.low = Double.parseDouble(obj.getString("04. low"));
        }

        if (obj.has("06. volume")) {
            this.volume = Integer.parseInt(obj.getString("06. volume"));
        }

        if (obj.has("08. previous close")) {
            this.previous_close = Double.parseDouble(obj.getString("08. previous close"));
        }

        if (obj.has("09. change")) {
            this.change = Double.parseDouble(obj.getString("09. change"));
        }

        if (obj.has("10. change percent")) {
            this.change_percent = obj.getString("10. change percent");
        }
    }
}
