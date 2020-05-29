package com.example.abfinancials.ui.calculator;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.abfinancials.R;
import com.example.abfinancials.ui.dashboard.DashboardViewModel;

import java.text.DecimalFormat;

public class CalculatorFragment extends Fragment {

    private CalculatorViewModel calculatorViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.calculator_fragment, container, false);

        final TextView total_dividend_paid = (TextView) view.findViewById(R.id.total_dividend_paid);
        final TextView franking_credits = (TextView) view.findViewById(R.id.total_franked);

        final EditText dividend_amount = (EditText) view.findViewById(R.id.editText);
        final EditText franking = (EditText) view.findViewById(R.id.editText2);
        final EditText number_of_shares = (EditText) view.findViewById(R.id.editText3);

        Button calculateButton = (Button) view.findViewById(R.id.calculateButton);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double dividend_amount_result = Double.parseDouble(dividend_amount.getText().toString());
                double franking_result = Double.parseDouble(franking.getText().toString());
                double number_of_shares_result = Double.parseDouble(number_of_shares.getText().toString());

                double total_franked = ((dividend_amount_result / 100) * 0.3 / 0.7 * (franking_result / 100)) * number_of_shares_result;
                double total_div = (dividend_amount_result / 100) * number_of_shares_result;

                DecimalFormat df = new DecimalFormat("#.##");
                String format_total_div = df.format(total_div);
                String format_total_franked = df.format(total_franked);

                total_dividend_paid.setText(format_total_div);
                franking_credits.setText(format_total_franked);
            }
        });
        return view;

    }

}
