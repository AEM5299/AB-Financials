package com.example.abfinancials.ui.stock;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.abfinancials.MainActivity;
import com.example.abfinancials.R;
import com.example.abfinancials.WatchListDatabase;
import com.example.abfinancials.dao.WatchListDao;
import com.example.abfinancials.entities.WatchList;
import com.example.abfinancials.models.Stock;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StockFragment extends Fragment {
    private StockViewModel stockViewModel;
    WatchListDao dao;
    LineChart lineChart;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dao = WatchListDatabase.getDatabase(getContext()).watchListDao();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        stockViewModel =
                ViewModelProviders.of(this).get(StockViewModel.class);

        View root = inflater.inflate(R.layout.stock_info, container, false);
        ((MainActivity) getActivity()).setActionBarTitle(getArguments().getString("name", "No name"));
        final TextView symbol = root.findViewById(R.id.company_symbol);
        final TextView price = root.findViewById(R.id.latest_price);
        final TextView volume = root.findViewById(R.id.company_value_volume);
        final TextView high = root.findViewById(R.id.company_value_high);
        final TextView name = root.findViewById(R.id.company_title_name);
        final TextView low = root.findViewById(R.id.company_value_low);
        stockViewModel.getStock().observe(getViewLifecycleOwner(), new Observer<Stock>() {
            @Override
            public void onChanged(@Nullable Stock s) {
                name.setText(s.name);
                symbol.setText(s.symbol);
                price.setText(String.format("$%.2f", s.price));
                volume.setText(String.valueOf(s.volume));
                high.setText(String.format("$%.2f", s.high));
                low.setText(String.format("$%.2f", s.low));
                new CheckIfAddedToWatchList().execute(s.symbol);
            }
        });

        this.lineChart = root.findViewById(R.id.price_chart);
        this.lineChart.getDescription().setEnabled(false);
        this.lineChart.getLegend().setEnabled(false);

        getStockData(getArguments().getString("symbol", null));

        getHistoricalData(getArguments().getString("symbol", null));

        return root;
    }

    private void getHistoricalData(String symbol)
    {
        if (symbol == null) {
            Toast.makeText(getContext(), "Something is wrong", Toast.LENGTH_LONG).show();
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = String.format("https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY&symbol=%s&apikey=%s",
                symbol, "7497KD4TIL4ECWOO");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject s = response.getJSONObject("Monthly Time Series");
                            float[] val_arr = new float[12];
                            String[] months_arr = new String[12];
                            for(int i = 0; i < val_arr.length; i++) {
                                val_arr[val_arr.length - 1 - i] = (float) ((JSONObject) s.get(s.names().getString(i))).getDouble("4. close");
                                months_arr[val_arr.length - 1 - i] = s.names().getString(i).substring(0, 7);
                            }
                            updateChart(val_arr, months_arr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        queue.add(jsonObjectRequest);
    }

    private void updateChart(float[] priceList, String[] months)
    {
        LineDataSet data = new LineDataSet(getArrayList(priceList), "data");
        LineData lineData = new LineData(data);
        XAxis xAxis = this.lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
        this.lineChart.setData(lineData);
        this.lineChart.invalidate();
    }

    private ArrayList<Entry> getArrayList(float[] priceList) {
        ArrayList<Entry> data = new ArrayList<>();
        for (int i = 0; i < priceList.length; i++) {
            data.add(new Entry(i, priceList[i]));
        }
        return data;
    }

    private void updateWatchlistIcon(Boolean v)
    {
        final ImageButton btn = getView().findViewById(R.id.addToWatchList);
        if (!v) {
            btn.setImageResource(R.drawable.baseline_add_black_18dp);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AddToWatchListTask().execute(stockViewModel.getStock().getValue());
                    btn.setImageResource(R.drawable.baseline_remove_black_18dp);
                }
            });
        } else {
            btn.setImageResource(R.drawable.baseline_remove_black_18dp);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DeleteFromWatchListTask().execute(stockViewModel.getStock().getValue().symbol);
                    btn.setImageResource(R.drawable.baseline_add_black_18dp);
                }
            });
        }
        btn.setVisibility(View.VISIBLE);
    }

    private void getStockData(@Nullable String symbol) {
        if (symbol == null) {
            Toast.makeText(getContext(), "Something is wrong", Toast.LENGTH_LONG).show();
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = String.format("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=%s&apikey=%s",
                symbol, "7497KD4TIL4ECWOO");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            stockViewModel.setStock(response.getJSONObject("Global Quote"));
                            stockViewModel.setName(getArguments().getString("name", "Something"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        queue.add(jsonObjectRequest);
    }

    private class AddToWatchListTask extends AsyncTask<Stock, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Stock... stocks) {
            try {
                dao.insert(new WatchList(stocks[0]));
            } catch (Exception x) {
                return false;
            }
           return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                Toast.makeText(getContext(), "Added to your watchlist", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "We couldn't add it", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class CheckIfAddedToWatchList extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            int v = dao.isExist(strings[0]);
            return v != 0;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            updateWatchlistIcon(aBoolean);
        }
    }

    private class DeleteFromWatchListTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... symbol) {
            try {
                dao.delete(symbol[0]);
            } catch (Exception x) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                Toast.makeText(getContext(), "Removed from your watchlist", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "We couldn't remove it", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
