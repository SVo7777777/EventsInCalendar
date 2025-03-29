package com.example.calendarhours.ui.home;

import static android.graphics.Color.GRAY;
import static android.graphics.Color.LTGRAY;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

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
    public Button previous_month;
    public Button next_month;
    public Button previous_year;
    public Button next_year;
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
    String[] split;
    String[] split2;
    private DatabaseHelper mydb ;
    public TextView textView3;
    public EditText editTextNumber;
    public Button button2;


    @SuppressLint("WrongViewCast")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        month = root.findViewById(R.id.month);
        year = root.findViewById(R.id.year);
        text_home = root.findViewById(R.id.text_home);
        previous_month = root.findViewById(R.id.previous_month);
        next_month = root.findViewById(R.id.next_month);
        previous_year = root.findViewById(R.id.previous_year);
        next_year = root.findViewById(R.id.next_year);
        previousMonthOnButtonClick(previous_month);
        nextMonthOnButtonClick(next_month);
        previousYearOnButtonClick(previous_year);
        nextYearOnButtonClick(next_year);
        textView3 = root.findViewById(R.id.textView3);
        textView3.setText("Всего: 0.0");
        editTextNumber = root.findViewById(R.id.editTextNumber);
        editTextNumber.setPaintFlags(View.INVISIBLE);
        button2 = root.findViewById(R.id.button2);
        salaryShowOnButtonClick(button2);



        int e = 0;
        while (e < 7) {
            String weekId = "numweek_" + e;
            int weID = getResources().getIdentifier(weekId, "id", requireActivity().getPackageName());
            number_of_week[e] = root.findViewById(weID);
            e++;
        }
        for (int i = 0; i < 7; i++)
            for (int j = 0; j < 8; j++) {
                //buttons[i][j] = "calendar_"+i+j;
                String llButtonId = "calendar_" + i + j;
                String dayId = "day_" + i + j;
                String eventId = "event_" + i + j;
                int bID = getResources().getIdentifier(llButtonId, "id", requireActivity().getPackageName());
                int dID = getResources().getIdentifier(dayId, "id", requireActivity().getPackageName());
                int eID = getResources().getIdentifier(eventId, "id", requireActivity().getPackageName());
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


        String month2 = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("ru"));
        System.out.println(month2);

        calendar.add(Calendar.MONTH, -1);
        int max_pred = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println("max_pred="+max_pred);


        showCalendar(month3, current_year, week_of_year, max_pred, day_of_week, dateEnd);


        for (int i = 1; i < 7; i++)
            for (int j = 1; j < 8; j++) {
                try {
                    setOnClick(buttons[i][j], days[i][j], events[i][j], day_of_weeks[j]);
                    //ne: NullPointerException
                } catch (Exception ignored) {

                }

            }
        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    private void salaryShowOnButtonClick(Button button2) {
        button2.setOnClickListener(v -> {
            String h = String.valueOf(textView3.getText());
            split2 = h.split(" ");
            System.out.println(Arrays.toString(split2));
            System.out.println(split2[0]);
            System.out.println(split2[1]);
            String price = String.valueOf(editTextNumber.getText());
            String hours = split2[1];
            float p = Float.parseFloat(hours) * Float.parseFloat(price);
            System.out.println(p);
            String salary = String.valueOf(p);
            button2.setText(salary);
        });
    }

    @SuppressLint("SetTextI18n")
    private void showCalendar(String mon, int yea, int wee, int mpred, int dayOfWeekOfFirstDayOfMonth, int dateEnd) {
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
                        events[i][j].setText("0");
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
    @SuppressLint("SetTextI18n")
    private void addHoursFromBase(String mont, String y){
        StringBuilder hours = new StringBuilder();
        float sum = 0.0F;
        for (int i = 1; i < 7; i++) {
            for (int j = 1; j < 8; j++) {
                String h =(String) events[i][j].getText();
                if (!h.isEmpty()){
                    System.out.println(days[i][j].getText() + "-" + h);
                    sum += Float.parseFloat(h);
                    hours.append("-").append(h);
                }else{
                    System.out.println(days[i][j].getText() + "-" + ".");
                    hours.append("-" + ".");
                }
            }
        }
        System.out.println(hours);
        textView3.setText("Всего: "+ sum);
        String data = mont+" "+y;
        mydb = new DatabaseHelper(getContext());
        boolean search = mydb.checkDataExistOrNot(data);
        System.out.println("search: "+search);
        if (search) {
            Toast.makeText(getActivity(), data + " уже есть!", Toast.LENGTH_SHORT).show();
            String h = mydb.getHours(data);
            //String s = mydb.getSum(data);
            System.out.println("за "+data+" часы: "+h);
            System.out.println("s="+sum);
            addHoursInCalendar(h);
        }else {
            System.out.println("hours="+hours);
            System.out.println("sum="+ sum);
            mydb.insertContact(data, hours.toString());
            Toast.makeText(getActivity(), data + " добавлен!", Toast.LENGTH_SHORT).show();
        }
    }
    @SuppressLint("SetTextI18n")
    private void addHoursInCalendar(String h) {
        split = h.split("-");
        System.out.println(h);
        int d = 1;
        float sum = 0.0F;
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
                    sum += Float.parseFloat(split[d]);
                    }
                d += 1;
            }
        }
        System.out.println(sum);
        textView3.setText("Всего: "+ sum);
    }
    private void previousMonthOnButtonClick(Button btn) {
        btn.setOnClickListener(v -> {
            String mon = (String) month.getText();
            int ind = Arrays.asList(monthNames).indexOf(mon);
            if (ind == 0) {
                ind = 12;
            }
            String new_month = monthNames[ind - 1];
            System.out.println(new_month);
            month.setText(new_month);
            int _year = Integer.parseInt(year.getText().toString());
            System.out.println(_year);
            System.out.println(new_month);
            System.out.println(ind - 1);
            Calendar c = Calendar.getInstance();
            c.set(_year, ind - 1, 1);
            int day_of_week = c.get(Calendar.DAY_OF_WEEK);
            System.out.println("day_of_week=" + day_of_week);
            int dateEnd = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            System.out.println(dateEnd);
            int week_of_year = c.get(Calendar.WEEK_OF_YEAR);
            System.out.println(week_of_year);
            c.add(Calendar.MONTH, -1);
            int max_pred = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            showCalendar(new_month, _year, week_of_year, max_pred, day_of_week, dateEnd);
        });
    }
    private void nextMonthOnButtonClick(Button btn) {
        btn.setOnClickListener(v -> {
            String mon = (String) month.getText();
            int ind = Arrays.asList(monthNames).indexOf(mon);
            if (ind == 11) {
                ind = -1;
            }
            String new_month = monthNames[ind + 1];
            System.out.println(new_month);
            month.setText(new_month);
            int _year = Integer.parseInt(year.getText().toString());
            System.out.println(_year);
            System.out.println(new_month);
            System.out.println(ind + 1);
            Calendar c = Calendar.getInstance();
            c.set(_year, ind + 1, 1);
            int day_of_week = c.get(Calendar.DAY_OF_WEEK);
            System.out.println(day_of_week);
            int dateEnd = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            System.out.println(dateEnd);
            int week_of_year = c.get(Calendar.WEEK_OF_YEAR);
            System.out.println(week_of_year);
            c.add(Calendar.MONTH, -1);
            int max_pred = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            showCalendar(new_month, _year, week_of_year, max_pred, day_of_week, dateEnd);
        });
    }
    private void previousYearOnButtonClick(Button btn) {
        btn.setOnClickListener(view -> {
            String old_month = (String) month.getText();
            int ind = Arrays.asList(monthNames).indexOf(old_month);
            int _year = Integer.parseInt(year.getText().toString());
            int new_year = _year - 1;
            System.out.println(_year);
            System.out.println(ind - 1);
            Calendar c = Calendar.getInstance();
            c.set(new_year, ind, 1);
            int day_of_week = c.get(Calendar.DAY_OF_WEEK);
            System.out.println(day_of_week);
            int dateEnd = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            System.out.println(dateEnd);
            int week_of_year = c.get(Calendar.WEEK_OF_YEAR);
            System.out.println(week_of_year);
            c.add(Calendar.MONTH, -1);
            int max_pred = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            showCalendar(old_month, new_year, week_of_year, max_pred, day_of_week, dateEnd);
        });
    }
    private void nextYearOnButtonClick(Button btn) {
        btn.setOnClickListener(view -> {
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
            showCalendar(old_month, new_year, week_of_year, max_pred, day_of_week, dateEnd);
        });
    }
    private void setOnClick(LinearLayout btn, TextView day1, TextView event1,  String day_week) {
        btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"SetTextI18n", "InflateParams"})
            @Override
            public void onClick(View v) {
                String mon = (String) month.getText();
                String ye = (String) year.getText();
                @SuppressLint("UseRequireInsteadOfGet")
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
                month1.setText(month.getText());
                TextView day_of_weeks = view.findViewById(R.id.day_of_weeks);
                day_of_weeks.setText(day_week);
                @SuppressLint("SimpleDateFormat")
                final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Calendar c = Calendar.getInstance();
                c.set(Integer.parseInt(year.getText().toString()), Arrays.asList(monthNames).indexOf((String) month.getText()), Integer.parseInt((String) number.getText()));
                String sDate = sdf.format(c.getTime());
                all.setText(sDate+": ");
                event.requestFocus();
                event.setSelection(event.getText().length());
                //клавиатура выезжает сразу
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
                        String data;
                        data = month_year;
                        int id = mydb.GetId(data);
                        System.out.println("id="+id);
                        System.out.println("month_year="+month_year);
                        boolean update_hours = mydb.updateHours(id, month_year, String.valueOf(hours));
                        if (update_hours){
                            Toast.makeText(getActivity(), "Часы изменены! Всего часов: "+sum, Toast.LENGTH_SHORT).show();
                        }
                        String s = String.valueOf(sum);
                        textView3.setText(String.format("Всего: %s", s));
                    }
                });
                close.setOnClickListener(v1 -> alertDialog.dismiss());
                num1.setOnClickListener(v2 -> {
                    event.setText(event.getText()+"1");
                    event.requestFocus();
                    event.setSelection(event.getText().length());
                });
                num2.setOnClickListener(v3 -> {
                    event.setText(event.getText()+"2");
                    event.requestFocus();
                    event.setSelection(event.getText().length());
                });
                num3.setOnClickListener(v4 -> {
                    event.setText(event.getText()+"3");
                    event.requestFocus();
                    event.setSelection(event.getText().length());
                });
                num4.setOnClickListener(v5 -> {
                    event.setText(event.getText()+"4");
                    event.requestFocus();
                    event.setSelection(event.getText().length());
                });
                num5.setOnClickListener(v6 -> {
                    event.setText(event.getText()+"5");
                    event.requestFocus();
                    event.setSelection(event.getText().length());
                });
                num6.setOnClickListener(v7 -> {
                    event.setText(event.getText()+"6");
                    event.requestFocus();
                    event.setSelection(event.getText().length());
                });
                num7.setOnClickListener(v8 -> {
                    event.setText(event.getText()+"7");
                    event.requestFocus();
                    event.setSelection(event.getText().length());
                });
                num8.setOnClickListener(v9 -> {
                    event.setText(event.getText()+"8");
                    event.requestFocus();
                    event.setSelection(event.getText().length());
                });
                num9.setOnClickListener(v10 -> {
                    event.setText(event.getText()+"9");
                    event.requestFocus();
                    event.setSelection(event.getText().length());
                });
                num0.setOnClickListener(v11 -> {
                    event.setText(event.getText()+"0");
                    event.requestFocus();
                    event.setSelection(event.getText().length());
                });
                point.setOnClickListener(v12 -> {
                    event.setText(event.getText()+".");
                    event.requestFocus();
                    event.setSelection(event.getText().length());
                });
                backspace.setOnClickListener(v13 -> {
                    String str = String.valueOf(event.getText());
                    if (!str.isEmpty()){
                        String newStr = str.substring(0, str.length() - 1);
                        event.setText(newStr);
                        event.requestFocus();
                        event.setSelection(event.getText().length());
                    }
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
