package com.example.eventsincalendar;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;

import com.example.eventsincalendar.ui.home.HomeFragment;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        // Время в миллисекундах, в течение которого будет отображаться заставка
        int SPLASH_DISPLAY_LENGTH = 500;
        new Handler().postDelayed(() -> {
            // По истечении времени запускаем главный активити, а заставку закрываем
            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
            //SplashActivity.this.startActivity(mainIntent);
            startActivity(mainIntent);
            //SplashActivity.this.finish();
            finish();
        }, SPLASH_DISPLAY_LENGTH);
    }
}