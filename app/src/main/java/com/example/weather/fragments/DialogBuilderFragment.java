package com.example.weather.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.weather.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class DialogBuilderFragment extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dialog, container, false);

        setCancelable(false);

        view.findViewById(R.id.btnOk).setOnClickListener(view1 -> dismiss());

        return view;
    }

}
