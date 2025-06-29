package com.example.calendarhours;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.calendarhours.MyWidget;

import javax.xml.transform.Result;

public class DayWidgetUpdateWorker extends Worker {
    public DayWidgetUpdateWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName widget = new ComponentName(context, MyWidget.class);
        int[] widgetIds = appWidgetManager.getAppWidgetIds(widget);

        for (int widgetId : widgetIds) {
            MyWidget.updateAppWidget2(context, appWidgetManager, widgetId);
        }

        return Result.success();
    }
}
