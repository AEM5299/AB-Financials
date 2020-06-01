package com.example.abfinancials.ui.dashboard;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
import com.example.abfinancials.models.Stock;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private static final String TAG = "DashboardFragment";

    private DashboardViewModel dashboardViewModel;
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
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard,
                container,
                false);
        final ArrayAdapter<WatchList> adapter = new WatchListAdapter(getContext(), adapterList);

//        ((ListView) root.findViewById(R.id.watchlist)).setAdapter(adapter);
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
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Log.d(TAG, "onMenuItemClick: clicked item" + index);
                        break;
                }
                // false : close the menu; true : not close the menu
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

        new getWatchedStocks().execute();
        return root;
    }

    private class getWatchedStocks extends AsyncTask<Void, Void, List<WatchList>> {

        @Override
        protected List<WatchList> doInBackground(Void... voids) {
            Log.i("info", "executing");
            List<WatchList> m = dao.getAll();
            return m;
        }

        @Override
        protected void onPostExecute(List<WatchList> watchLists) {
            Log.i("info", String.format("returned %d", watchLists.size()));
            dashboardViewModel.setWatchList(watchLists);
        }
    }
}