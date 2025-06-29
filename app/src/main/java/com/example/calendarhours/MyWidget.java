package com.example.calendarhours;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;


import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class MyWidget extends AppWidgetProvider {

    static final String LOG_TAG = "myLogs";
    private static final String ACTION_UPDATE_CLICK_NEXT = "action.UPDATE_CLICK_NEXT";
    private static final String ACTION_UPDATE_CLICK_PREVIOUS = "action.UPDATE_CLICK_PREVIOUS";
    //public static DatabaseHelper mydb ;
//    String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
//// textView is the TextView view that should display it
//textView.setText(currentDateTimeString);

    //для отображения виджета на экране блокировки
//    AppWidgetManager appWidgetManager;
//    int widgetId;
//    Bundle myOptions = appWidgetManager.getAppWidgetOptions (widgetId);
//
//    // Get the value of OPTION_APPWIDGET_HOST_CATEGORY
//    int category = myOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_HOST_CATEGORY, -1);
//
//    // If the value is WIDGET_CATEGORY_KEYGUARD, it's a lockscreen widget
//    boolean isKeyguard = category == AppWidgetProviderInfo.WIDGET_CATEGORY_KEYGUARD;
//
//    int baseLayout = isKeyguard ? R.layout.widget : R.layout.widget;

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(LOG_TAG, "onEnabled");

    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {

//        //scheduleAlarm(context);
//        //scheduleWork(context);
        for (int appWidgetId : appWidgetIds) {
            //updateAppWidget2(context, appWidgetManager, appWidgetId);
            String number = String.format("%03d", (new Random().nextInt(900) + 100));

//            ArrayList<ArrayList<String>> str;
//            try (DatabaseHelper mydb = new DatabaseHelper(context)) {
//                str = mydb.getAllRows();
//            }
//            System.out.println(str);
//
//            Calendar calendar = Calendar.getInstance();
//            int current_year = calendar.get(Calendar.YEAR);
//            int current_month = calendar.get(Calendar.MONTH);
//            int current_day = calendar.get(Calendar.DATE);
//            @SuppressLint("SimpleDateFormat")
//            SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
//            String month_name = month_date.format(calendar.getTime());
//            System.out.println(current_day);
//            System.out.println(month_name);
//            System.out.println(current_year);
//
//            Calendar cldr = Calendar.getInstance(Locale.ROOT);
//
//            int dayInd = loadDayId(context, appWidgetId);
//            switch (dayInd) {
//                case 0:
//                    cldr.add(Calendar.DATE, 2);
//                    break;
//                case 1:
//                    cldr.add(Calendar.DATE, 1);
//                    break;
//                case 2:
//                    break;
//                case 3:
//                    cldr.add(Calendar.DATE, -1);
//                    break;
//                case 4:
//                    cldr.add(Calendar.DATE, -2);
//                    break;
//            }
//
//            Date date = cldr.getTime();
//
//            int dayOfWeek = cldr.get(Calendar.DAY_OF_WEEK);
//            String[] daysOfWeek = context.getResources().getStringArray(R.array.days_of_week_short);
//            String[] month = context.getResources().getStringArray(R.array.months);
//
//            // Define the desired date format
//            @SuppressLint("SimpleDateFormat")
//            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
//            String data =  sdf.format(date);
//            String weekday =  daysOfWeek[dayOfWeek-1];
//            System.out.println(month[current_month]+" "+String.valueOf(current_year));
//            String searchElement = month[current_month]+" "+String.valueOf(current_year);
//            System.out.println("searchElement="+searchElement);
//            int index_i = 0;
//            int size = str.size();
//            for (int i = 0; i < size; i++) {
//                if (searchElement.equals(str.get(i).get(0))) {
//                    // Если элемент найден, установить флаг found в true и выйти из цикла
//                    index_i = i;
//                    break;
//                }
//
//            }
//            RemoteViews views = new RemoteViews(context.getPackageName(),
//                    R.layout.widget);
//            String d = str.get(index_i).get(0);
//            System.out.println("current month and year="+d);
//            views.setTextViewText(R.id.date, "за "+d);//го
//            String h = str.get(index_i).get(2);
//            views.setTextViewText(R.id.hours, "всего часов: "+h);//всего часов
//            String sa = str.get(index_i).get(3);
//            views.setTextViewText(R.id.salary, "заработано: "+sa);//всего заработано
//            views.setTextViewText(R.id.summary, "сегодня: "+weekday+" "+data);//цена за час
//            SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss a");
//            Date date1 = cldr.getTime();
//            String data1 =  sdf1.format(date1);
//            System.out.println(data1);
//
//            Intent intent1 = new Intent(context, MainActivity.class); // Запускаем главную активность (можно другую)
//            PendingIntent pIntentMainActivity = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_IMMUTABLE);
//            views.setOnClickPendingIntent(R.id.my_hours, pIntentMainActivity); // R.id.appwidget_startMainActivity — название кнопки в форме виджета

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            views.setTextViewText(R.id.hours, number);
            Intent intent = new Intent(context, MyWidget.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.summary, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
            Toast.makeText(context, "Widget has been updated! ", Toast.LENGTH_SHORT).show();
        }

        System.out.println("Widget has been updated! ");

        //updateAppWidget2(context, appWidgetManager, appWidgetId);
//        for (int i = 0; i < count; i++) {
//            int widgetId = appWidgetIds[i];
//            @SuppressLint("DefaultLocale")
//            String number = String.format("%03d", (new Random().nextInt(900) + 100));
//
//            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
//                    R.layout.widget);
//            remoteViews.setTextViewText(R.id.hours, number);
//
//            Intent intent = new Intent(context, MyWidget.class);
//            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
//                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            remoteViews.setOnClickPendingIntent(R.id.summary, pendingIntent);
//            appWidgetManager.updateAppWidget(widgetId, remoteViews);
//        }
//        Intent intentUpdate = new Intent(context, NewAppWidget.class);
//        intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//        RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.widget);
//        PendingIntent pendingUpdate = PendingIntent.getBroadcast(
//                context, appWidgetId, intentUpdate,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//
//        views.setOnClickPendingIntent(R.id.widget, getPendingSelfIntent(context, ACTION_UPDATE_CLICK_NEXT));
//        //views.setOnClickPendingIntent(R.id.previousButtonWidget, getPendingSelfIntent(context, ACTION_UPDATE_CLICK_PREVIOUS));


    }
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);
        Toast.makeText(context, "Clicked!!", Toast.LENGTH_SHORT).show();
        Log.d(LOG_TAG, "Clicked!!");
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(LOG_TAG, "onDeleted " + Arrays.toString(appWidgetIds));
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(LOG_TAG, "onDisabled");
    }

    public static void updateAppWidget2(Context context, AppWidgetManager appWidgetManager, int appWidgetId)
    {

        ArrayList<ArrayList<String>> str;
        try (DatabaseHelper mydb = new DatabaseHelper(context)) {
            str = mydb.getAllRows();
        }
        System.out.println(str);

        Calendar calendar = Calendar.getInstance();
        int current_year = calendar.get(Calendar.YEAR);
        int current_month = calendar.get(Calendar.MONTH);
        int current_day = calendar.get(Calendar.DATE);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(calendar.getTime());
        System.out.println(current_day);
        System.out.println(month_name);
        System.out.println(current_year);

        Calendar cldr = Calendar.getInstance(Locale.ROOT);

        int dayInd = loadDayId(context, appWidgetId);
        switch (dayInd) {
            case 0:
                cldr.add(Calendar.DATE, 2);
                break;
            case 1:
                cldr.add(Calendar.DATE, 1);
                break;
            case 2:
                break;
            case 3:
                cldr.add(Calendar.DATE, -1);
                break;
            case 4:
                cldr.add(Calendar.DATE, -2);
                break;
        }

        Date date = cldr.getTime();

        int dayOfWeek = cldr.get(Calendar.DAY_OF_WEEK);
        String[] daysOfWeek = context.getResources().getStringArray(R.array.days_of_week_short);
        String[] month = context.getResources().getStringArray(R.array.months);

        // Define the desired date format
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String data =  sdf.format(date);
        String weekday =  daysOfWeek[dayOfWeek-1];
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        views.setTextViewText(R.id.date, "за "+month[current_month]+" "+String.valueOf(current_year));//год

        System.out.println(month[current_month]+" "+String.valueOf(current_year));
        String searchElement = month[current_month]+" "+String.valueOf(current_year);
        System.out.println("searchElement="+searchElement);
        int index_i = 0;
        int size = str.size();
        for (int i = 0; i < size; i++) {
                if (searchElement.equals(str.get(i).get(0))) {
                    // Если элемент найден, установить флаг found в true и выйти из цикла
                    index_i = i;
                    break;
                }

        }

        String d = str.get(index_i).get(0);
        System.out.println("current month and year="+d);
        views.setTextViewText(R.id.date, "за "+d);//го
        String h = str.get(index_i).get(2);
        views.setTextViewText(R.id.hours, "всего часов: "+h);//всего часов
        String sa = str.get(index_i).get(3);
        views.setTextViewText(R.id.salary, "заработано: "+sa);//всего заработано
        views.setTextViewText(R.id.summary, "сегодня: "+weekday+" "+data);//цена за час

//        Intent intent = new Intent(context, MainActivity.class); // Запускаем главную активность (можно другую)
//        PendingIntent pIntentMainActivity = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
//        //views.setOnClickPendingIntent(R.id.widget, pIntentMainActivity); // R.id.appwidget_startMainActivity — название кнопки в форме виджета
        // ...
        Intent intent = new Intent(context, MyWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.summary, pendingIntent);
        //appWidgetManager.updateAppWidget(appWidgetId, views);

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss a");
        Date date1 = cldr.getTime();
        String data1 =  sdf1.format(date1);
        System.out.println(data1);
        appWidgetManager.updateAppWidget(appWidgetId, views); // Обязательно нужно вызвать этот метод, иначе обработка не будет работать


        // Construct the RemoteViews object

        //views.setTextViewText(R.id.date, da);
        //views.setTextViewText(R.id.weekDay, da);

        // Instruct the widget manager to update the widget
        //appWidgetManager.updateAppWidget(appWidgetId, views);
    }
    public static void scheduleAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DayWidgetUpdateReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Request permission if needed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Intent settingsIntent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(settingsIntent);
                return; // Don't schedule the alarm until permission is granted
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + 60 * 1000,
                    pendingIntent
            );
        }

    }

    public static void scheduleWork(Context context) {
        Constraints constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(false) // Allow running even when battery is low
                .build();

        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(DayWidgetUpdateWorker.class, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "hc_day_widget_update_work",
                ExistingPeriodicWorkPolicy.UPDATE,
                request
        );
    }
    private static final String PREFS_NAME = "hours_calculator_widget";
    public static int loadDayId(Context context, int widgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt("day_for_" + widgetId, -1);
    }

}