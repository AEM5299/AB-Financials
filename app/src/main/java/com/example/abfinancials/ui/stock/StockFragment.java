package com.example.abfinancials.ui.stock;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.abfinancials.WatchListDatabase;
import com.example.abfinancials.R;
import com.example.abfinancials.dao.WatchListDao;
import com.example.abfinancials.entities.WatchList;
import com.example.abfinancials.models.Stock;

import org.json.JSONException;
import org.json.JSONObject;

public class StockFragment extends Fragment {
    private StockViewModel stockViewModel;
    WatchListDao dao;

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
        final TextView symbol = root.findViewById(R.id.company_symbol);
        final TextView price = root.findViewById(R.id.company_name);
        final TextView name = root.findViewById(R.id.company_title_name);
        final TextView volume = root.findViewById(R.id.company_value_volume);
        final TextView high = root.findViewById(R.id.company_value_high);
        final TextView low = root.findViewById(R.id.company_value_low);
        stockViewModel.getStock().observe(getViewLifecycleOwner(), new Observer<Stock>() {
            @Override
            public void onChanged(@Nullable Stock s) {
                symbol.setText(s.symbol);
                price.setText(String.format("$%.2f", s.price));
                name.setText("Working on name...");
                volume.setText(String.valueOf(s.volume));
                high.setText(String.valueOf(s.high));
                low.setText(String.valueOf(s.low));
            }
        });

        // TODO: NOW YOU CAN ADD A STOCK TO YOUR WATCHLIST MORE THAN ONCE. NEEDS TO BE FIXED.
        root.findViewById(R.id.addToWatchList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddToWatchListTask().execute(stockViewModel.getStock().getValue());
            }
        });

        getStockData(getArguments().getString("symbol", null));

        final StockFragment self = this;
        ((Button) root.findViewById(R.id.buy_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putDouble("price", stockViewModel.getStock().getValue().price);
                bundle.putString("symbol", stockViewModel.getStock().getValue().symbol);

                NavHostFragment.findNavController(self).navigate(R.id.action_stockFragment2_to_buyFragment, bundle);
            }
        });

        return root;
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
}
