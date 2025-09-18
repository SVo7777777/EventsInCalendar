package com.example.eventsincalendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
public class ListenActivity extends Activity {

    private TextToSpeech tts;
    private String text;
    private String t;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.x = -20;
        params.height = 100;
        params.width = 550;
        params.y = -10;
        this.getWindow().setAttributes(params);

        setContentView(R.layout.listen);

        Calendar cldr = Calendar.getInstance(Locale.ROOT);
        Date date = cldr.getTime();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String sDate =  sdf.format(date);
        StringBuilder sb = new StringBuilder();

        t = "Мои события за сегодня.";
        boolean ev = false;

        boolean exists = FileEmpty.fileExistsInSD("event_diary.txt");
        if (exists) {

            try (FileInputStream fis = openFileInput("event_diary.txt");
                 InputStreamReader isr = new InputStreamReader(fis);
                 BufferedReader br = new BufferedReader(isr)) {
                String line;

                while ((line = br.readLine()) != null) {
                    boolean contains = line.contains(sDate);
                    if (contains) {
//                         = line.substring(0, 11);
                        String event = line.substring(11);
                        sb.append(event).append(" ");
                        ev = true;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        text = t + String.valueOf(sb);

        // Initialize TextToSpeech instance
        boolean finalEv = ev;
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    // TTS engine is successfully initialized
                    // Set language and other configurations if needed
                    Locale locale = new Locale("RU");
                    tts.setLanguage(locale);
                    //int result = tts.setLanguage(Locale.RU);
                    int result = tts.setLanguage(locale);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(ListenActivity.this, "Language not supported", Toast.LENGTH_SHORT).show();

                    } else {
                        // TTS engine is ready
                        // Proceed with speaking
                        if (finalEv) {
                            speak(text);
                        }else{
                            speak("За сегодня нет событий.");
                        }
                        //close();

                        onStop();

                    }
                } else {
                    Toast.makeText(ListenActivity.this, "Initialization failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void speak(String text) {
        if (tts != null) {
            // Speak the text
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            checkSpeaking(true);
        }
    }
    private void checkSpeaking(boolean isSpeaking)
    {
        System.out.println("Speaking is: " + isSpeaking);
        if (isSpeaking)
        {
            new Handler().postDelayed(()->
            {
                if (tts.isSpeaking()) {
                    checkSpeaking(true);
                }
                else {
                    checkSpeaking(false);
                    close();
                }
            },300);
        }
        else
        {
            //Get time
        }
    }

    public void close() {

        finishAffinity();
    }
    protected void onDestroy() {
        // Shutdown TTS engine when activity is destroyed
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    protected void onStop(){

        super.onStop();
    }


}
