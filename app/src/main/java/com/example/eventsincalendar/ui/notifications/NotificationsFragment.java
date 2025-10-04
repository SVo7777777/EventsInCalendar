package com.example.eventsincalendar.ui.notifications;
import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.eventsincalendar.CustomDialogFragment;
import com.example.eventsincalendar.DatabaseHelperEv;
import com.example.eventsincalendar.MyWidget2;
import com.example.eventsincalendar.R;
import com.example.eventsincalendar.databinding.FragmentNotificationsBinding;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private DatabaseHelperEv mydb;
    CalendarView calendarView;
    public EditText textMultiline;
    public Button buttonAdd;
    public Button buttonReset;
    boolean addRecord;
    public String chosesData;
    private int ch_d;
    private int ch_m;
    private int ch_y;
    Calendar calendar = Calendar.getInstance();
    Calendar ci = Calendar.getInstance();
    public int current_year = calendar.get(Calendar.YEAR);
    public int current_month = calendar.get(Calendar.MONTH);
    public int current_day = calendar.get(Calendar.DATE);
    String current_data = "_"+ current_day +"-"+ (current_month + 1) +"-"+ current_year;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        calendarView = root.findViewById(R.id.calendarView10);
        textMultiline = root.findViewById(R.id.editTextTextMultiLine8);
        buttonAdd = root.findViewById(R.id.buttonAdd);
        buttonReset = root.findViewById(R.id.buttonReset);
        clickAdd(buttonAdd);
        clickReset(buttonReset);
        addRecord = false;

        //обновление виджета
        Intent intentq = new Intent(getActivity(), MyWidget2.class);
        intentq.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        int[] ids = AppWidgetManager.getInstance(getActivity().getApplication()).getAppWidgetIds(new ComponentName(getActivity().getApplication(), MyWidget2.class));
        intentq.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        getActivity().sendBroadcast(intentq);

        mydb = new DatabaseHelperEv(getContext());
        //вывод текущей даты в поле информации при запуске приложения
        currentData();
        //вывод даты  в поле информации при нажатии на календаре
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                addRecord = true;
                ch_d = dayOfMonth;
                ch_m = month;
                ch_y = year;

                @SuppressLint("SimpleDateFormat")
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(ch_y, ch_m, ch_d);
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdf1 = new SimpleDateFormat("EE dd-MM-yyyy");
                Date date1 = calendar1.getTime();
                String data1 =  sdf1.format(date1);//дата в базе
                System.out.println(data1);
                boolean search = mydb.checkDataExistOrNot(data1, DatabaseHelperEv.TABLE);
                String ev = mydb.getEvents(data1, DatabaseHelperEv.TABLE);

                @SuppressLint("SimpleDateFormat")
                final SimpleDateFormat sdf = new SimpleDateFormat("EE dd-MM-yyyy");
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                String sDate = sdf.format(calendar.getTime());
                if (search){
                    textMultiline.setText(Html.fromHtml("<font color=\"#0000FF\">" + data1  + ": " +"</font>" + ev));

                }else {
                    chosesData = sDate;
                    //цвет даты 006400-зелёный
                    textMultiline.setText(Html.fromHtml("<font color=\"#0000FF\">" + sDate + ": " +"</font>"));
                    //фокус в конце даты
                    textMultiline.requestFocus();
                    textMultiline.setSelection(textMultiline.getText().length());
                }
            }
        });
        return root;
    }
    private void clickAdd(Button button2) {
        button2.setOnClickListener(v -> {
            try {


                if (!addRecord) {
                    Toast.makeText(getContext(), "Выберите дату, запишите событие, а потом внесите! ", Toast.LENGTH_LONG).show();
                } else {
                    String data = String.valueOf(textMultiline.getText());
                    System.out.println(data);
                    String day = data.substring(0, 10 + 3);
                    String event = data.substring(12 + 3);

//                @SuppressLint("SimpleDateFormat")
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(ch_y, ch_m, ch_d);
//
//                @SuppressLint("SimpleDateFormat")
//                SimpleDateFormat sdf1 = new SimpleDateFormat("EE dd-MM-yyyy");
//                Date date1 = calendar.getTime();
//                String data1 =  sdf1.format(date1);//дата в базе
//                System.out.println(data1);
                    if (data.length() > 16) {
                        //mydb = new DatabaseHelperEv(getContext());
                        boolean search = mydb.checkDataExistOrNot(day, DatabaseHelperEv.TABLE);
                        System.out.println("search: " + search);
                        if (search) {
                            Toast.makeText(getActivity(), " Событие добавлено!", Toast.LENGTH_SHORT).show();
                            System.out.println(data + " уже есть!");
                            String ev = mydb.getEvents(day, DatabaseHelperEv.TABLE);

                            int id = mydb.GetId(day, DatabaseHelperEv.TABLE);
                            boolean update_events = mydb.updateEvents(id, day, event, DatabaseHelperEv.TABLE);
                            if (update_events) {
                                System.out.println("событие изменено");
                            }

                            System.out.println("за " + day + " событие: " + ev);

                        } else {
                            System.out.println(day);
                            System.out.println(event);
                            mydb.insertContact(day, event, null, null, null, null, DatabaseHelperEv.TABLE);
                            String attention = "За " + day + "\nдобавлено событие:\n" + event;


                            CustomDialogFragment dialog = new CustomDialogFragment();
                            Bundle args = new Bundle();
                            args.putString("attention", attention);
                            dialog.setArguments(args);
                            dialog.show(getParentFragmentManager(), "custom");
                        }
                        //addRecord = false;
                    } else {
                        Toast.makeText(getContext(), "Запишите событие, а потом внесите! ", Toast.LENGTH_LONG).show();
                    }
                }

            }catch (StringIndexOutOfBoundsException e){
                System.out.println(e);
                Toast.makeText(getContext(), "Выберите дату, запишите событие, а потом внесите! ", Toast.LENGTH_LONG).show();
            }

        });
    }
    private void clickReset(Button button2) {
        button2.setOnClickListener(v -> {
            addRecord = false;
            textMultiline.setText("");
            //курсор в конце строки
            textMultiline.requestFocus();
            textMultiline.setSelection(textMultiline.getText().length());
            //currentData();

        });
    }
    public void currentData(){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format0 = new SimpleDateFormat("EE dd-MM-yyyy");
        String today0 = format0.format(ci.getTime());
        System.out.println(today0);
        boolean search = mydb.checkDataExistOrNot(today0, DatabaseHelperEv.TABLE);
        String ev = mydb.getEvents(today0, DatabaseHelperEv.TABLE);
        if (search){
            textMultiline.setText(Html.fromHtml("<font color=\"#0000FF\">" + today0  + ": " +"</font>" + ev));
            addRecord = true;
        }else {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy EEEE");
            System.out.println(format.format(ci.getTime()));
            String today = format.format(ci.getTime());
            // цвет даты
            //textMultiline.setText(Html.fromHtml("<font color=\"#006400\">" + today  + "</font>"));
            CharSequence hint = textMultiline.getHint();
            String s = "Сегодня " + today + ". " + hint;
            textMultiline.setHint(s);

            //курсор в конце строки
            textMultiline.requestFocus();
            textMultiline.setSelection(textMultiline.getText().length());
        }
    }
        @Override
        public void onDestroyView () {
            super.onDestroyView();
            binding = null;
    }
}
