package com.example.calendarhours;

import  android.app.AlarmManager;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import com.example.calendarhours.MyWidget;

public class DayWidgetUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName componentName = new ComponentName(context, MyWidget.class);
        int[] widgetIds = appWidgetManager.getAppWidgetIds(componentName);

        for (int widgetId : widgetIds) {
            MyWidget.updateAppWidget(context, appWidgetManager, widgetId);
        }
    }
}
