package com.example.eventsincalendar;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.eventsincalendar.ui.home.HomeFragment;


public class CustomDialogFragmentOkNo extends DialogFragment {
    private DatabaseHelperEv mydb;


    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        assert getArguments() != null;
        Bundle args0 = new Bundle();
        mydb = new DatabaseHelperEv(getContext());

        String attention = getArguments().getString("attention");
        String sDate_new = getArguments().getString("sDate_new");
        String data = getArguments().getString("data");
        int id = getArguments().getInt("id");
        Boolean search= getArguments().getBoolean("search");

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        return builder
                .setTitle("ВНИМАНИЕ!")
                .setMessage(attention)
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CustomDialogFragmentOkNo fragment = new CustomDialogFragmentOkNo(); //Your Fragment
                        Bundle bundle = new Bundle();
                        if (id==0){
                            mydb.insertContact(sDate_new, String.valueOf(data), null,null,null,null, DatabaseHelperEv.TABLE);
                            Toast.makeText(getActivity(), "Событие внесено!", Toast.LENGTH_LONG).show();
                        }else {
                            mydb.deleteContact(id);
                            Toast.makeText(getActivity(), "Событие удалено!", Toast.LENGTH_LONG).show();

                            //удаляем запись и галочку
                            assert getParentFragment() != null;
                            ((HomeFragment) getParentFragment()).eventDelete();


                        }

                        bundle.putBoolean("down", true); // Key, value
                        fragment.setArguments(bundle);
                    }
                })
                .setNegativeButton("Нет",new DialogInterface.OnClickListener() {
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
