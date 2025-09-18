package com.example.eventsincalendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventsincalendar.ui.home.HomeFragment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReviewTodayActivity extends Activity {
    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    Intent resultValue;
    public HomeFragment fragment;
    LinearLayout ln;
    TextView ev;
    TextView day;
    String wd;
    Calendar calendar = Calendar.getInstance();
    int current_month =  calendar.get(Calendar.MONTH);

    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.x = -20;
        params.height = 100;
        params.width = 850;
        params.y = -10;
        this.getWindow().setAttributes(params);
        setContentView(R.layout.activity_review_today);

        EditText event = findViewById(R.id.editTextTextMultiLine);
        //Button close = findViewById(R.id.close);
        TextView month1 = findViewById(R.id.month);
        TextView day_of_weeks = findViewById(R.id.day_of_weeks);

        Calendar ci = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy EEEE");
        System.out.println(format.format(ci.getTime()));
        String today = format.format(ci.getTime());
        String[] month = getResources().getStringArray(R.array.months);

        String[] daysOfWeek = getResources().getStringArray(R.array.days_of_week_short);
        int dayOfWeek = ci.get(Calendar.DAY_OF_WEEK);
        String weekday =  daysOfWeek[dayOfWeek-1];


        String day = today.substring(0, 2);
        System.out.println(day);
        //number.setText(day);
        String month2 = today.substring(3, 5);
        System.out.println(month2);
        //month1.setText(month[current_month]);
        String year = today.substring(6, 10);
        System.out.println(year);
        String sDate = today.substring(0, 10);
        //year1.setText(year);
        String dw = today.substring(11);
        day_of_weeks.setText("за сегодня: "+weekday+" "+sDate);
        System.out.println(dw);




        boolean exists = FileEmpty.fileExistsInSD("event_diary.txt");
        if (exists) {
            StringBuilder sb = new StringBuilder();
            try (FileInputStream fis = openFileInput("event_diary.txt");
                 InputStreamReader isr = new InputStreamReader(fis);
                 BufferedReader br = new BufferedReader(isr)) {
                String line;

                while ((line = br.readLine()) != null) {
                    boolean contains = line.contains(sDate);
                    if (contains) {
                        String day1 = line.substring(0, 11);
                        String event1 = line.substring(11);
                        String str =  "<font color=\"#0000FF\">"  + day1 + "</font>" + event1+ " <br>";
                        sb.append(str);
                    }else {
//                                    delete.setEnabled(false);
//                                    editor.setEnabled(false);
                    }
                }

                event.setText(Html.fromHtml(String.valueOf(sb), Html.FROM_HTML_MODE_LEGACY));
                //event.setHint(Html.fromHtml(st, Html.FROM_HTML_MODE_LEGACY));
                if (sb.length() == 0) {
                    event.setHint(sDate + " нет событий за этот день!");
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


//        close.setOnClickListener(new  View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                finishAffinity();
//            }
//        });



    }





}
