package com.example.abfinancials.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.abfinancials.models.Stock;

@Entity(tableName = "watchlist")
public class WatchList {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "company_name")
    public String companyName;

    @ColumnInfo(name = "symbol")
    public String symbol;

    @ColumnInfo(name = "latest_price")
    public Double latest_price;

    public WatchList(){}

    @Ignore
    public WatchList(Stock s) {
        companyName = s.name;
        symbol = s.symbol;
        latest_price = s.price;
    }
}
