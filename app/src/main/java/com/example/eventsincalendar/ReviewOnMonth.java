package com.example.eventsincalendar;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

public class ReviewOnMonth extends AppCompatActivity {

    boolean addRecord;
    TextView textMultiline;
    TextView textView;
    ImageButton btnd;
    ImageButton btn_open;

    LinearLayout linear;
    Calendar calendar = Calendar.getInstance();
    public int current_year = calendar.get(Calendar.YEAR);
    public int current_month = calendar.get(Calendar.MONTH);
    public int current_day = calendar.get(Calendar.DATE);
    String current_data = "_"+ current_day +"-"+ (current_month + 1) +"-"+ current_year;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    String[] name_month = {"month", "ЯНВАРЬ", "ФЕВРАЛЬ", "МАРТ", "АПРЕЛЬ", "МАЙ", "ИЮНЬ", "ИЮЛЬ","АВГУСТ", "СЕНТЯБРЬ", "ОКТЯБРЬ", "НОЯБРЬ", "ДЕКАБРЬ"};
    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_review_ondata);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.review), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
//        //верхняя полоса с названием и 3мя точками
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar); // Use the toolbar as the app bar
//        //цвет для 3ёх точек и для названия
//        Objects.requireNonNull(toolbar.getOverflowIcon()).setColorFilter(Color.WHITE , PorterDuff.Mode.SRC_ATOP);

        textMultiline = findViewById(R.id.editTextTextMultiLine2);
        textView = findViewById(R.id.textView);
        btnd = findViewById(R.id.btnd);
        btn_open = findViewById(R.id.btn_open);
        linear = findViewById(R.id.lineard);

        btnd.setOnClickListener(v -> {
            System.out.println("hear-btnd");
            Log.d("size", linear.getWidth() + " " + linear.getWidth());
            boolean wr = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                wr = CreatPDF.creatPDF(linear, current_data);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (wr){
                    String attention = "Итоги загружены в телефон в папку Download. В файл itogi_results"+ current_data + ".pdf";
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
        //проссмотр за месяц
        addRecord = false;
        Intent intent = getIntent();
        String   data = intent.getStringExtra("data");;

        boolean exists = FileEmpty.fileExistsInSD("event_diary.txt");

        System.out.println(data.length());
        if (exists) {
            if ((data.length() >= 10) && (data.length() <= 12)) {
                int index_first = data.indexOf("-");
                int index_second = data.indexOf("-", index_first + 1);
                String month = data.substring(index_first, index_second + 5);//-1-2025
                String year = data.substring(index_second + 1, index_second + 5);//-1-2025
                String number_month = data.substring(index_first+1, index_second);
                //поиск месяца по виду -01-2025
                StringBuilder month2 = new StringBuilder(month);
                month2.insert(1, '0');
                System.out.println(month2);
                int number = Integer.parseInt(number_month);
                System.out.println("номер"+number_month+"месяца");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    System.out.println(name_month[number]);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    textView.setText("   за " +name_month[number]+" "+year + "г.:");
                }
                //считываем с файла всё что есть
                StringBuilder sb = new StringBuilder();
                try (FileInputStream fis = openFileInput("event_diary.txt");
                     InputStreamReader isr = new InputStreamReader(fis);
                     BufferedReader br = new BufferedReader(isr)) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        System.out.println(data + "мы тут");
                        System.out.println(line);
                        System.out.println(data);
                        boolean contains = line.contains(month);
                        boolean contains2 = line.contains(month2);
                        if ((contains) || (contains2)) {
                            System.out.println(data + "в файле есть");
                            String day = line.substring(0, 11);
                            String event = line.substring(11);
                            System.out.println("day="+day);
                            System.out.println("event="+event);
                            String str =  "<span style=\"background-color:#f3f402;\">" + day + "</span>" + event+ " <br>";
                            System.out.println(str);
                            sb.append(str);

                            //textMultiline.setText(Html.fromHtml(textMultiline.getText() + str, Html.FROM_HTML_MODE_LEGACY));

                        }


                    }
                    //textMultiline.setText(sb.toString());
                    textMultiline.setText(Html.fromHtml(String.valueOf(sb), Html.FROM_HTML_MODE_LEGACY));

                    //;String str = "<span style=\"background-color:#f3f402;\">" + sb + "</span>" + " нет событий за этот день!";
                    //textMultiline.setText(Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY));

                    //textMultiline.setText(Html.fromHtml("<font background_color=\"#0000FF\">" + sb.toString()  + "</font>"+ " нет событий за этот день!"));

                    if (sb.length() == 0) {
                        textMultiline.setText( "   НЕТ СОБЫТИЙ ЗА ЭТОТ  МЕСЯЦ!");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Toast.makeText(this, "Выберите дату на календаре!", Toast.LENGTH_LONG).show();
                System.out.println("кнопка не работает");
            }
        }else {
            Toast.makeText(this, "В Вашем календаре пока нет событий! Выберите дату, запишите событие  и внесите!", Toast.LENGTH_LONG).show();

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
//            Intent intent = new Intent(ReviewOnMonth.this, SettingsActivity.class);
//            startActivity(intent);
//            return true;
//        }
//        else if (R.id.action_about == id) {
//            // Handle about action
//            Intent intent = new Intent(ReviewOnMonth.this, AboutActivity.class);
//            startActivity(intent);
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


}