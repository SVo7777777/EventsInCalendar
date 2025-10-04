package com.example.eventsincalendar.ui.home;

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
import com.example.eventsincalendar.DatabaseHelperEv;

import com.example.eventsincalendar.FileEmpty;
import com.example.eventsincalendar.MyWidget2;
import com.example.eventsincalendar.R;
import com.example.eventsincalendar.ReviewOWeek;
import com.example.eventsincalendar.ReviewOYear;
import com.example.eventsincalendar.ReviewOnMonth;
import com.example.eventsincalendar.databinding.FragmentHomeBinding;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private DatabaseHelperEv mydb;
    public Button previous_month;
    public Button next_month;
    public Button previous_year;
    public Button next_year;
    public Button month;
    public Button year;
    LinearLayout view;

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



    public int day_OfWeekOfFirstDayOfMonth;
    public int date_End;
    @SuppressLint("SdCardPath")
    private static final String APP_SD_PATH = "/data/data/com.example.eventsincalendar";



    @SuppressLint({"WrongViewCast", "SetTextI18n"})
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);



        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mydb = new DatabaseHelperEv(getContext());

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

        onReviewMonthClick(month);
        onReviewYearClick(year);

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
    public void onReviewMonthClick(Button button2) {
        button2.setOnClickListener(v -> {
            Intent intent2 = new Intent(getContext(), ReviewOnMonth.class);
            @SuppressLint("SimpleDateFormat") final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Calendar c = Calendar.getInstance();
            c.set(Integer.parseInt(year.getText().toString()), Arrays.asList(monthNames).indexOf((String) month.getText()), 1);
            String data = sdf.format(c.getTime());
            intent2.putExtra("data", data);
            startActivity(intent2);
        });
    }

    public void onReviewYearClick(Button button2) {
        button2.setOnClickListener(v -> {
            Intent intent2 = new Intent(getContext(), ReviewOYear.class);
            @SuppressLint("SimpleDateFormat") final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Calendar c = Calendar.getInstance();
            c.set(Integer.parseInt(year.getText().toString()), Arrays.asList(monthNames).indexOf((String) month.getText()), 1);
            String data = sdf.format(c.getTime());
            intent2.putExtra("data", data);
            startActivity(intent2);
        });
    }
    @SuppressLint("SetTextI18n")
    private void showCalendar(String mon, int yea, int wee, int mpred, int dayOfWeekOfFirstDayOfMonth, int dateEnd) {
        month.setText(mon);
        year.setText(Integer.toString(yea));
        date_End = dateEnd;
        day_OfWeekOfFirstDayOfMonth = dayOfWeekOfFirstDayOfMonth;
//        Drawable icon = ContextCompat.getDrawable(requireContext(), R.drawable.free_icon_check_mark_5290644);
//        events[6][6].setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);

        @SuppressLint("SimpleDateFormat")
        final SimpleDateFormat sdf1 = new SimpleDateFormat("EE dd-MM-yyyy");

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
                events[i][j].setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

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
//                        mydb = new DatabaseHelpereEv(getContext());
                        @SuppressLint("SimpleDateFormat")
                        final SimpleDateFormat sdf = new SimpleDateFormat("EE dd-MM-yyyy");
                        Calendar c = Calendar.getInstance();
                        c.set(Integer.parseInt(year.getText().toString()), Arrays.asList(monthNames).indexOf((String) month.getText()), d);
                        String sDate = sdf.format(c.getTime());

                        boolean search = mydb.checkDataExistOrNot(sDate, DatabaseHelperEv.TABLE);
                        String ev = mydb.getEvents(sDate, DatabaseHelperEv.TABLE);
                        if (search){
                            Drawable icon = ContextCompat.getDrawable(requireContext(), R.drawable.free_icon_check_mark_5290982);
                            events[i][j].setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
                        }else{
                            events[i][j].setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

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
                    Button delete = view.findViewById(R.id.delete);
                    TextView number = view.findViewById(R.id.number);
                    number.setText(day1.getText());
                    TextView year1 = view.findViewById(R.id.year);



                    year1.setText(year.getText().toString());
                    TextView month1 = view.findViewById(R.id.month);
                    month1.setText((String) month.getText());
                    TextView day_of_weeks = view.findViewById(R.id.day_of_weeks);
                    day_of_weeks.setText(day_week);

                    @SuppressLint("SimpleDateFormat")
                    final SimpleDateFormat sdf = new SimpleDateFormat("EE dd-MM-yyyy");
                    Calendar c = Calendar.getInstance();
                    c.set(Integer.parseInt(year.getText().toString()), Arrays.asList(monthNames).indexOf((String) month.getText()), Integer.parseInt((String) number.getText()));
                    String sDate = sdf.format(c.getTime());
                    //выводится событие, если оно есть
                    boolean search = mydb.checkDataExistOrNot(sDate, DatabaseHelperEv.TABLE);
                    String ev = mydb.getEvents(sDate, DatabaseHelperEv.TABLE);
                    //String str =  "<font color=\"#0000FF\">"  + sDate+ ": " + "</font>" + ev+ " <br>";

                    if (search){
                        event.setText(Html.fromHtml(String.valueOf(ev), Html.FROM_HTML_MODE_LEGACY));
                        //add.setEnabled(false);
                        delete.setEnabled(true);
                    }else {
                        event.setHint(sDate + ": нет событий за этот день!");
                        //add.setEnabled(true);
                        delete.setEnabled(false);

                        event.requestFocus();
                        event.setSelection(event.getText().length());
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
                            if  (data.length() > 1){
                                //mydb = new DatabaseHelperEv(getContext());
                                boolean search = mydb.checkDataExistOrNot(sDate, DatabaseHelperEv.TABLE);
                                System.out.println("search: "+search);
                                if (search) {
                                    Toast.makeText(getActivity(), sDate + " уже есть!", Toast.LENGTH_SHORT).show();
                                    System.out.println(data + " уже есть!");
                                    String ev = mydb.getEvents(sDate, DatabaseHelperEv.TABLE);

                                    int id = mydb.GetId(sDate, DatabaseHelperEv.TABLE);
                                    boolean update_events = mydb.updateEvents(id, sDate, String.valueOf(data),  DatabaseHelperEv.TABLE);
                                    if (update_events){
                                        System.out.println("событие изменено");
                                    }
                                    //String s = mydb.getSum(data);
                                    System.out.println("за "+sDate+" событие: "+ev);
                                    //System.out.println("s="+sum);
                                    //addHoursInCalendar(h);
                                }else {
                                    System.out.println(sDate);
                                    System.out.println(data);
                                    mydb.insertContact(sDate, String.valueOf(data), null,null,null,null, DatabaseHelperEv.TABLE);

                                    String attention = "За " + sDate + "\nдобавлено событие:\n" + data;
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

                            //обновление виджета
                            Intent intentq = new Intent(getActivity(), MyWidget2.class);
                            intentq.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                            int[] ids = AppWidgetManager.getInstance(getActivity().getApplication()).getAppWidgetIds(new ComponentName(getActivity().getApplication(), MyWidget2.class));
                            intentq.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
                            getActivity().sendBroadcast(intentq);
                            //Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();//display the text of button1
                        }

                    });
                    builder.setView(view);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    delete.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onClick(View view) {
//
//                            Drawable icon = ContextCompat.getDrawable(requireContext(), R.drawable.free_icon_check_mark_5290982);

                            int id = mydb.GetId(sDate, DatabaseHelperEv.TABLE);
                            mydb.deleteContact(id);
                            event1.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

                            boolean search = mydb.checkDataExistOrNot(sDate, DatabaseHelperEv.TABLE);
                            if (!search) {
                                Toast.makeText(getActivity(), "запись удалена!", Toast.LENGTH_LONG).show();
                                event.setText("");
                            }

                            //обновление виджета
                            Intent intentq = new Intent(getActivity(), MyWidget2.class);
                            intentq.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                            int[] ids = AppWidgetManager.getInstance(getActivity().getApplication()).getAppWidgetIds(new ComponentName(getActivity().getApplication(), MyWidget2.class));
                            intentq.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
                            getActivity().sendBroadcast(intentq);
                        }
                    });
                    close.setOnClickListener(new  View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
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
