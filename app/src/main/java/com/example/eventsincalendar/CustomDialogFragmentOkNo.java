package com.example.eventsincalendar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class CustomDialogFragmentOkNo extends DialogFragment {

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        assert getArguments() != null;
        Bundle args0 = new Bundle();


        String attention = getArguments().getString("attention");

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        return builder
                .setTitle("ВНИМАНИЕ!")
                .setMessage(attention)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CustomDialogFragmentOkNo fragment = new CustomDialogFragmentOkNo(); //Your Fragment
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("down", true); // Key, value
                        fragment.setArguments(bundle);

                    }
                })
                .setNegativeButton("Отмена",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CustomDialogFragmentOkNo fragment = new CustomDialogFragmentOkNo(); //Your Fragment
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("down", false); // Key, value
                        fragment.setArguments(bundle);
                    }
                })
                .create();
    }
}
