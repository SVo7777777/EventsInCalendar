package com.example.eventsincalendar.ui.home;

import static android.content.Context.MODE_APPEND;
import static android.graphics.Color.GRAY;
import static android.graphics.Color.LTGRAY;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.text.Html;
import android.util.TypedValue;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.eventsincalendar.CustomDialogFragment;
import com.example.eventsincalendar.DatabaseHelper;
import com.example.eventsincalendar.FileEmpty;
import com.example.eventsincalendar.MyWidget2;
import com.example.eventsincalendar.R;
import com.example.eventsincalendar.ReviewOWeek;
import com.example.eventsincalendar.ReviewOnMonth;
import com.example.eventsincalendar.databinding.FragmentHomeBinding;
import com.example.eventsincalendar.ui.dashboard.DashboardFragment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

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
    public TextView[][] days= new TextView[7][8];
    public TextView[][] events= new TextView[7][8];
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
    public int day_OfWeekOfFirstDayOfMonth;
    public int date_End;



    @SuppressLint({"WrongViewCast", "SetTextI18n"})
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
        textView3.setText("Часов: 0.0");
        editTextNumber = root.findViewById(R.id.editTextNumber);
        editTextNumber.setPaintFlags(View.INVISIBLE);
        button2 = root.findViewById(R.id.button2);
        salaryShowOnButtonClick(button2);

        //обновление виджета
        Intent intentq = new Intent(getActivity(), MyWidget2.class);
        intentq.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        int[] ids = AppWidgetManager.getInstance(getActivity().getApplication()).getAppWidgetIds(new ComponentName(getActivity().getApplication(), MyWidget2.class));
        intentq.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        getActivity().sendBroadcast(intentq);

        int e = 0;
        while (e < 7) {
            String weekId = "numweek_" + e;
            @SuppressLint("DiscouragedApi")
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
                @SuppressLint("DiscouragedApi")
                int bID = getResources().getIdentifier(llButtonId, "id", requireActivity().getPackageName());
                @SuppressLint("DiscouragedApi")
                int dID = getResources().getIdentifier(dayId, "id", requireActivity().getPackageName());
                @SuppressLint("DiscouragedApi")
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
        String data = month.getText() + " " + year.getText();
        System.out.println(data);
        String price = mydb.getPrice(data, DatabaseHelper.TABLE);
        System.out.println(price);
        String sal = mydb.getSalary(data, DatabaseHelper.TABLE);
        System.out.println(sal);
        //boolean update_salary = mydb.updateSalary(id, month_year, String.valueOf(sal), "plan1");
        if (price.equals("0.0") ){
            button2.setText("з/п");
            editTextNumber.setText("");
        }else {
            button2.setText(String.valueOf(sal));
            editTextNumber.setText(price);
        }
        for (int i = 1; i < 7; i++){
            onWeekMonthClick(number_of_week[i]);
        }

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }
    public void onWeekMonthClick(TextView btn) {
        btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
            @Override
            public void onClick(View v) {
                System.out.println(btn.getText());
                String[] week_days;
                String result = "";
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                int week = Integer.parseInt((String) btn.getText());
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.WEEK_OF_YEAR, week);
                cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                int week_start_day = cal.getFirstDayOfWeek();
                System.out.println(format.format(cal.getTime()));
                for (int i = week_start_day; i < week_start_day + 7; i++) {
                    cal.set(Calendar.DAY_OF_WEEK, i);
                    result += new SimpleDateFormat("dd-MM-yyyy").format(cal.getTime()) + " ";
                }
                System.out.println(result);
                week_days = result.split(" ");
                System.out.println(Arrays.toString(week_days));
                Intent intent = new Intent(getContext(), ReviewOWeek.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST",(Serializable)week_days);
                intent.putExtra("BUNDLE",args);
                intent.putExtra("week", week);
                //intent.putExtra("week_days", week_days);

                startActivity(intent);

            }
        });
    }
    public void onReviewMonthClick(View view) {
        Intent intent2 = new Intent(getContext(), ReviewOnMonth.class);
        @SuppressLint("SimpleDateFormat")
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(year.getText().toString()), Arrays.asList(monthNames).indexOf((String) month.getText()), 1);
        String data = sdf.format(c.getTime());
        intent2.putExtra("data", data);
        startActivity(intent2);
    }

    @SuppressLint("SetTextI18n")
    private void salaryShowOnButtonClick(Button button2) {
        button2.setOnClickListener(v -> {
            String h = String.valueOf(textView3.getText());
            String mon = (String) month.getText();
            String ye = (String) year.getText();
            String month_year = mon + " " + ye;
            DashboardFragment dashboardFragment = new DashboardFragment();
            try {
                split2 = h.split(" ");
                System.out.println(Arrays.toString(split2));
                System.out.println(split2[0]);
                System.out.println(split2[1]);
                String price = String.valueOf(editTextNumber.getText());
                //dashboardFragment.editTextNumber.setText(price);
                String hours = split2[1];
                float p = Float.parseFloat(hours) * Float.parseFloat(price);
                System.out.println(p);
                String salary = String.valueOf(p);
                int id = mydb.GetId(month_year, DatabaseHelper.TABLE);
                System.out.println("id="+id);
                System.out.println("month_year="+month_year);

                boolean update_hours = mydb.updateSalary(id, month_year, salary, "hours");
                if (update_hours){
                    Toast.makeText(getActivity(), "Зарплата изменена! Всего: "+salary, Toast.LENGTH_SHORT).show();
                }
                boolean update_price = mydb.updatePrice(id, month_year, price, "hours");
                if (update_price){
                    Toast.makeText(getActivity(), "Цена за час в этом месяце: "+price+" сохранена!", Toast.LENGTH_SHORT).show();
                }
                button2.setText(salary);
            }catch (NumberFormatException e) {
                Toast.makeText(getActivity(), "Введите цену за час!", Toast.LENGTH_LONG).show();
            }
            //обновление виджета
            Intent intentq = new Intent(getActivity(), MyWidget2.class);
            intentq.setAction("android.appwidget.action.APPWIDGET_UPDATE");
            int[] ids = AppWidgetManager.getInstance(getActivity().getApplication()).getAppWidgetIds(new ComponentName(getActivity().getApplication(), MyWidget2.class));
            intentq.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
            getActivity().sendBroadcast(intentq);

        });
    }
    @SuppressLint("SetTextI18n")
    private void showCalendar(String mon, int yea, int wee, int mpred, int dayOfWeekOfFirstDayOfMonth, int dateEnd) {
        month.setText(mon);
        year.setText(Integer.toString(yea));
        String mont = String.valueOf(month.getText());
        String y = String.valueOf(year.getText());
        //button2.setText("salary");
        date_End = dateEnd;
        day_OfWeekOfFirstDayOfMonth = dayOfWeekOfFirstDayOfMonth;
//        Drawable icon = ContextCompat.getDrawable(requireContext(), R.drawable.free_icon_check_mark_5290644);
//        events[6][6].setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);

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
        boolean exists = FileEmpty.fileExistsInSD("event_diary.txt");

        for (int i = 1; i < 7; i++) {
            number_of_week[i].setText(Integer.toString(wee));
            wee += 1;
            for (int j = 1; j < 8; j++) {
                if (i == 1 && j < dayOfWeekOfFirstDayOfMonth-1) {
                    buttons[i][j].setBackgroundColor(LTGRAY);
                    days[i][j].setText(Integer.toString(m));
                    days[i][j].setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    buttons[i][j].setEnabled(false);
                    days[i][j].setTextColor(GRAY);
                    events[i][j].setText("");
                    m += 1;
                } else {
                    if (d < dateEnd + 1) {
                        days[i][j].setText(Integer.toString(d));
                        days[i][j].setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
                        //events[i][j].setText("0");
                        events[i][j].setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        events[i][j].setTypeface(null, Typeface.BOLD);
                        buttons[i][j].setEnabled(true);
                        @SuppressLint("SimpleDateFormat")
                        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        Calendar c = Calendar.getInstance();
                        c.set(Integer.parseInt(year.getText().toString()), Arrays.asList(monthNames).indexOf((String) month.getText()), d);
                        String sDate = sdf.format(c.getTime());


                        if (exists) {
                            try (FileInputStream fis = requireContext().openFileInput("event_diary.txt");
                                 InputStreamReader isr = new InputStreamReader(fis);
                                 BufferedReader br = new BufferedReader(isr)) {
                                String line;

                                while ((line = br.readLine()) != null) {
                                    boolean contains = line.contains(sDate);
                                    if (contains) {
                                        Drawable icon = ContextCompat.getDrawable(requireContext(), R.drawable.free_icon_check_mark_5290982);
                                        events[i][j].setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);

                                        //events[i][j].setText("11");
                                        System.out.println("sDate="+sDate);
                                        break;
                                    }
                                }

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }else {
                            Toast.makeText(getContext(), "В Вашем календаре пока нет событий! Выберите дату, запишите событие  и внесите!", Toast.LENGTH_LONG).show();
                            //Toast.makeText(getContext(), "Запишите событие, а потом внесите! ", Toast.LENGTH_LONG).show();

                        }


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
                        days[i][j].setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
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
        textView3.setText("Часов:"+ sum);
        String data = mont+" "+y;
        mydb = new DatabaseHelper(getContext());
        boolean search = mydb.checkDataExistOrNot(data, DatabaseHelper.TABLE);
        System.out.println("search: "+search);
        if (search) {
            //Toast.makeText(getActivity(), data + " уже есть!", Toast.LENGTH_SHORT).show();
            System.out.println(data + " уже есть!");
            String h = mydb.getHours(data, DatabaseHelper.TABLE);
            //String s = mydb.getSum(data);
            System.out.println("за "+data+" часы: "+h);
            System.out.println("s="+sum);
            //addHoursInCalendar(h);
        }else {
            System.out.println("hours="+hours);
            System.out.println("sum="+ sum);
            mydb.insertContact(data, hours.toString(), "0.0","0.0","0.0","hours");
            mydb.insertContact(data, hours.toString(), "0.0","0.0","0.0","plan1");
            mydb.insertContact(data, hours.toString(), "0.0","0.0","0.0","plan2");
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
        textView3.setText("Часов: "+ sum);
        String data = month.getText()+" "+year.getText();
        String price = mydb.getPrice(data, DatabaseHelper.TABLE);
        System.out.println(data);
        System.out.println(price);
        button2.setText(String.valueOf(sum*Float.parseFloat(price)));
        editTextNumber.setText(price);
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
    public void setOnClick(LinearLayout btn, TextView day1, TextView event1,  String day_week) {

            btn.setOnClickListener(new View.OnClickListener() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View v) {
                    @SuppressLint("UseRequireInsteadOfGet")
                    AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                    view = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_review, null);
                    EditText event = view.findViewById(R.id.editTextTextMultiLine);
                    Button add = view.findViewById(R.id.button);
                    Button close = view.findViewById(R.id.close);
                    TextView number = view.findViewById(R.id.number);
                    number.setText(day1.getText());
                    TextView year1 = view.findViewById(R.id.year);

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
                    event.setText(sDate+": ");
                    boolean exists = FileEmpty.fileExistsInSD("event_diary.txt");
                    if (exists) {
                        StringBuilder sb = new StringBuilder();
                        try (FileInputStream fis = requireContext().openFileInput("event_diary.txt");
                             InputStreamReader isr = new InputStreamReader(fis);
                             BufferedReader br = new BufferedReader(isr)) {
                            String line;

                            while ((line = br.readLine()) != null) {
                                boolean contains = line.contains(sDate);
                                if (contains) {
                                    String day = line.substring(0, 11);
                                    String event1 = line.substring(11);
                                    String str =  "<font color=\"#0000FF\">"  + day + "</font>" + event1+ " <br>";
                                    st = str;
                                    add.setEnabled(false);
                                    sb.append(str);
                                }
                            }

                            event.setText(Html.fromHtml(String.valueOf(sb), Html.FROM_HTML_MODE_LEGACY));
                            //event.setHint(Html.fromHtml(st, Html.FROM_HTML_MODE_LEGACY));
                            if (sb.length() == 0) {
                                event.setHint(sDate + " нет событий за этот день!");
                                add.setEnabled(true);
                                event.requestFocus();
                                event.setSelection(event.getText().length());

                                //дата синяя
//                                String str = "<font color=\"#0000FF\">" + sDate+": " + "</font>" + " нет событий за этот день!";
//                                event.setHint(Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY));
//
                                //textMultiline.setText(Html.fromHtml("<font color=\"#0000FF\">" + data  + "</font>"+ " нет событий за этот день!"));


                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }else {
                        event.setHint(R.string.file_exist);

                    }

                    add.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onClick(View view) {
                            String data = String.valueOf(event.getText());
                            System.out.println("data="+data);
                            Drawable icon = ContextCompat.getDrawable(requireContext(), R.drawable.free_icon_check_mark_5290982);
                            event1.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
                            //event1.setText("event");
                            //event1.setCompoundDrawablesWithIntrinsicBounds(R.id.checkbox_on_background, 0, 0, 0);

                            //сохранение события в (базу данных) пока в текстовый файл
                            if  (data.length() >= 20) {
                                try (FileOutputStream fos = requireContext().openFileOutput("event_diary.txt", MODE_APPEND);
                                     OutputStreamWriter osw = new OutputStreamWriter(fos)) {
                                    //String data = String.valueOf(textMultiline.getText());
                                    osw.write(sDate + ": " + data + "\n");
                                    //вывод диалогового окна, что запись внесена
                                    String attention = "запись внесена";
                                    CustomDialogFragment dialog = new CustomDialogFragment();
                                    Bundle args = new Bundle();
                                    args.putString("attention", attention);
                                    dialog.setArguments(args);
                                    dialog.show(getParentFragmentManager(), "custom");

                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }  else {
                                Toast.makeText(getActivity(),  "Запишите событие, а потом внесите! ", Toast.LENGTH_LONG).show();
                            }
                            //Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();//display the text of button1
                        }
                    });
                    builder.setView(view);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    close.setOnClickListener(new  View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                }

                // Do whatever you want(str can be used here)

            });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
