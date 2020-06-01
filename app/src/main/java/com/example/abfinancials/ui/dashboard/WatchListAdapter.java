package com.example.abfinancials.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.abfinancials.R;
import com.example.abfinancials.entities.WatchList;

import java.util.List;

public class WatchListAdapter extends ArrayAdapter<WatchList> {

    public WatchListAdapter(@NonNull Context context, List<WatchList> list) {
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        WatchList w = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.watchlist_item, parent, false);
        }

        TextView symbolView = convertView.findViewById(R.id.symbol_view);

        symbolView.setText(w.symbol);

        return convertView;
    }
}
