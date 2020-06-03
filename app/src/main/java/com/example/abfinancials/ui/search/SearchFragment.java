package com.example.abfinancials.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.abfinancials.R;
import com.example.abfinancials.models.SearchResultStock;
import com.example.abfinancials.utils.StockAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    ArrayList<SearchResultStock> items = new ArrayList<>();
    private ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchViewModel searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        listView = root.findViewById(R.id.stock_search_result);
        StockAdapter stocksAdapter = new StockAdapter(getContext(), items);
        listView.setAdapter(stocksAdapter);

        final SearchView searchView = root.findViewById(R.id.search_field);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                RequestQueue queue = Volley.newRequestQueue(getContext());
                String url = String.format("https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords=%s&apikey=%s",
                query, "7497KD4TIL4ECWOO");

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                JSONArray results = response.optJSONArray("bestMatches");
                                items.clear();
                                items.addAll(SearchResultStock.fromJson(results));
                                listView.invalidateViews();
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                queue.add(jsonObjectRequest);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        final Fragment self = this;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchResultStock selectedItem = (SearchResultStock) parent.getItemAtPosition(position);

                Bundle bundle = new Bundle();
                bundle.putString("symbol", selectedItem.symbol);
                bundle.putString("name", selectedItem.name);

                NavHostFragment.findNavController(self).navigate(R.id.action_navigation_home_to_stockFragment2, bundle);
            }
        });


        return root;
    }
}
