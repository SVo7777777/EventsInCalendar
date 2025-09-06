package com.example.eventsincalendar;

import static com.example.eventsincalendar.MyWidget2.LOG_TAG;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.example.eventsincalendar.ui.home.HomeFragment;

import androidx.fragment.app.FragmentManager;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReviewActivity extends Activity {
    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    Intent resultValue;
    public HomeFragment fragment;
    LinearLayout ln;
    TextView ev;
    TextView day;
    String wd;
    Calendar calendar = Calendar.getInstance();
    int current_month =  calendar.get(Calendar.MONTH);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        EditText event = findViewById(R.id.editTextTextMultiLine);
        Button add = findViewById(R.id.button);
        Button close = findViewById(R.id.close);
        Button delete = findViewById(R.id.delete);
        Button editor = findViewById(R.id.editor);
        TextView number = findViewById(R.id.number);
        TextView year1 = findViewById(R.id.year);
        TextView month1 = findViewById(R.id.month);
        TextView day_of_weeks = findViewById(R.id.day_of_weeks);

        Calendar ci = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy EEEE");
        System.out.println(format.format(ci.getTime()));
        String today = format.format(ci.getTime());
        String[] month = getResources().getStringArray(R.array.months);

        String day = today.substring(0, 2);
        System.out.println(day);
        number.setText(day);
        String month2 = today.substring(3, 5);
        System.out.println(month2);
        month1.setText(month[current_month]);
        String year = today.substring(6, 10);
        System.out.println(year);
        String sDate = today.substring(0, 10);
        year1.setText(year);
        String dw = today.substring(11);
        day_of_weeks.setText(dw);
        System.out.println(dw);



        add.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String data = String.valueOf(event.getText());
                System.out.println("data="+data);
//                Drawable icon = ContextCompat.getDrawable(requireContext(), R.drawable.free_icon_check_mark_5290982);
//                event1.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
                //event1.setText("event");
                //event1.setCompoundDrawablesWithIntrinsicBounds(R.id.checkbox_on_background, 0, 0, 0);

                //сохранение события в (базу данных) пока в текстовый файл
                if  (data.length() >= 20) {
                    try (FileOutputStream fos = openFileOutput("event_diary.txt", MODE_APPEND);
                         OutputStreamWriter osw = new OutputStreamWriter(fos)) {
                        //String data = String.valueOf(textMultiline.getText());
                        osw.write(sDate + ": " + data + "\n");
                        Toast.makeText(getApplicationContext(),  "Запись внесена! ", Toast.LENGTH_LONG).show();

                        //вывод диалогового окна, что запись внесена
//                        String attention = "запись внесена";
//                        CustomDialogFragment dialog = new CustomDialogFragment();
//                        Bundle args = new Bundle();
//                        args.putString("attention", attention);
//                        dialog.setArguments(args);
//                        dialog.show(new FragmentManager(),"custom" );


                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }  else {
                    Toast.makeText(getApplicationContext(),  "Запишите событие, а потом внесите! ", Toast.LENGTH_LONG).show();
                }
                //Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();//display the text of button1
            }
        });


        close.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });



    }





}
