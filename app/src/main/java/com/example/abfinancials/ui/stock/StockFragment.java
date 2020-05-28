package com.example.abfinancials.ui.stock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.abfinancials.R;
import com.example.abfinancials.models.Stock;

import org.json.JSONException;
import org.json.JSONObject;

public class StockFragment extends Fragment {
    private StockViewModel stockViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        stockViewModel =
                ViewModelProviders.of(this).get(StockViewModel.class);

        View root = inflater.inflate(R.layout.stock_info, container, false);
        final TextView symbol = root.findViewById(R.id.company_symbol);
        final TextView price = root.findViewById(R.id.company_name);
        stockViewModel.getStock().observe(getViewLifecycleOwner(), new Observer<Stock>() {
            @Override
            public void onChanged(@Nullable Stock s) {
                symbol.setText(s.symbol);
                price.setText(String.format("$%.2f", s.price));
            }
        });

        getStockData(getArguments().getString("symbol", null));

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
}
