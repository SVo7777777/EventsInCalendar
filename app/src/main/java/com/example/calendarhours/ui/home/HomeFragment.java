package com.example.calendarhours.ui.home;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.graphics.Color.GRAY;
import static android.graphics.Color.LTGRAY;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.calendarhours.DatabaseHelper;
import com.example.calendarhours.R;
import com.example.calendarhours.databinding.FragmentHomeBinding;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    Button previous_month;
    public Button month;
    public Button year;
    LinearLayout view;
    //String[][] buttons = new String[3][3];
    LinearLayout[][] buttons= new LinearLayout[7][8];
    TextView[][] days= new TextView[7][8];
    TextView[][] events= new TextView[7][8];
    TextView[] number_of_week= new TextView[7];
    TextView text_home;

    String[] monthNames = new String[]{"ЯНВАРЬ", "ФЕВРАЛЬ", "МАРТ", "АПРЕЛЬ", "МАЙ", "ИЮНЬ", "ИЮЛЬ", "АВГУСТ", "СЕНТЯБРЬ", "ОКТЯБРЬ", "НОЯБРЬ", "ДЕКАБРЬ"};
    String[] day_of_weeks = new String[]{"","ПОНЕДЕЛЬНИК", "ВТОРНИК", "СРЕДА", "ЧЕТВЕРГ", "ПЯТНИЦА", "СУББОТА", "ВОСКРЕСЕНЬЕ"};
    Calendar calendar = Calendar.getInstance();
    public int current_year = calendar.get(Calendar.YEAR);
    public int current_month = calendar.get(Calendar.MONTH);
    public int current_day = calendar.get(Calendar.DATE);
    public int i_start = 0;
    public int j_start = 0;
    public int i_end = 0;
    public int j_end = 0;
    String[] split;
    private DatabaseHelper mydb ;
    private static final String APP_SD_PATH = "/data/data/com.example.calendarhours/files/hours.txt";




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        month = root.findViewById(R.id.month);
        year = root.findViewById(R.id.year);
        previous_month = root.findViewById(R.id.previous_month);
        text_home = root.findViewById(R.id.text_home);


        int e = 0;
        while (e < 7) {
            String weekId = "numweek_" + e;
            int weID = getResources().getIdentifier(weekId, "id", getActivity().getPackageName());
            number_of_week[e] = root.findViewById(weID);
            e++;
        }
        for (int i = 0; i < 7; i++)
            for (int j = 0; j < 8; j++) {
                //buttons[i][j] = "calendar_"+i+j;
                String llButtonId = "calendar_" + i + j;
                String dayId = "day_" + i + j;
                String eventId = "event_" + i + j;
                int bID = getResources().getIdentifier(llButtonId, "id", getActivity().getPackageName());
                int dID = getResources().getIdentifier(dayId, "id", getActivity().getPackageName());
                int eID = getResources().getIdentifier(eventId, "id", getActivity().getPackageName());
                //buttons[i][j] = String.valueOf(findViewById(gameID));
                buttons[i][j] = root.findViewById(bID);
                days[i][j] = root.findViewById(dID);
                events[i][j] = root.findViewById(eID);

            }
        String month3 = monthNames[current_month];
        System.out.println(month3);
        Calendar c = Calendar.getInstance();
        c.set(current_year, current_month, 1);
        int day_of_week = c.get(Calendar.DAY_OF_WEEK);
        int dateEnd = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println(dateEnd);
        int dayOfWeekOfFirstDayOfMonth = c.get(Calendar.DAY_OF_WEEK);
        System.out.println(dayOfWeekOfFirstDayOfMonth);
        int  week_of_year = c.get(Calendar.WEEK_OF_YEAR);
        System.out.println(week_of_year);
        //int day_of_week = calendar.getFirstDayOfWeek();
        System.out.println(day_of_week);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(calendar.getTime());
        String month2 = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("ru"));
        System.out.println(month2);

        calendar.add(Calendar.MONTH, -1);
        int max_pred = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println("max_pred="+max_pred);


        showCalendar(month3, current_year, week_of_year, max_pred, day_of_week, dateEnd, "");


        for (int i = 1; i < 7; i++)
            for (int j = 1; j < 8; j++) {
                try {
                    setOnClick(buttons[i][j], days[i][j], events[i][j], day_of_weeks[j]);
                    //ne: NullPointerException
                } catch (Exception ignored) {

                }

            }

//        for (int i = 1; i < 7; i++){
//            onWeekMonthClick(number_of_week[i]);
//        }i





        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }


    @SuppressLint("SetTextI18n")
    private void showCalendar(String mon, int yea, int wee, int mpred, int dayOfWeekOfFirstDayOfMonth, int dateEnd, String chas) {
        month.setText(mon);
        year.setText(Integer.toString(yea));
        String mont = String.valueOf(month.getText());
        String y = String.valueOf(year.getText());

        @SuppressLint("SimpleDateFormat")
        final SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");

        calendar.set(current_year, current_month, current_day);
        String sDate_now = sdf1.format(calendar.getTime());
        System.out.println("sDate_now="+sDate_now);
        if (dayOfWeekOfFirstDayOfMonth == 1){
            dayOfWeekOfFirstDayOfMonth = 8;
        }
        int m = mpred - dayOfWeekOfFirstDayOfMonth+3;
        int day_of_week = dayOfWeekOfFirstDayOfMonth;
        int ind = Arrays.asList(monthNames).indexOf(mon);
        if (ind == 0){
            ind = 11;
        }
        int d = 1;
        int d2 = 1;

        for (int i = 1; i < 7; i++) {
            number_of_week[i].setText(Integer.toString(wee));
            wee += 1;
            for (int j = 1; j < 8; j++) {
                if (i == 1 && j < dayOfWeekOfFirstDayOfMonth-1) {
                    buttons[i][j].setBackgroundColor(LTGRAY);
                    days[i][j].setText(Integer.toString(m));
                    buttons[i][j].setEnabled(false);
                    days[i][j].setTextColor(GRAY);
                    events[i][j].setText("");
                    m += 1;
                } else {
                    if (d < dateEnd + 1) {
                        days[i][j].setText(Integer.toString(d));
                        if (chas.isEmpty()){
                            events[i][j].setText("0");
                        }else {
                            split = chas.split("-");
                            System.out.println(Arrays.toString(split));


                        }
                        events[i][j].setTypeface(null, Typeface.BOLD);
                        buttons[i][j].setEnabled(true);
                        @SuppressLint("SimpleDateFormat")
                        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        Calendar c = Calendar.getInstance();
                        c.set(Integer.parseInt(year.getText().toString()), Arrays.asList(monthNames).indexOf((String) month.getText()), d);
                        String sDate = sdf.format(c.getTime());
                        if (j==6 || j == 7){
                            buttons[i][j].setBackgroundColor(buttons[i][j].getContext().getResources().getColor(R.color.weekend_day));
                            days[i][j].setTextColor(days[i][j].getContext().getResources().getColor(R.color.white));
                        }else {
                            buttons[i][j].setBackgroundColor(buttons[i][j].getContext().getResources().getColor(R.color.work_day));
                            events[i][j].setTextColor(events[i][j].getContext().getResources().getColor(R.color.red));
                            events[i][j].setTextSize(20);
                            days[i][j].setTextColor(days[i][j].getContext().getResources().getColor(R.color.Purple2));
                        }
                        if (sDate.equals(sDate_now)){
                            buttons[i][j].setBackgroundColor(buttons[i][j].getContext().getResources().getColor(R.color.DeepSkyBlue));
                        }
                        d += 1;
                    } else {
                        i_end = i;
                        j_end = j;
                        buttons[i][j].setBackgroundColor(LTGRAY);
                        days[i][j].setText(Integer.toString(d2));
                        days[i][j].setTextColor(GRAY);
                        buttons[i][j].setEnabled(false);
                        events[i][j].setText("");
                        d2 += 1;

                    }

                }

            }
        }
        addHoursFromBase(mont,y);
    }

    private void addHoursFromBase(String mont, String y){
        String hours = "";

        float sum = 0.0F;
        for (int i = 1; i < 7; i++) {
            for (int j = 1; j < 8; j++) {
                String h =(String) events[i][j].getText();
                if (!h.isEmpty()){
                    System.out.println(days[i][j].getText() + "-" + h);
                    sum += Float.parseFloat(h);
                    hours += "-"+h;

                }else{
                    System.out.println(days[i][j].getText() + "-" + ".");
                    hours += "-"+".";
                }
            }
        }
        System.out.println(hours);
        text_home.setText(String.valueOf(sum));
        String data = mont+" "+y;
        mydb = new DatabaseHelper(getContext());
        boolean search = mydb.checkDataExistOrNot(data);
        System.out.println("search: "+search);
        if (search) {
            Toast.makeText(getActivity(), data + " уже есть!", Toast.LENGTH_SHORT).show();
            String h = mydb.getHours(data);
            System.out.println("за "+data+" часы: "+h);
            addHoursInCalendar(h);
        }else {
            mydb.insertContact(data, hours);
            Toast.makeText(getActivity(), data + " добавлен!", Toast.LENGTH_SHORT).show();
        }

//        ArrayList<String> allRows = mydb.getAllRows();
//        System.out.println(allRows);
    }
    private void addHoursInCalendar(String h) {
        split = h.split("-");
        System.out.println(h);
        int d = 1;
        System.out.println("split[0=]"+split[0]);
        System.out.println("split[1=]"+split[1]);
        System.out.println(split[d]);
        System.out.println(Arrays.toString(split));
        for (int i = 1; i < 7; i++) {
            for (int j = 1; j < 8; j++) {
                if (split[d].equals(".")){
                    events[i][j].setText("");
                }else {
                    events[i][j].setText(split[d]);
                }

                d += 1;
            }
        }


    }

    public void onPreviousMonthClick1(View view)
    {
        String mon = (String) month.getText();
        int ind = Arrays.asList(monthNames).indexOf(mon);
        if (ind == 0){
            ind = 12;
        }
        String new_month = monthNames[ind-1];
        System.out.println(new_month);
        month.setText(new_month);
        int _year =Integer.parseInt( year.getText().toString());
        System.out.println(_year);
        System.out.println(new_month);
        System.out.println(ind-1);
        Calendar c = Calendar.getInstance();
        c.set(_year, ind-1, 1);
        int day_of_week = c.get(Calendar.DAY_OF_WEEK);
        System.out.println("day_of_week="+day_of_week);
        int dateEnd = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println(dateEnd);
        int  week_of_year = c.get(Calendar.WEEK_OF_YEAR);
        System.out.println(week_of_year);
        c.add(Calendar.MONTH, -1);
        int max_pred = c.getActualMaximum(Calendar.DAY_OF_MONTH);

        showCalendar(new_month, _year, week_of_year, max_pred, day_of_week, dateEnd,"");
    }
    public void onNextMonthClick(View view)
    {
        String mon = (String) month.getText();
        int ind = Arrays.asList(monthNames).indexOf(mon);
        if (ind == 11){
            ind = -1;
        }
        String new_month = monthNames[ind+1];
        System.out.println(new_month);
        month.setText(new_month);
        int _year =Integer.parseInt( year.getText().toString());
        System.out.println(_year);
        System.out.println(new_month);
        System.out.println(ind+1);
        Calendar c = Calendar.getInstance();
        c.set(_year, ind+1, 1);
        int day_of_week = c.get(Calendar.DAY_OF_WEEK);
        System.out.println(day_of_week);
        int dateEnd = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println(dateEnd);
        int  week_of_year = c.get(Calendar.WEEK_OF_YEAR);
        System.out.println(week_of_year);
        c.add(Calendar.MONTH, -1);
        int max_pred = c.getActualMaximum(Calendar.DAY_OF_MONTH);

        showCalendar(new_month, _year, week_of_year, max_pred, day_of_week, dateEnd,"");
    }
    public void onPreviousYearClick(View view)
    {
        String old_month = (String) month.getText();
        int ind = Arrays.asList(monthNames).indexOf(old_month);
        int _year =Integer.parseInt( year.getText().toString());
        int new_year = _year-1;
        System.out.println(_year);
        System.out.println(ind-1);
        Calendar c = Calendar.getInstance();
        c.set(new_year, ind, 1);
        int day_of_week = c.get(Calendar.DAY_OF_WEEK);
        System.out.println(day_of_week);
        int dateEnd = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println(dateEnd);
        int  week_of_year = c.get(Calendar.WEEK_OF_YEAR);
        System.out.println(week_of_year);
        c.add(Calendar.MONTH, -1);
        int max_pred = c.getActualMaximum(Calendar.DAY_OF_MONTH);

        showCalendar(old_month, new_year, week_of_year, max_pred, day_of_week, dateEnd,"");
    }
    public void onNextYearClick(View view)
    {
        String old_month = (String) month.getText();
        int ind = Arrays.asList(monthNames).indexOf(old_month);
        int _year =Integer.parseInt( year.getText().toString());
        int new_year = _year+1;
        System.out.println(_year);
        System.out.println(ind-1);
        Calendar c = Calendar.getInstance();
        c.set(new_year, ind, 1);
        int day_of_week = c.get(Calendar.DAY_OF_WEEK);
        System.out.println(day_of_week);
        int dateEnd = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println(dateEnd);
        int  week_of_year = c.get(Calendar.WEEK_OF_YEAR);
        System.out.println(week_of_year);
        c.add(Calendar.MONTH, -1);
        int max_pred = c.getActualMaximum(Calendar.DAY_OF_MONTH);


        showCalendar(old_month, new_year, week_of_year, max_pred, day_of_week, dateEnd,"");
    }

    private void setOnClick(LinearLayout btn, TextView day1, TextView event1,  String day_week) {
        btn.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                String mon = (String) month.getText();
                String ye = (String) year.getText();

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                view = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_review, null);
                EditText event = view.findViewById(R.id.editTextNumberDecimal);
                Button add = view.findViewById(R.id.button);
                Button close = view.findViewById(R.id.close);
                TextView number = view.findViewById(R.id.number);
                TextView all = view.findViewById(R.id.textView2);
                number.setText(day1.getText());
                TextView year1 = view.findViewById(R.id.year);
                Button num1 = view.findViewById(R.id.num1);
                Button num2 = view.findViewById(R.id.num2);
                Button num3 = view.findViewById(R.id.num3);
                Button num4 = view.findViewById(R.id.num4);
                Button num5 = view.findViewById(R.id.num5);
                Button num6 = view.findViewById(R.id.num6);
                Button num7 = view.findViewById(R.id.num7);
                Button num8 = view.findViewById(R.id.num8);
                Button num9 = view.findViewById(R.id.num9);
                Button num0 = view.findViewById(R.id.num0);
                Button point = view.findViewById(R.id.point);
                Button backspace = view.findViewById(R.id.backspace);


                year1.setText(year.getText().toString());
                TextView month1 = view.findViewById(R.id.month);
                month1.setText((String) month.getText());
                TextView day_of_weeks = view.findViewById(R.id.day_of_weeks);
                day_of_weeks.setText(day_week);

                @SuppressLint("SimpleDateFormat")
                final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Calendar c = Calendar.getInstance();
                c.set(Integer.parseInt(year.getText().toString()), Arrays.asList(monthNames).indexOf((String) month.getText()), Integer.parseInt((String) number.getText()));
                String sDate = sdf.format(c.getTime());
                String st = "";
                //event.setText(sDate+": ");
                all.setText(sDate+": ");
                event.requestFocus();
                event.setSelection(event.getText().length());
                //клавиатура выезжает сразу
                //showSoftKeyboard(event);
//                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
//                assert imm != null;
//                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                //imm.showSoftInput(event,InputMethodManager.SHOW_IMPLICIT);



                builder.setView(view);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                add.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View view) {
                        String data = String.valueOf(event.getText());
                        System.out.println("data="+data);
                        if (!data.isEmpty()){
                            event1.setText(data);
                            alertDialog.dismiss();
                            addHours();

                        }else {
                            Toast.makeText(getActivity(), "Введите часы!", Toast.LENGTH_LONG).show();

                        }
                        //hideKeyboard(event);

                        //imm.hideSoftInputFromWindow(event.getWindowToken(), 0);
                        //сохранение события в (базу данных) пока в текстовый файл
//                        if  (data.length() >= 20) {
//                            try (FileOutputStream fos = getContext().openFileOutput("event_diary.txt", Context.MODE_APPEND);
//                                 OutputStreamWriter osw = new OutputStreamWriter(fos)) {
//                                //String data = String.valueOf(textMultiline.getText());
//                                osw.write(sDate + ": " + data + "\n");
//                                //вывод диалогового окна, что запись внесена
//                                CustomDialogFragment dialog2 = new CustomDialogFragment();
//                                dialog2.show( getFragmentManager(), "custom");
//                            } catch (IOException e) {
//                                throw new RuntimeException(e);
//                            }
//                        }  else {
//                            Toast.makeText(getActivity(), "Запишите событие, а потом внесите! ", Toast.LENGTH_LONG).show();
//                        }
                        //Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();//display the text of button1
                    }

                    private void addHours() {
                        String month_year = mon + " " + ye;
                        StringBuilder hours = new StringBuilder();

                        float sum = 0.0F;
                        for (int i = 1; i < 7; i++) {
                            for (int j = 1; j < 8; j++) {
                                String h =(String) events[i][j].getText();
                                if (!h.isEmpty()){
                                    System.out.println(days[i][j].getText() + "-" + h);
                                    try {
                                        sum += Float.parseFloat(h);
                                        hours.append("-").append(h);
                                    } catch (NumberFormatException e) {
                                        Toast.makeText(getActivity(), "Это не число!", Toast.LENGTH_SHORT).show();
                                        hours.append("-").append("0");
                                    }


                                }else{
                                    System.out.println(days[i][j].getText() + "-" + ".");
                                    hours.append("-").append(".");
                                }


                            }
                       }
                        System.out.println(hours);
                        System.out.println(month_year+" "+hours);
                        int id = mydb.numberOfRows();
                        boolean updatehourse = mydb.updateHours(id, month_year, String.valueOf(hours));
                        if (updatehourse){
                            Toast.makeText(getActivity(), "Часы изменены!", Toast.LENGTH_SHORT).show();
                        }
                        text_home.setText(String.valueOf(sum));
                        //SaveNewHours.fileChangeLine1(month_year,hours);
//                        mydb = new DatabaseHelper(getContext());
//                        mydb.insertContact(mon+" "+ye, String.valueOf(hours));
//                        Toast.makeText(getActivity(), "Часы добавлены", Toast.LENGTH_SHORT).show();
//
//                        for (String allRow : mydb.getAllRows()) {
//                            System.out.println(allRow);
//                        }

//                        Cursor rs;
//                        rs = mydb.getData(1);
//                        rs.moveToFirst();



                    }


                });



                close.setOnClickListener(new  View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                num1.setOnClickListener(new  View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        event.setText(event.getText()+"1");
                        event.requestFocus();
                        event.setSelection(event.getText().length());
                    }
                });
                num2.setOnClickListener(new  View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        event.setText(event.getText()+"2");
                        event.requestFocus();
                        event.setSelection(event.getText().length());
                    }
                });
                num3.setOnClickListener(new  View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        event.setText(event.getText()+"3");
                        event.requestFocus();
                        event.setSelection(event.getText().length());
                    }
                });
                num4.setOnClickListener(new  View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        event.setText(event.getText()+"4");
                        event.requestFocus();
                        event.setSelection(event.getText().length());
                    }
                });
                num5.setOnClickListener(new  View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        event.setText(event.getText()+"5");
                        event.requestFocus();
                        event.setSelection(event.getText().length());
                    }
                });
                num6.setOnClickListener(new  View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        event.setText(event.getText()+"6");
                        event.requestFocus();
                        event.setSelection(event.getText().length());
                    }
                });
                num7.setOnClickListener(new  View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        event.setText(event.getText()+"7");
                        event.requestFocus();
                        event.setSelection(event.getText().length());
                    }
                });
                num8.setOnClickListener(new  View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        event.setText(event.getText()+"8");
                        event.requestFocus();
                        event.setSelection(event.getText().length());
                    }
                });
                num9.setOnClickListener(new  View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        event.setText(event.getText()+"9");
                        event.requestFocus();
                        event.setSelection(event.getText().length());
                    }
                });
                num0.setOnClickListener(new  View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        event.setText(event.getText()+"0");
                        event.requestFocus();
                        event.setSelection(event.getText().length());
                    }
                });
                point.setOnClickListener(new  View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        event.setText(event.getText()+".");
                        event.requestFocus();
                        event.setSelection(event.getText().length());
                    }
                });
                backspace.setOnClickListener(new  View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        String str = String.valueOf(event.getText());
                        if (!str.isEmpty()){
                            String newStr = str.substring(0, str.length() - 1);
                            event.setText(newStr);
                            event.requestFocus();
                            event.setSelection(event.getText().length());
                        }
                    }
                });

            }



            // Do whatever you want(str can be used here)

        });
    }
    /**
     * Hides the soft keyboard
     */
//    public void hideSoftKeyboard() {
//
//            InputMethodManager inputMethodManager = (InputMethodManager)getContext().getSystemService(INPUT_METHOD_SERVICE);
//            inputMethodManager.hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
//
//    }
    public void hideKeyboard(View v){
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
    /**
     * Shows the soft keyboard
     */
    public void onClickNumber() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

    }
    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = getContext().getSystemService(InputMethodManager.class);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void onPreviousMonthClick(View view) {
        System.out.println("hi");
    }
}
