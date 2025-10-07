package com.example.eventsincalendar;



import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.widget.RemoteViews;


public class MyWidget2 extends AppWidgetProvider {

    static final String LOG_TAG = "myLogs";
    private DatabaseHelperEv mydb;
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(LOG_TAG, "onEnabled");

    }
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        ArrayList<ArrayList<String>> str;
        try (DatabaseHelperEv mydb = new DatabaseHelperEv(context)) {
            str = mydb.getAllRows();
        }
        System.out.println(str);
        mydb = new DatabaseHelperEv(context.getApplicationContext());


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
        Date date = cldr.getTime();

        int dayOfWeek = cldr.get(Calendar.DAY_OF_WEEK);
        String[] daysOfWeek = context.getResources().getStringArray(R.array.days_of_week_short);
        String[] month = context.getResources().getStringArray(R.array.months);

        // Define the desired date format
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("EE dd-MM-yyyy");
        String sDate =  sdf.format(date);
        System.out.println(sDate);
        StringBuilder sb = new StringBuilder();

        boolean search = mydb.checkDataExistOrNot(sDate, DatabaseHelperEv.TABLE);
        String eve = mydb.getEvents(sDate, DatabaseHelperEv.TABLE);
        //String stri = String.valueOf(eve);
        String str1 =  "<font color=\"#0000FF\">" + String.valueOf(eve)+ " <br>";
        System.out.println(str);
        if (search){

            sb.append(str1).append(" ");
            System.out.println(sb);

        }
        String weekday =  daysOfWeek[dayOfWeek-1];
        System.out.println(month[current_month]+" "+ current_year);
        String searchElement = month[current_month]+" "+ current_year;
        System.out.println("searchElement="+searchElement);
        int index_i = 0;
        int size = str.size();
        for (int i = 0; i < size; i++) {
            if (searchElement.equals(str.get(i).get(0))) {
                index_i = i;
                break;
            }

        }
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss a");
        Date date1 = cldr.getTime();
        String data1 =  sdf1.format(date1);
        System.out.println(data1);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy");
        String data =  sdf2.format(date);

        for (int widgetId : appWidgetIds) {
            //String number = String.format("%03d", (new Random().nextInt(900) + 100));

            @SuppressLint("RemoteViewLayout")
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
            //remoteViews.setTextViewText(R.id.hours, number);
            String d = str.get(index_i).get(0);
            System.out.println("current month and year=" + d);
            if (sb.length() == 0) {
//                event.setHint(sDate + " нет событий за этот день!");
                remoteViews.setTextViewText(R.id.date, "нет событий ");//го
                remoteViews.setTextViewText(R.id.btnListen, "");
            }else {
                if (sb.length() < 15) {
                    remoteViews.setTextViewText(R.id.btnListen, "");
                    remoteViews.setTextViewText(R.id.date, Html.fromHtml(String.valueOf(sb), Html.FROM_HTML_MODE_LEGACY));

                }else{
                    remoteViews.setTextViewText(R.id.date, Html.fromHtml(String.valueOf(sb), Html.FROM_HTML_MODE_LEGACY));
                }
            }
            remoteViews.setTextViewText(R.id.summary, "за сегодня: " + weekday + " " + data);//цена за час

            // обновление виджета при нажатии на дату
            Intent intent = new Intent(context, MyWidget2.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            //intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    widgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            remoteViews.setOnClickPendingIntent(R.id.widget, pendingIntent);

            //открываем прослушку события при нажатии на грамофон
            Intent intent5 = new Intent(context, ListenActivity.class); // Запускаем главную активность (можно другую)
            PendingIntent pIntentHomeFragment5 = PendingIntent.getActivity(context, 0, intent5, PendingIntent.FLAG_IMMUTABLE);
            remoteViews.setOnClickPendingIntent(R.id.listening, pIntentHomeFragment5);


            //открываем окно внесения события при нажатии а на дату
//            Intent intent3 = new Intent(context, ReviewActivity.class); // Запускаем главную активность (можно другую)
//            PendingIntent pIntentHomeFragment = PendingIntent.getActivity(context, 0, intent3, PendingIntent.FLAG_IMMUTABLE);
//            remoteViews.setOnClickPendingIntent(R.id.today, pIntentHomeFragment);

            //открываем окно проссмотра события на сегодня при нажатии на "читать полностью"
            Intent intent4 = new Intent(context, ReviewTodayActivity.class); // Запускаем главную активность (можно другую)
            PendingIntent pIntentHomeFragment4 = PendingIntent.getActivity(context, 0, intent4, PendingIntent.FLAG_IMMUTABLE);
            remoteViews.setOnClickPendingIntent(R.id.btnListen, pIntentHomeFragment4);


            //открываем календарь при нажатии на "мои события"
            Intent intent2 = new Intent(context, MainActivity.class); // Запускаем главную активность (можно другую)
            PendingIntent pIntentMainActivity = PendingIntent.getActivity(context, 0, intent2, PendingIntent.FLAG_IMMUTABLE);
            remoteViews.setOnClickPendingIntent(R.id.my_events, pIntentMainActivity);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(LOG_TAG, "onDeleted " + Arrays.toString(appWidgetIds));
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(LOG_TAG, "onDisabled");
    }

}