package com.example.calendarhours;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends Activity {
    // Время в миллисекундах, в течение которого будет отображаться заставка
    private final int SPLASH_DISPLAY_LENGTH = 500;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // По истечении времени запускаем главный активити, а заставку закрываем
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                //SplashActivity.this.startActivity(mainIntent);
                startActivity(mainIntent);
                //SplashActivity.this.finish();
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}