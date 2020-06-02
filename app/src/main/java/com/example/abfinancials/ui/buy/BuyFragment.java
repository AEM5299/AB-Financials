package com.example.abfinancials.ui.buy;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.abfinancials.R;

public class BuyFragment extends Fragment {
    TextView totalPrice;
    double sharePrice;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.buy, container, false);
        sharePrice = getArguments().getDouble("price", 0.00);

        totalPrice = root.findViewById(R.id.total_price);
        ((TextView) root.findViewById(R.id.price)).setText(Double.toString(sharePrice));
        ((EditText) root.findViewById(R.id.number_of_shares)).addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                final double num = parseDoubleWithDefaultValue(s.toString(), 0.0);
                totalPrice.setText(Double.toString(num * sharePrice));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        ((EditText) root.findViewById(R.id.number_of_shares)).setText("0");

        return root;
    }

    private Double parseDoubleWithDefaultValue(String s, Double d) {
        return s.isEmpty() || s == null? d : Double.parseDouble(s);
    }
}
