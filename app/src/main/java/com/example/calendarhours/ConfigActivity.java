package com.example.calendarhours;


import static com.example.calendarhours.R.id.radioRed;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ConfigActivity extends Activity {

    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    Intent resultValue;

    final String LOG_TAG = "myLogs";

    public final static String WIDGET_PREF = "widget_pref";
    public final static String WIDGET_TEXT = "widget_text_";
    public final static String WIDGET_COLOR = "widget_color_";
    public  RadioButton red;
    public RadioButton green;
    public RadioButton blue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate config");
        red = findViewById(R.id.radioRed);
        green = findViewById(R.id.radioGreen);
        blue = findViewById(R.id.radioBlue);
        // извлекаем ID конфигурируемого виджета
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        // и проверяем его корректность
        if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        // формируем intent ответа
        resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);

        // отрицательный ответ
        setResult(RESULT_CANCELED, resultValue);

        setContentView(R.layout.config);
    }


    @SuppressLint({"NonConstantResourceId", "ApplySharedPref"})
    public void onClick(View v) {
        int selRBColor = ((RadioGroup) findViewById(R.id.rgColor))
                .getCheckedRadioButtonId();
        System.out.println("selRBColor="+selRBColor);
        int color = Color.RED;
        switch (selRBColor) {
            case 2131231451:
                color = Color.parseColor("#66ff0000");
                break;
            case 2131231450:
                color = Color.parseColor("#6600ff00");
                break;
            case 2131231449:
                color = Color.parseColor("#660000ff");
                break;
//            R.id.radioBlue


        }
        EditText etText = (EditText) findViewById(R.id.etText);

        // Записываем значения с экрана в Preferences
        SharedPreferences sp = getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(WIDGET_TEXT + widgetID, etText.getText()
                .toString());
        editor.putInt(WIDGET_COLOR + widgetID, color);
        editor.commit();

        // положительный ответ 
        setResult(RESULT_OK, resultValue);

        Log.d(LOG_TAG, "finish config " + widgetID);
        finish();
    }
}
