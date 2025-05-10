package com.example.calendarhours.ui.notifications;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.calendarhours.DatabaseHelper;
import com.example.calendarhours.R;
import com.example.calendarhours.databinding.FragmentNotificationsBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private DatabaseHelper mydb ;
    LinearLayout number;
    LinearLayout data;
    LinearLayout hours;
    LinearLayout salary;
    TextView num;
    TextView dat;
    TextView hou;
    TextView sal;
    String[] split;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        number = root.findViewById(R.id.number);
        data = root.findViewById(R.id.data);
        hours = root.findViewById(R.id.hours);
        salary = root.findViewById(R.id.salary);

        mydb = new DatabaseHelper(getContext());
        ArrayList<ArrayList<String>>  str = mydb.getAllRows();
        System.out.println(str);
        System.out.println(str.get(0).get(0));
        System.out.println(str.get(0).get(1));
        int size = str.size();
        System.out.println(size);
        int j = 10;
        for (int i = 0; i < size; i++) {
            num=new TextView(getContext());
            dat=new TextView(getContext());
            hou=new TextView(getContext());
            sal=new TextView(getContext());
            String da = str.get(i).get(0);
            String sa = str.get(i).get(3);
            String h = str.get(i).get(2);
            num.setText(String.valueOf(i+1));
            num.setGravity(Gravity.CENTER);
            dat.setText(da);
            dat.setGravity(Gravity.CENTER);
            hou.setText(h);
            hou.setGravity(Gravity.CENTER);
            hou.setId(i+1);
            sal.setText(sa);
            sal.setId(j+1);
            sal.setGravity(Gravity.CENTER);
            number.addView(num);
            data.addView(dat);
            hours.addView(hou);
            salary.addView(sal);
            j += 1;

        }

//        for (ArrayList<String> list : str) {
//            System.out.println("размер массива: "+list.size());
//        }
//        Collections.sort(str);
//        System.out.println(Arrays.toString(str.toArray()));


        //final TextView textView = binding.textNotifications;
        //notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }
    public String summerHours(String h) {
        split = h.split("-");
        System.out.println(Arrays.toString(split));
        int d = 1;
        float sum = 0.0F;
        for (int i = 0; i < 42; i++) {
            if (split[d].equals(".")) {
                System.out.println("point");
            } else {
                sum += Float.parseFloat(split[d]);
            }
            d += 1;
        }
        System.out.println(sum);
        return String.valueOf(sum);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}