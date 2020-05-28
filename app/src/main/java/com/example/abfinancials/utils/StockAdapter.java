package com.example.abfinancials.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.abfinancials.R;
import com.example.abfinancials.models.SearchResultStock;

import java.util.ArrayList;

public class StockAdapter extends ArrayAdapter<SearchResultStock> {
    public StockAdapter(Context context, ArrayList<SearchResultStock> stocks) {
        super(context, 0, stocks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchResultStock stock = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.stock_search_row, parent, false);
        }
        TextView tvName = convertView.findViewById(R.id.stock_name);
        tvName.setText(stock.name);
        return convertView;
    }

}
