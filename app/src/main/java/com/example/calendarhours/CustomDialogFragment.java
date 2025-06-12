package com.example.calendarhours;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class CustomDialogFragment extends DialogFragment {

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String attention = getArguments().getString("attention");
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        return builder
                .setTitle("ВНИМАНИЕ!")
                .setMessage(attention)
                .setPositiveButton("OK", null)
                .setNegativeButton("",null)



                .create();
    }
}
