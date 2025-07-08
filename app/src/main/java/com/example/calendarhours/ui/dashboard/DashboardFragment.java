package com.example.calendarhours.ui.dashboard;

import static android.graphics.Color.LTGRAY;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.calendarhours.DatabaseHelper;
import com.example.calendarhours.R;
import com.example.calendarhours.databinding.FragmentDashboardBinding;
import com.example.calendarhours.ui.home.HomeFragment;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    public Button previous_month;
    public Button next_month;
    public Button previous_year;
    public Button next_year;
    public Button month;
    public Button year;
    LinearLayout view;
    LinearLayout days_month;
    LinearLayout my_hours;
    LinearLayout my_hours1;
    LinearLayout my_hours2;
    TextView[] number_of_days= new TextView[33];
    Button[] my_hours_of_days= new Button[33];
    Button[] my_hours1_of_days= new Button[33];
    Button[] my_hours2_of_days= new Button[33];
    String[] monthNames = new String[]{"ЯНВАРЬ", "ФЕВРАЛЬ", "МАРТ", "АПРЕЛЬ", "МАЙ", "ИЮНЬ", "ИЮЛЬ", "АВГУСТ", "СЕНТЯБРЬ", "ОКТЯБРЬ", "НОЯБРЬ", "ДЕКАБРЬ"};
    String[] day_of_weeks2 = new String[]{"","ВОСКРЕСЕНЬЕ", "ПОНЕДЕЛЬНИК", "ВТОРНИК", "СРЕДА", "ЧЕТВЕРГ", "ПЯТНИЦА", "СУББОТА" };
    String[] day_weeks = new String[]{"","вс", "пн", "вт", "ср", "чт", "пт", "сб"};

    Calendar calendar = Calendar.getInstance();
    public int current_year = calendar.get(Calendar.YEAR);
    public int current_month = calendar.get(Calendar.MONTH);
    public int current_day = calendar.get(Calendar.DATE);
    String[] split;
    String[] split2;
    private DatabaseHelper mydb ;
    public TextView textView3;
    public TextView result;
    public TextView result1;
    public TextView result2;
    public TextView plan1;
    public TextView plan2;
    public EditText editTextNumber;
    public Button button2;
    public String hours1;
    public HomeFragment fragment;
    public int day_OfWeekOfFirstDayOfMonth;
    public int date_End;


    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        days_month = root.findViewById(R.id.days_month);
        my_hours = root.findViewById(R.id.my_hours);
        my_hours1 = root.findViewById(R.id.my_hours1);
        my_hours2 = root.findViewById(R.id.my_hours2);
        month = root.findViewById(R.id.month);
        year = root.findViewById(R.id.year);
        previous_month = root.findViewById(R.id.previous_month);
        next_month = root.findViewById(R.id.next_month);
        previous_year = root.findViewById(R.id.previous_year);
        next_year = root.findViewById(R.id.next_year);
        textView3 = root.findViewById(R.id.textView3);
        editTextNumber = root.findViewById(R.id.editTextNumber);
        editTextNumber.setPaintFlags(View.INVISIBLE);
        result = root.findViewById(R.id.result);
        result1 = root.findViewById(R.id.result1);
        result2 = root.findViewById(R.id.result2);
        previousMonthOnButtonClick(previous_month);
        nextMonthOnButtonClick(next_month);
        previousYearOnButtonClick(previous_year);
        nextYearOnButtonClick(next_year);
        plan1 = root.findViewById(R.id.plan1);
        plan2 = root.findViewById(R.id.plan2);
        button2 = root.findViewById(R.id.button2);
        //button2.setText("salary");
        salaryShowOnButtonClick(button2);



        hours1 = "";

        mydb = new DatabaseHelper(getContext());
        //mydb.AddnewTable("plan3");
        gridTable();
            for (int j = 0; j < 32; j++) {
                try {
                    setOnClick(my_hours_of_days[j], number_of_days[j], my_hours_of_days);
                    setOnClick(my_hours1_of_days[j], number_of_days[j], my_hours1_of_days);
                    setOnClick(my_hours2_of_days[j], number_of_days[j], my_hours2_of_days);
                    //ne: NullPointerException
                } catch (Exception ignored) {

                }

            }

        String month3 = monthNames[current_month];
        System.out.println(month3);
        Calendar c = Calendar.getInstance();
        c.set(current_year, current_month, 1);
        int day_of_week = c.get(Calendar.DAY_OF_WEEK);
        System.out.println("day_of_week="+day_of_week);
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

        String data = month.getText() + " " + year.getText();
        System.out.println(data);
        String price = mydb.getPrice(data, DatabaseHelper.TABLE);
        System.out.println(price);
        String sal = mydb.getSalary(data, DatabaseHelper.TABLE);
        System.out.println(sal);
        //boolean update_salary = mydb.updateSalary(id, month_year, String.valueOf(sal), "plan1");
        if (price.equals("0.0")){
            button2.setText("salary");
            editTextNumber.setText("");
        }else {
            button2.setText(String.valueOf(sal));
            editTextNumber.setText(price);
        }
        final TextView textView = binding.textHome;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }
    @SuppressLint("SetTextI18n")
    private void salaryShowOnButtonClick(Button button2) {
        button2.setOnClickListener(v -> {
            String h = String.valueOf(textView3.getText());
            String mon = (String) month.getText();
            String ye = (String) year.getText();
            String month_year = mon + " " + ye;
            try {
                split2 = h.split(" ");
                System.out.println(Arrays.toString(split2));
                System.out.println(split2[0]);
                System.out.println(split2[1]);
                String price = String.valueOf(editTextNumber.getText());
                String hours = split2[1];
                float p = Float.parseFloat(hours) * Float.parseFloat(price);
                System.out.println(p);
                String salary = String.valueOf(p);
                int id = mydb.GetId(month_year, DatabaseHelper.TABLE);
                System.out.println("id="+id);
                System.out.println("month_year="+month_year);
                boolean update_hours = mydb.updateSalary(id, month_year, String.valueOf(salary), "hours");
                if (update_hours){
                    Toast.makeText(getActivity(), "Зарплата изменена! Всего: "+salary, Toast.LENGTH_SHORT).show();
                }
                button2.setText(salary);
                boolean update_price = mydb.updatePrice(id, month_year, String.valueOf(price), "hours");
                if (update_price){
                    Toast.makeText(getActivity(), "Цена за час в этом месяце: "+price+" сохранена!", Toast.LENGTH_SHORT).show();
                }

            }catch (NumberFormatException e) {
                Toast.makeText(getActivity(), "Введите цену за час!", Toast.LENGTH_LONG).show();
            }
        });
    }
    @SuppressLint("SetTextI18n")
    private void showCalendar(String mon, int yea, int wee, int mpred, int dayOfWeekOfFirstDayOfMonth, int dateEnd) {
        month.setText(mon);
        year.setText(Integer.toString(yea));
        String mont = String.valueOf(month.getText());
        String y = String.valueOf(year.getText());
        date_End = dateEnd;
        day_OfWeekOfFirstDayOfMonth = dayOfWeekOfFirstDayOfMonth;


        @SuppressLint("SimpleDateFormat")
        final SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");

        calendar.set(current_year, current_month, current_day);
        String sDate_now = sdf1.format(calendar.getTime());
        System.out.println("sDate_now="+sDate_now);
        if (dayOfWeekOfFirstDayOfMonth == 1){
            day_OfWeekOfFirstDayOfMonth = 8;
        }
        int m = mpred - dayOfWeekOfFirstDayOfMonth+3;
        int d = 1;
        int d2 = 1;

        for (int j = 0; j < 32; j++) {
            number_of_days[j].setEnabled(true);
            my_hours_of_days[j].setEnabled(true);
            my_hours_of_days[j].setBackgroundColor(0);
            my_hours1_of_days[j].setBackgroundColor(0);
            my_hours2_of_days[j].setBackgroundColor(0);
            my_hours_of_days[j].setTextColor(my_hours_of_days[j].getContext().getResources().getColor(R.color.purple_700));
            my_hours1_of_days[j].setTextColor(my_hours_of_days[j].getContext().getResources().getColor(R.color.purple_700));
            my_hours2_of_days[j].setTextColor(my_hours_of_days[j].getContext().getResources().getColor(R.color.purple_700));


            @SuppressLint("SimpleDateFormat")
            final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Calendar c = Calendar.getInstance();
            c.set(Integer.parseInt(year.getText().toString()), Arrays.asList(monthNames).indexOf((String) month.getText()), j+1);
            int day_of_week = c.get(Calendar.DAY_OF_WEEK);
            String sDate = sdf.format(c.getTime());
            number_of_days[j].setText("");
            number_of_days[j].setText(( String.valueOf(j+1) + " " + String.valueOf(day_weeks[day_of_week])));
            number_of_days[j].setBackgroundColor(number_of_days[j].getContext().getResources().getColor(R.color.work_day));
            number_of_days[j].setTextColor(number_of_days[j].getContext().getResources().getColor(R.color.black));



            if (day_of_week == 1 || day_of_week == 7) {
                    number_of_days[j].setBackgroundColor(number_of_days[j].getContext().getResources().getColor(R.color.weekend_day));
                    number_of_days[j].setTextColor(number_of_days[j].getContext().getResources().getColor(R.color.white));
                    my_hours1_of_days[j].setBackgroundColor(number_of_days[j].getContext().getResources().getColor(R.color.weekend_day));
                    my_hours1_of_days[j].setTextColor(number_of_days[j].getContext().getResources().getColor(R.color.white));
                    my_hours_of_days[j].setBackgroundColor(number_of_days[j].getContext().getResources().getColor(R.color.weekend_day));
                    my_hours_of_days[j].setTextColor(number_of_days[j].getContext().getResources().getColor(R.color.white));
                    my_hours2_of_days[j].setBackgroundColor(number_of_days[j].getContext().getResources().getColor(R.color.weekend_day));
                    my_hours2_of_days[j].setTextColor(number_of_days[j].getContext().getResources().getColor(R.color.white));

                } else {
                    number_of_days[j].setBackgroundColor(number_of_days[j].getContext().getResources().getColor(R.color.work_day));
//                    events[i][j].setTextColor(events[i][j].getContext().getResources().getColor(R.color.red));
//                    events[i][j].setTextSize(26);
//                    days[i][j].setTextColor(days[i][j].getContext().getResources().getColor(R.color.Purple2));
                }
                if (sDate.equals(sDate_now)) {
                    number_of_days[j].setBackgroundColor(number_of_days[j].getContext().getResources().getColor(R.color.DeepSkyBlue));
                }
            if (dateEnd == j+1){
                System.out.println("j+1="+(j+1));
                if (dateEnd==28){
                    my_hours_of_days[28].setEnabled(false);
                    my_hours_of_days[28].setText("");
                    number_of_days[28].setBackgroundColor(LTGRAY);
                    number_of_days[28].setText("");
                    my_hours_of_days[29].setEnabled(false);
                    my_hours_of_days[29].setText("");
                    number_of_days[29].setBackgroundColor(LTGRAY);
                    number_of_days[29].setText("");
                    my_hours_of_days[30].setEnabled(false);
                    my_hours_of_days[30].setText("");
                    number_of_days[30].setBackgroundColor(LTGRAY);
                    number_of_days[30].setText("");

                }
                if (dateEnd==29){
                    my_hours_of_days[29].setEnabled(false);
                    my_hours_of_days[29].setText("");
                    number_of_days[29].setBackgroundColor(LTGRAY);
                    number_of_days[29].setText("");
                    my_hours_of_days[30].setEnabled(false);
                    my_hours_of_days[30].setText("");
                    number_of_days[30].setBackgroundColor(LTGRAY);
                    number_of_days[30].setText("");

                }
                if (dateEnd==30){
                    my_hours_of_days[30].setEnabled(false);
                    number_of_days[30].setBackgroundColor(LTGRAY);
                    number_of_days[30].setText("");
                    my_hours_of_days[30].setText("");

                }

                my_hours_of_days[31].setEnabled(false);
                break;
            }


        }
        addHoursFromBase(mont,y, dateEnd, dayOfWeekOfFirstDayOfMonth);
    }
    @SuppressLint("SetTextI18n")
    private void addHoursFromBase(String mont, String y, int dateEnd, int dayOfWeekOfFirstDayOfMonth){
        //StringBuilder hours = new StringBuilder();
        float sum = 0.0F;

        //textView3.setText("Всего: "+ sum);
        String data = mont+" "+y;
        StringBuilder hours = new StringBuilder(zeroHours(dateEnd, dayOfWeekOfFirstDayOfMonth));
//        mydb.insertContact(data, hours.toString(), "plan1");
//        mydb.insertContact(data, hours.toString(), "plan2");
        boolean search = mydb.checkDataExistOrNot(data, DatabaseHelper.TABLE);
        System.out.println("search: "+search);
        if (search) {
            Toast.makeText(getActivity(), data + " уже есть!", Toast.LENGTH_SHORT).show();
            String h = mydb.getHours(data, DatabaseHelper.TABLE);
            String h1 = mydb.getHours(data, DatabaseHelper.TABLE1);
            String h2 = mydb.getHours(data, DatabaseHelper.TABLE2);
            //String s = mydb.getSum(data);
            hours1 = h;
            System.out.println("hours1="+hours1);
            System.out.println("за "+data+" часы: "+h);
            System.out.println("s="+sum);
            addHoursInCalendar(h, dateEnd, my_hours_of_days);
            addHoursInCalendar(h1, dateEnd, my_hours1_of_days);
            addHoursInCalendar(h2, dateEnd, my_hours2_of_days);
        }else {
//            for (int i = 1; i < 7; i++) {
//                for (int j = 1; j < 8; j++) {
//                    String h =(String) fragment.events[i][j].getText();
//                    if (!h.isEmpty()){
//                        System.out.println(fragment.days[i][j].getText() + "-" + h);
//                        sum += Float.parseFloat(h);
//                        hours.append("-").append(h);
//                    }else{
//                        System.out.println(fragment.days[i][j].getText() + "-" + ".");
//                        hours.append("-" + ".");
//                    }
//                }
//            }
//            int d = 1;
//            for (int i = 1; i < 43; i++) {
//                if (i < dayOfWeekOfFirstDayOfMonth-1){
//                    hours.append("-" + ".");
//                }else {
//                    if (d < dateEnd + 1) {
//                        hours.append("-" + "0");
//                        d += 1;
//                    }else {
//                        hours.append("-" + ".");
//                    }
//                }
//
//            }
            //StringBuilder hours = new StringBuilder(zeroHours(dateEnd, dayOfWeekOfFirstDayOfMonth));
            System.out.println(hours);
            System.out.println("hours="+hours);
            System.out.println("sum="+ sum);
            mydb.insertContact(data, hours.toString(), "0.0", "0.0","0.0","plan1");
            mydb.insertContact(data, hours.toString(), "0.0", "0.0","0.0", "plan2");
            mydb.insertContact(data, hours.toString(),"0.0","0.0","0.0", "hours");
            Toast.makeText(getActivity(), data + " добавлен!", Toast.LENGTH_SHORT).show();
        }
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
    private StringBuilder zeroHours(int dateEnd, int dayOfWeekOfFirstDayOfMonth){
        StringBuilder hours = new StringBuilder();
        int d = 1;
        for (int i = 1; i < 43; i++) {
            if (i < dayOfWeekOfFirstDayOfMonth-1){
                hours.append("-" + ".");
            }else {
                if (d < dateEnd + 1) {
                    hours.append("-" + "0");
                    d += 1;
                }else {
                    hours.append("-" + ".");
                }
            }

        }
        return hours;
    }
    @SuppressLint("SetTextI18n")
    private void addHoursInCalendar(String h, int dateEnd, Button[] hours_of_days) {
        split = h.split("-");
        System.out.println(h);
        int d = 1;
        float sum = 0.0F;
        int j = 0;
        System.out.println("split[0=]"+split[0]);
        System.out.println("split[1=]"+split[1]);
        System.out.println(split[d]);
        System.out.println(Arrays.toString(split));
        for (int i = 0; i < 43; i++) {
            if (split[d].equals(".")){
                System.out.println("point");
            }else {
                hours_of_days[j].setText(split[d]);
                sum += Float.parseFloat(split[d]);
                j += 1;
                if (dateEnd == j) {
                    break;
                }

            }
            d += 1;


        }
        String data = month.getText()+" "+year.getText();
        String price = mydb.getPrice(data, DatabaseHelper.TABLE);
        System.out.println(data);
        System.out.println(price);

        hours_of_days[31].setText(String.valueOf(sum));
        if (Arrays.equals(hours_of_days, my_hours2_of_days)) {
            result2.setText(String.valueOf(sum));
            hours_of_days[32].setText(String.valueOf(sum*Float.parseFloat(price)));
            System.out.println(sum);
        } else if (Arrays.equals(hours_of_days, my_hours_of_days)) {
            result.setText(String.valueOf(sum));
            hours_of_days[32].setText(String.valueOf(sum*Float.parseFloat(price)));
            System.out.println(sum);
            textView3.setText("Всего: " + sum);
            button2.setText(String.valueOf(sum*Float.parseFloat(price)));
            editTextNumber.setText(price);
        } else if (Arrays.equals(hours_of_days, my_hours1_of_days)) {
            result1.setText(String.valueOf(sum));
            hours_of_days[32].setText(String.valueOf(sum*Float.parseFloat(price)));
            System.out.println(sum);
        } else {
            System.out.println("---INVALID---");
        }
//        result.setText(String.valueOf(sum));
//        System.out.println(sum);
//        textView3.setText("Всего: "+ sum);
    }
    public void previousMonthOnButtonClick(Button btn) {
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
    public void nextMonthOnButtonClick(Button btn) {
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
    public void previousYearOnButtonClick(Button btn) {
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
    public void nextYearOnButtonClick(Button btn) {
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

    private void setOnClick(Button myHoursOfDay, TextView numberOfDay, Button[] hours_of_days) {
        myHoursOfDay.setOnClickListener(new View.OnClickListener() {
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
                number.setText(numberOfDay.getText());
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

                @SuppressLint("SimpleDateFormat")
                final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Calendar c = Calendar.getInstance();
                String date = (String) number.getText();
                split2 = date.split(" ");
                System.out.println(split2[0]);
                int ind = Arrays.asList(day_weeks).indexOf(split2[1]);
                day_of_weeks.setText(day_of_weeks2[ind]);
                number.setText(split2[0]);
                c.set(Integer.parseInt(year.getText().toString()), Arrays.asList(monthNames).indexOf((String) month.getText()), Integer.parseInt((String)split2[0]));
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
                            myHoursOfDay.setText(data);
                            alertDialog.dismiss();
                            addHours();
                        }else {
                            Toast.makeText(getActivity(), "Введите часы!", Toast.LENGTH_LONG).show();
                        }
                    }
                    private void addHours() {
                        String h0 = "";
                        String month_year = mon + " " + ye;
                        StringBuilder hours = new StringBuilder();
                        float sum = 0.0F;
                        int d = 1;
                        for (int i = 1; i < 43; i++) {
                            if (i < day_OfWeekOfFirstDayOfMonth-1){
                                hours.append("-" + ".");
                            }else {
                                if (Arrays.equals(hours_of_days, my_hours2_of_days)) {
                                    h0 = (String) DashboardFragment.this.my_hours2_of_days[d - 1].getText();
                                } else if (Arrays.equals(hours_of_days, my_hours_of_days)) {
                                    h0 = (String) DashboardFragment.this.my_hours_of_days[d - 1].getText();
                                } else if (Arrays.equals(hours_of_days, my_hours1_of_days)) {
                                    h0 = (String) DashboardFragment.this.my_hours1_of_days[d - 1].getText();
                                } else {
                                    System.out.println("---INVALID---");
                                }

                                String h = h0;
                                if (d < date_End+1)  {
                                    try {
                                        sum += Float.parseFloat(h);
                                        hours.append("-").append(h);
                                    } catch (NumberFormatException e) {
                                        Toast.makeText(getActivity(), "Это не число!", Toast.LENGTH_SHORT).show();
                                        hours.append("-").append("0");
                                    }
                                    d += 1;
                                }else {
                                    hours.append("-" + ".");
                                }
                            }

                        }

//                            for (int j = 0; j < 32; j++) {
//                                String h =(String) my_hours_of_days[j].getText();
//                                if (!h.isEmpty()){
//                                    System.out.println(number_of_days[j].getText() + "-" + h);
//                                    try {
//                                        sum += Float.parseFloat(h);
//                                        hours.append("-").append(h);
//                                    } catch (NumberFormatException e) {
//                                        Toast.makeText(getActivity(), "Это не число!", Toast.LENGTH_SHORT).show();
//                                        hours.append("-").append("0");
//                                    }
//                                }else{
//                                    System.out.println(number_of_days[j].getText() + "-" + ".");
//                                    hours.append("-").append(".");
//                                }
//                            }

                        System.out.println(hours);
                        System.out.println(month_year+" "+hours);
                        String data;
                        data = month_year;
                        if (Arrays.equals(hours_of_days, my_hours2_of_days)) {
                            int id = mydb.GetId(data, DatabaseHelper.TABLE2);
                            boolean update_hours = mydb.updateHours(id, month_year, String.valueOf(hours), "plan2", String.valueOf(sum));
                            if (update_hours){
                                Toast.makeText(getActivity(), "Часы plan2 изменены! Всего часов: "+sum, Toast.LENGTH_SHORT).show();
                            }
                            DashboardFragment.this.my_hours2_of_days[31].setText(String.valueOf(sum));
                            result2.setText(String.valueOf(sum));

                            String price = mydb.getPrice(data, DatabaseHelper.TABLE);
                            float sal = (Float.parseFloat(String.valueOf(sum)) * Float.parseFloat(price));
                            System.out.println(sal);
                            boolean update_salary = mydb.updateSalary(id, month_year, String.valueOf(sal), "plan2");
                            if (price.equals("0.0")){
                                button2.setText("salary");
                                editTextNumber.setText("");
                            }else {
                                button2.setText(String.valueOf(sal));
                                editTextNumber.setText(price);
                                if (update_salary) {
                                    Toast.makeText(getActivity(), "Зарплата plan2 изменена! Всего: " + sal, Toast.LENGTH_SHORT).show();
                                }
                            }
                            DashboardFragment.this.my_hours2_of_days[32].setText(String.valueOf(sal));


                        } else if (Arrays.equals(hours_of_days, my_hours_of_days)) {
                            int id = mydb.GetId(data, DatabaseHelper.TABLE);
                            System.out.println("id=" + id);
                            System.out.println("month_year=" + month_year);
                            boolean update_hours = mydb.updateHours(id, month_year, String.valueOf(hours), "hours", String.valueOf(sum));
                            if (update_hours) {
                                Toast.makeText(getActivity(), "Часы real изменены! Всего часов: " + sum, Toast.LENGTH_SHORT).show();
                            }
                            String s = String.valueOf(sum);
                            DashboardFragment.this.my_hours_of_days[31].setText(String.valueOf(sum));
                            result.setText(String.valueOf(sum));
                            textView3.setText(String.format("Всего: %s", s));

                            String price = mydb.getPrice(data, DatabaseHelper.TABLE);
                            float sal = (Float.parseFloat(s) * Float.parseFloat(price));
                            System.out.println(sal);
                            boolean update_salary = mydb.updateSalary(id, month_year, String.valueOf(sal), "hours");

                            if (price.equals("0.0")){
                                button2.setText("salary");
                                editTextNumber.setText("");
                            }else {
                                button2.setText(String.valueOf(sal));
                                editTextNumber.setText(price);
                                if (update_salary) {
                                    Toast.makeText(getActivity(), "Зарплата real изменена! Всего: " + sal, Toast.LENGTH_SHORT).show();
                                }
                            }
                            DashboardFragment.this.my_hours_of_days[32].setText(String.valueOf(sal));

                        } else if (Arrays.equals(hours_of_days, my_hours1_of_days)) {
                            int id = mydb.GetId(data, DatabaseHelper.TABLE1);
                            boolean update_hours = mydb.updateHours(id, month_year, String.valueOf(hours), "plan1", String.valueOf(sum));
                            if (update_hours){
                                Toast.makeText(getActivity(), "Часы pian1 изменены! Всего часов: "+sum, Toast.LENGTH_SHORT).show();
                            }

                            DashboardFragment.this.my_hours1_of_days[31].setText(String.valueOf(sum));
                            result1.setText(String.valueOf(sum));
                            String price = mydb.getPrice(data, DatabaseHelper.TABLE);
                            float sal = (Float.parseFloat(String.valueOf(sum)) * Float.parseFloat(price));
                            System.out.println(sal);
                            boolean update_salary = mydb.updateSalary(id, month_year, String.valueOf(sal), "plan1");
                            if (price.equals("0.0")){
                                button2.setText("salary");
                                editTextNumber.setText("");
                            }else {
                                button2.setText(String.valueOf(sal));
                                editTextNumber.setText(price);
                                if (update_salary) {
                                    Toast.makeText(getActivity(), "Зарплата plan1 изменена! Всего: " + sal, Toast.LENGTH_SHORT).show();
                                }
                            }
                            DashboardFragment.this.my_hours1_of_days[32].setText(String.valueOf(sal));

                        } else {
                            System.out.println("---INVALID---");
                        }


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

    @SuppressLint("SetTextI18n")
    public void gridTable(){
        for (int i = 0; i < 33; i++){
            number_of_days[i]=new TextView(getContext());
            number_of_days[i].setText(Integer.toString(i + 1)); // Set text or other properties as needed
            number_of_days[i].setId(i + 1);
            number_of_days[i].setTextSize(30);
            number_of_days[i].setWidth(160);
            number_of_days[i].setGravity(Gravity.CENTER | Gravity.TOP);
            number_of_days[i].setBackgroundColor(LTGRAY);

            my_hours_of_days[i]=new Button(getContext());
            my_hours_of_days[i].setId(i + 1);
            my_hours_of_days[i].setTextSize(30);
            my_hours_of_days[i].setPadding(0,0,0,5);
            my_hours_of_days[i].setWidth(160);
            my_hours_of_days[i].setTextColor(my_hours_of_days[i].getContext().getResources().getColor(R.color.purple_700));
            my_hours_of_days[i].setGravity(Gravity.CENTER);
            my_hours_of_days[i].setBackgroundColor(0);

            my_hours1_of_days[i]=new Button(getContext());
            my_hours1_of_days[i].setId(i + 1);
            my_hours1_of_days[i].setTextSize(30);
            my_hours1_of_days[i].setPadding(0,0,0,5);
            my_hours1_of_days[i].setWidth(160);
            my_hours1_of_days[i].setTextColor(my_hours_of_days[i].getContext().getResources().getColor(R.color.purple_700));
            my_hours1_of_days[i].setGravity(Gravity.CENTER);
            my_hours1_of_days[i].setBackgroundColor(0);

            my_hours2_of_days[i]=new Button(getContext());
            my_hours2_of_days[i].setId(i + 1);
            my_hours2_of_days[i].setTextSize(30);
            my_hours2_of_days[i].setPadding(0,0,0,5);
            my_hours2_of_days[i].setTextColor(my_hours_of_days[i].getContext().getResources().getColor(R.color.purple_700));
            my_hours2_of_days[i].setWidth(160);
            my_hours2_of_days[i].setGravity(Gravity.CENTER);
            my_hours2_of_days[i].setBackgroundColor(0);



//            if ((i % 2) == 0){
//                number_of_days[i].setBackgroundColor(number_of_days[i].getContext().getResources().getColor(R.color.teal_200));
//
//            }
            if (i == 31) {
                number_of_days[i].setText("Итог");
                number_of_days[i].setWidth(200);
                number_of_days[i].setBackgroundColor(number_of_days[i].getContext().getResources().getColor(R.color.purple_200));
                my_hours_of_days[i].setWidth(200);
                my_hours1_of_days[i].setWidth(200);
                my_hours2_of_days[i].setWidth(200);
            }
            if (i == 32) {
                number_of_days[i].setText("З/п");
                number_of_days[i].setWidth(250);
                number_of_days[i].setBackgroundColor(number_of_days[i].getContext().getResources().getColor(R.color.purple_200));
                my_hours_of_days[i].setWidth(250);
                my_hours1_of_days[i].setWidth(250);
                my_hours2_of_days[i].setWidth(250);
            }

            days_month.addView(number_of_days[i]);
            my_hours.addView(my_hours_of_days[i]);
            my_hours1.addView(my_hours1_of_days[i]);
            my_hours2.addView(my_hours2_of_days[i]);


        }
        System.out.println(Arrays.toString(number_of_days));

    }
            @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}