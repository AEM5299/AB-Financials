package com.example.abfinancials.ui.watchlist;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.abfinancials.R;
import com.example.abfinancials.WatchListDatabase;
import com.example.abfinancials.dao.WatchListDao;
import com.example.abfinancials.entities.WatchList;

import java.util.ArrayList;
import java.util.List;

public class WatchlistFragment extends Fragment {

    private WatchlistViewModel dashboardViewModel;
    WatchListDao dao;
    List<WatchList> adapterList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dao = WatchListDatabase.getDatabase(getContext()).watchListDao();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(WatchlistViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard,
                container,
                false);
        final ArrayAdapter<WatchList> adapter = new WatchListAdapter(getContext(), adapterList);

        SwipeMenuListView swipeMenuListView = ((SwipeMenuListView) root.findViewById(R.id.watchlist));
        swipeMenuListView.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.baseline_delete_outline_white_18dp);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        swipeMenuListView.setMenuCreator(creator);

        swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                dao.delete(adapterList.get(position));
                            }
                        }).start();
                        break;
                }
                return false;
            }
        });

        dashboardViewModel.getWatchList().observe(getViewLifecycleOwner(), new Observer<List<WatchList>>() {
            @Override
            public void onChanged(List<WatchList> watchLists) {
                adapter.clear();
                adapterList.addAll(watchLists);
            }
        });

        dao.getAllLive().observe(getViewLifecycleOwner(), new Observer<List<WatchList>>() {
            @Override
            public void onChanged(List<WatchList> watchLists) {
                dashboardViewModel.setWatchList(watchLists);
            }
        });
        return root;
    }
}