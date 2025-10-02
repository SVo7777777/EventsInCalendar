package com.example.eventsincalendar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class ReviewOWeek extends AppCompatActivity {

    boolean addRecord;
    DatabaseHelperEv mydb;
    TextView textMultiline;
    TextView textView;
    ImageButton btnd;
    ImageButton btn_open;
    private int width, height;
    LinearLayout linear;
    Calendar calendar = Calendar.getInstance();
    public int current_year = calendar.get(Calendar.YEAR);
    public int current_month = calendar.get(Calendar.MONTH);
    public int current_day = calendar.get(Calendar.DATE);
    String current_data = "-"+ current_year;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_review_ondata);

        //верхняя полоса с названием и 3мя точками
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar); // Use the toolbar as the app bar
        //цвет для 3ёх точек и для названия
//        Objects.requireNonNull(toolbar.getOverflowIcon()).setColorFilter(Color.WHITE , PorterDuff.Mode.SRC_ATOP);

        //toolbar.setTitle("hello");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.review), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        textMultiline = findViewById(R.id.editTextTextMultiLine2);
        textView = findViewById(R.id.textView);
        btnd = findViewById(R.id.btnd);
        btn_open = findViewById(R.id.btn_open);
        linear = findViewById(R.id.lineard);
        mydb = new DatabaseHelperEv(getApplicationContext());
        addRecord = false;
        Intent intent = getIntent();
        int week = intent.getIntExtra("week", 0);


        btnd.setOnClickListener(v -> {
            System.out.println("hear-btnd");
            Log.d("size", linear.getWidth() + " " + linear.getWidth());
            boolean wr = CreatPDF.creatPDF(linear,week + current_data);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (wr){
                    String attention = "Итоги загружены в телефон в папку Download. В файл itogi_results"+week+ current_data + ".pdf";
                    CustomDialogFragment dialog = new CustomDialogFragment();
                    Bundle args = new Bundle();
                    args.putString("attention", attention);
                    dialog.setArguments(args);
                    dialog.show(this.getSupportFragmentManager(), "custom");
                }else{
                    String attention = "Включите разрешение ПАМЯТЬ для этого приложения (Настройки-->Приложения-->Календарь рабочих часов-->Разрешение-->Память--> Разрешить)";
                    CustomDialogFragment dialog = new CustomDialogFragment();
                    Bundle args = new Bundle();
                    args.putString("attention", attention);
                    dialog.setArguments(args);
                    dialog.show(this.getSupportFragmentManager(), "custom");
                }
                //creatPDF();
            }else {
                System.out.println("hear0000");
            }

        });

        btn_open.setOnClickListener(v -> {
            System.out.println("hear-btn_open");
            Log.d("size", "размер" + linear.getWidth() + " " + linear.getWidth());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                openPdf();
            }

        });



        System.out.println("week "+ week);

        ArrayList<ArrayList<String>> all_data = mydb.getAllRows();
        System.out.println(all_data);
        int size = all_data.size();
        System.out.println(size);
        System.out.println(all_data.get(0).get(0));
        System.out.println(all_data.get(0).get(1));


        Bundle args = intent.getBundleExtra("BUNDLE");
        assert args != null;
        String[] week_days = (String[]) args.getSerializable("ARRAYLIST");
        System.out.println((Arrays.toString(week_days))+" week_days");
        boolean exists = FileEmpty.fileExistsInSD("event_diary.txt");
        assert week_days != null;
        String weekDays = Arrays.toString(week_days);
        System.out.println("weekDays "+weekDays);
        String year = week_days[0].substring(6);

        StringBuilder sb = new StringBuilder();

        if ( !week_days.equals("")) {
            textView.setText("  за " + week + "ую неделю " +year +"г.:");
            for (int i = 0; i < size; i++) {
                System.out.println(all_data.get(i).get(1));
                String mon = all_data.get(i).get(0);
                String ev = all_data.get(i).get(1);

                //есть ли в строке дата из массива дней недели
                boolean contains = Arrays.stream(week_days).anyMatch(mon::contains);
                if (contains) {

                    String str = "<span style=\"background-color:#f3f402;\">" + mon + ": " + "</span>" + ev + " <br>";
                    System.out.println(str);
                    sb.append(str);

                    //textMultiline.setText(Html.fromHtml(textMultiline.getText() + str, Html.FROM_HTML_MODE_LEGACY));

                }
            }


            textMultiline.setText(Html.fromHtml(String.valueOf(sb), Html.FROM_HTML_MODE_LEGACY));
            if (sb.length() == 0) {
                textMultiline.setText("   НЕТ СОБЫТИЙ ЗА ЭТОТ  МЕСЯЦ!");
            }
//        }
//
//        //System.out.println("week_days[0]"+result);
//        //String data = String.valueOf((new MainActivity().textMultiline.getText()));
//
//        //System.out.println(data.length());
//        if (exists) {
//            if ( week_days.equals("")) {
//                Toast.makeText(this, "выберите дату на календаре", Toast.LENGTH_LONG).show();
//                System.out.println("кнопка не работает");
//
//            } else {
//                textView.setText("  за " + week + "ую неделю " +year +"г.:");
//                //считываем с файла всё что есть
//                StringBuilder sb = new StringBuilder();
//                try (FileInputStream fis = openFileInput("event_diary.txt");
//                     InputStreamReader isr = new InputStreamReader(fis);
//                     BufferedReader br = new BufferedReader(isr)) {
//                    String line;
//                    while ((line = br.readLine()) != null) {
//                        //есть ли в строке дата из массива дней недели
//                        boolean contains = Arrays.stream(week_days).anyMatch(line::contains);
//                        //boolean contains = line.contains(week_days);
//                        if (contains)  {
//                            String day = line.substring(0, 11);
//                            String event = line.substring(11);
//                            String str =  "<span style=\"background-color:#f3f402;\">" + day + "</span>" + event+ " <br>";
//                            sb.append(str);
//                        }
//
//
//                    }
//                    //
//                    //textMultiline.setText(sb.toString());
//                    textMultiline.setText(Html.fromHtml(String.valueOf(sb), Html.FROM_HTML_MODE_LEGACY));
//
//                    if (sb.length() == 0) {
//                        textMultiline.setText("   НЕТ СОБЫТИЙ ЗА ЭТУ НЕДЕЛЮ!");
//                    }
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//
//            }
        }else {
            Toast.makeText(this, "В Вашем календаре пока нет событий! Выберите дату, запишите событие  и внесите!", Toast.LENGTH_LONG).show();
            System.out.println("pass");
        }
    }


    private void openPdf () {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        String downloadDir = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));


        String sFile=downloadDir+"/itogi_results"+current_data+".pdf";

        //File path = new File(Environment.getExternalStorageDirectory() + "/" + "ParentDirectory" + "/" + "ChildDirectory");
        File path = new File(sFile);
        Uri uri = Uri.fromFile(path);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
    //меню три точки вверху справа
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu); // Replace 'menu_main' with your menu resource name
//        return super.onCreateOptionsMenu(menu);
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (R.id.action_settings == id) {
//            // Handle settings action
//            Intent intent = new Intent(ReviewOWeek.this, SettingsActivity.class);
//            startActivity(intent);
//            return true;
//        }
//        else if (R.id.action_about == id) {
//            // Handle about action
//            Intent intent = new Intent(ReviewOWeek.this, AboutActivity.class);
//            startActivity(intent);
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }



}