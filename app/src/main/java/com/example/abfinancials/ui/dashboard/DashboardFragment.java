package com.example.abfinancials.ui.dashboard;

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

import com.example.abfinancials.R;
import com.example.abfinancials.WatchListDatabase;
import com.example.abfinancials.dao.WatchListDao;
import com.example.abfinancials.entities.WatchList;
import com.example.abfinancials.models.Stock;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

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
        ((ListView) root.findViewById(R.id.watchlist)).setAdapter(adapter);
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