package com.example.calendarhours;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.calendarhours.MyWidget;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            MyWidget.scheduleAlarm(context);
            MyWidget.scheduleWork(context);
        }
    }
}
