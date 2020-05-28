package com.example.abfinancials.ui.calculator;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abfinancials.R;
import com.example.abfinancials.ui.dashboard.DashboardViewModel;

public class CalculatorFragment extends Fragment {

    private CalculatorViewModel calculatorViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        calculatorViewModel =
                ViewModelProviders.of(this).get(CalculatorViewModel.class);
        View root = inflater.inflate(R.layout.calculator_fragment, container, false);
//        final TextView textView = root.findViewById(R.id.text_calculator);
        calculatorViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });
        return root;
    }

}
