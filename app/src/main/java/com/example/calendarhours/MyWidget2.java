package com.example.calendarhours;

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
import android.util.Log;
import android.widget.RemoteViews;


public class MyWidget2 extends AppWidgetProvider {

    static final String LOG_TAG = "myLogs";


    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(LOG_TAG, "onEnabled");

    }
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
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
        Date date = cldr.getTime();

        int dayOfWeek = cldr.get(Calendar.DAY_OF_WEEK);
        String[] daysOfWeek = context.getResources().getStringArray(R.array.days_of_week_short);
        String[] month = context.getResources().getStringArray(R.array.months);

        // Define the desired date format
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String data =  sdf.format(date);
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

        for (int widgetId : appWidgetIds) {
            //String number = String.format("%03d", (new Random().nextInt(900) + 100));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget);
            //remoteViews.setTextViewText(R.id.hours, number);
            String d = str.get(index_i).get(0);
            System.out.println("current month and year=" + d);
            remoteViews.setTextViewText(R.id.date, "за " + d);//го
            String h = str.get(index_i).get(2);
            remoteViews.setTextViewText(R.id.hours, "всего часов: " + h);//всего часов
            String sa = str.get(index_i).get(3);
            remoteViews.setTextViewText(R.id.salary, "заработано: " + sa);//всего заработано
            remoteViews.setTextViewText(R.id.summary, "сегодня: " + weekday + " " + data);//цена за час
            // обновление виджета при нажатии на дату внизу виджета
            Intent intent = new Intent(context, MyWidget2.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            //intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    widgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.summary, pendingIntent);

            //открываем календарь при нажатии виджет
            Intent intent2 = new Intent(context, MainActivity.class); // Запускаем главную активность (можно другую)
            PendingIntent pIntentMainActivity = PendingIntent.getActivity(context, 0, intent2, PendingIntent.FLAG_IMMUTABLE);
            //открываем календарь при нажатии на строку "мои рабочие часы"
            //remoteViews.setOnClickPendingIntent(R.id.my_hours, pIntentMainActivity); // R.id.appwidget_startMainActivity — название кнопки в форме виджета
            remoteViews.setOnClickPendingIntent(R.id.widget, pIntentMainActivity);
            //Toast.makeText(context, "Widget has been updated! ", Toast.LENGTH_SHORT).show();
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