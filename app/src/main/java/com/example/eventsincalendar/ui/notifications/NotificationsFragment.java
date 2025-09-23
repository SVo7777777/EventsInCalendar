package com.example.eventsincalendar.ui.notifications;

import static android.content.Context.MODE_APPEND;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.eventsincalendar.CustomDialogFragment;
import com.example.eventsincalendar.DatabaseHelper;
import com.example.eventsincalendar.DatabaseHelperEv;
import com.example.eventsincalendar.MyWidget2;
import com.example.eventsincalendar.R;
import com.example.eventsincalendar.databinding.FragmentNotificationsBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private DatabaseHelperEv mydb;
    CalendarView calendarView;
    public EditText textMultiline;
    public Button buttonAdd;
    public Button buttonReset;
    boolean addRecord;
    public String chosesData;
    LinearLayout number;
    LinearLayout data;
    LinearLayout hours;
    LinearLayout salary;
    LinearLayout price;
    private LinearLayout linear;
    TextView num;
    TextView dat;
    TextView hou;
    TextView sal;
    TextView rez_salary0;
    TextView pr;
    private int ch_d;
    private int ch_m;
    private int ch_y;
    private ImageButton btn;

    private int width, height;
    Calendar calendar = Calendar.getInstance();
    public int current_year = calendar.get(Calendar.YEAR);
    public int current_month = calendar.get(Calendar.MONTH);
    public int current_day = calendar.get(Calendar.DATE);
    String current_data = "_"+ current_day +"-"+ (current_month + 1) +"-"+ current_year;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        calendarView = root.findViewById(R.id.calendarView10);
        textMultiline = root.findViewById(R.id.editTextTextMultiLine8);
        buttonAdd = root.findViewById(R.id.buttonAdd);
        buttonReset = root.findViewById(R.id.buttonReset);
        clickAdd(buttonAdd);
        clickReset(buttonReset);

        //обновление виджета
        Intent intentq = new Intent(getActivity(), MyWidget2.class);
        intentq.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        int[] ids = AppWidgetManager.getInstance(getActivity().getApplication()).getAppWidgetIds(new ComponentName(getActivity().getApplication(), MyWidget2.class));
        intentq.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        getActivity().sendBroadcast(intentq);

        mydb = new DatabaseHelperEv(getContext());

        Calendar ci = Calendar.getInstance();
        //вывод текущей даты в поле информации при запуске приложения
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format0 = new SimpleDateFormat("EE dd-MM-yyyy");
        String today0 = format0.format(ci.getTime());
        System.out.println(today0);
        boolean search = mydb.checkDataExistOrNot(today0, DatabaseHelperEv.TABLE);
        String ev = mydb.getEvents(today0, DatabaseHelperEv.TABLE);
        if (search){
            textMultiline.setText(Html.fromHtml("<font color=\"#0000FF\">" + today0  + ": " +"</font>" + ev));

        }else {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy EEEE");
            System.out.println(format.format(ci.getTime()));
            String today = format.format(ci.getTime());
            // цвет даты
            //textMultiline.setText(Html.fromHtml("<font color=\"#006400\">" + today  + "</font>"));
            CharSequence hint = textMultiline.getHint();
            String s = "Сегодня " + today + ". " + hint;
            textMultiline.setHint(s);

            //курсор в конце строки
            textMultiline.requestFocus();
            textMultiline.setSelection(textMultiline.getText().length());
        }

        //вывод даты  в поле информации при нажатии на календаре
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                addRecord = true;
                ch_d = dayOfMonth;
                ch_m = month;
                ch_y = year;

                @SuppressLint("SimpleDateFormat")
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(ch_y, ch_m, ch_d);
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdf1 = new SimpleDateFormat("EE dd-MM-yyyy");
                Date date1 = calendar1.getTime();
                String data1 =  sdf1.format(date1);//дата в базе
                System.out.println(data1);
                boolean search = mydb.checkDataExistOrNot(data1, DatabaseHelperEv.TABLE);
                String ev = mydb.getEvents(data1, DatabaseHelperEv.TABLE);

                @SuppressLint("SimpleDateFormat")
                final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                String sDate = sdf.format(calendar.getTime());
                if (search){
                    textMultiline.setText(Html.fromHtml("<font color=\"#0000FF\">" + data1  + ": " +"</font>" + ev));

                }else {
                    chosesData = sDate;
                    //цвет даты 006400-зелёный
                    textMultiline.setText(Html.fromHtml("<font color=\"#0000FF\">" + sDate + ": " +"</font>"));
                    //фокус в конце даты
                    textMultiline.requestFocus();
                    textMultiline.setSelection(textMultiline.getText().length());
                }
            }
        });



//        btn.setOnClickListener(v -> {
//            Log.d("size", linear.getWidth() + " " + linear.getWidth());
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                creatPDF(true);
//            }
//
//        });
//        ImageButton btnop = root.findViewById(R.id.btn_open);
//        btnop.setOnClickListener(v -> {
//            Log.d("size", "размер" + linear.getWidth() + " " + linear.getWidth());
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                openPdf();
//            }
//
//        });
//
//
//        ArrayList<ArrayList<String>> str = mydb.getAllRows();
//        System.out.println(str);
//        System.out.println(str.get(0).get(0));
//        System.out.println(str.get(0).get(1));
//        int size = str.size();
//        float summer = 0.0F;
//        System.out.println(size);
//        int j = 10;
//        for (int i = 0; i < size; i++) {
//            num = new TextView(getContext());
//            dat = new TextView(getContext());
//            hou = new TextView(getContext());
//            sal = new TextView(getContext());
//            pr = new TextView(getContext());
//            String da = str.get(i).get(0);
//            String sa = str.get(i).get(3);
//            String h = str.get(i).get(2);
//            String p = str.get(i).get(4);
//            summer += Float.parseFloat(sa);
//            num.setText(String.valueOf(i + 1));
//            num.setGravity(Gravity.CENTER);
//            dat.setText(da);
//            dat.setGravity(Gravity.CENTER);
//            hou.setText(h);
//            hou.setGravity(Gravity.CENTER);
//            hou.setId(i + 1);
//            sal.setText(sa);
//            sal.setId(j + 1);
//            sal.setGravity(Gravity.CENTER);
//            pr.setGravity(Gravity.CENTER);
//            pr.setText(p);
//            pr.setId(j+1);
////            number.addView(num);
////            data.addView(dat);
////            hours.addView(hou);
////            salary.addView(sal);
////            price.addView(pr);
//            j += 1;
//
//        }
//        System.out.println("summer=" + summer);
//        //rez_salary0.setText(String.format("всего заработано: %s", summer));

        return root;
    }
    //Например, MODE_PRIVATE — файл доступен только этому приложению, MODE_WORLD_READABLE — файл доступен для чтения всем, MODE_WORLD_WRITEABLE — файл доступен для записи всем, MODE_APPEND — файл будет дописан, а не начат заново.
    //добавяем запись в файл "event_diary.txt"

    private void clickAdd(Button button2) {
        button2.setOnClickListener(v -> {
            String data = String.valueOf(textMultiline.getText());
            System.out.println(data);
            String day_ = data.substring(0, 11);
            String day = data.substring(0, 10);
            String event = data.substring(12);
//            int d = Integer.parseInt(data.substring(0, 2));
//            int m = Integer.parseInt(data.substring(3, 5));
//            int y = Integer.parseInt(data.substring(6, 10));


//            int dayOfWeek = cldr.get(Calendar.DAY_OF_WEEK);
//            String[] daysOfWeek = context.getResources().getStringArray(R.array.days_of_week_short);
//            String[] month = context.getResources().getStringArray(R.array.months);
            @SuppressLint("SimpleDateFormat")
            Calendar calendar = Calendar.getInstance();
            calendar.set(ch_y, ch_m, ch_d);

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf1 = new SimpleDateFormat("EE dd-MM-yyyy");
            Date date1 = calendar.getTime();
            String data1 =  sdf1.format(date1);//дата в базе
            System.out.println(data1);


            if (!addRecord) {
                Toast.makeText(getContext(), "Выберите дату, запишите событие, а потом внесите! ", Toast.LENGTH_LONG).show();
            } else {

                if  (data.length() > 13){
                    //mydb = new DatabaseHelperEv(getContext());
                    boolean search = mydb.checkDataExistOrNot(day, DatabaseHelperEv.TABLE);
                    System.out.println("search: "+search);
                    if (search) {
                        Toast.makeText(getActivity(), day + " уже есть!", Toast.LENGTH_SHORT).show();
                        System.out.println(data + " уже есть!");
                        String ev = mydb.getEvents(data1, DatabaseHelperEv.TABLE);

                        int id = mydb.GetId(data1, DatabaseHelperEv.TABLE);
                        boolean update_events = mydb.updateEvents(id, data1, String.valueOf(data), "hours");
                        if (update_events){
                            System.out.println("событие изменено");
                        }
                        //String s = mydb.getSum(data);
                        System.out.println("за "+day+" событие: "+ev);
                        //System.out.println("s="+sum);
                        //addHoursInCalendar(h);
                    }else {
                        System.out.println(day);
                        System.out.println(event);
                        mydb.insertContact(data1, event, null,null,null,null, DatabaseHelperEv.TABLE);
                        //mydb.insertContact(data, hours.toString(), "0.0","0.0","0.0","plan1");
                        //mydb.insertContact(data, hours.toString(), "0.0","0.0","0.0","plan2");
                        //Toast.makeText(getActivity(), "За " + day + "\n добавлено событие:\n" + event, Toast.LENGTH_SHORT).show();
                        String attention = "За " + day + "\nдобавлено событие:\n" + event;
                        CustomDialogFragment dialog = new CustomDialogFragment();
                        Bundle args = new Bundle();
                        args.putString("attention", attention);
                        dialog.setArguments(args);
                        dialog.show(getParentFragmentManager(), "custom");
                    }
                    addRecord = false;
                } else {
                    Toast.makeText(getContext(), "Запишите событие, а потом внесите! ", Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    private void clickReset(Button button2) {
        button2.setOnClickListener(v -> {
            addRecord = false;
            Calendar ci = Calendar.getInstance();
            textMultiline.setText("");
            //simpleSearchView.setQuery("", false);
            //searchView.setQuery("", false);
            //simpleSearchView.setIconified(true);
            //simpleSearchView.setQueryHint("Поиск по слову. Введите слово.");

//        String CiDateTime = ci.get(Calendar.DAY_OF_MONTH) + "-" + (ci.get(Calendar.MONTH) + 1) + "-" + ci.get(Calendar.YEAR) + ": ";
//        textMultiline.setText(CiDateTime);
            //курсор в конце строки
            textMultiline.requestFocus();
            textMultiline.setSelection(textMultiline.getText().length());

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public  void creatPDF(boolean hasFocus) {
        super.onHiddenChanged(hasFocus);
        width = linear.getWidth();
        height = linear.getHeight();
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c1 = new Canvas(b);
        linear.draw(c1);

        PdfDocument pd = new PdfDocument();

        PdfDocument.PageInfo pi = new PdfDocument.PageInfo.Builder(width, height, 1).create();
        PdfDocument.Page p = pd.startPage(pi);
        Canvas c = p.getCanvas();
        c.drawBitmap(b, 0, 0, new Paint());
        pd.finishPage(p);
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        //сохраняем итоги в папке  DOWNLOADS на телефоне
        try (FileOutputStream fos = new FileOutputStream(downloadsDir + "/itogi_results" + current_data + ".pdf");
             OutputStreamWriter osw = new OutputStreamWriter(fos)) {
            //String data = String.valueOf(textMultiline.getText());
            pd.writeTo(fos);
            Log.d("PDF", "PDF saved to external storage");
            String attention = "Итоги загружены в телефон в папку Download. В файл itogi_results"+ current_data + ".pdf";
            CustomDialogFragment dialog = new CustomDialogFragment();
            Bundle args = new Bundle();
            args.putString("attention", attention);
            dialog.setArguments(args);
            dialog.show(getParentFragmentManager(), "custom");


        } catch (IOException e) {
            //Toast.makeText(getActivity(), "Something wrong: включите разрешение ПАМЯТЬ для этого приложения (Настройки-->Приложения-->Календарь часов-->Разрешение-->Память--> Разрешить)" + e.toString(), Toast.LENGTH_LONG).show();
            System.out.println(e.toString());
            //вывод диалогового окна, что запись внесена
            String attention = "Включите разрешение ПАМЯТЬ для этого приложения (Настройки-->Приложения-->Календарь рабочих часов-->Разрешение-->Память--> Разрешить)";
            CustomDialogFragment dialog = new CustomDialogFragment();
            Bundle args = new Bundle();
            args.putString("attention", attention);
            dialog.setArguments(args);
            dialog.show(getParentFragmentManager(), "custom");
            //throw new RuntimeException(e);
        }

        pd.close();
        String downloadDir = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
        String pdf_file = "a-computer-engineer-pdf-test.pdf";
        System.out.println("Environment.getExternalStorageDirectory().toString()=" + Environment.getExternalStorageDirectory().toString());
        System.out.println(downloadDir);
        System.out.println("Environment.getExternalStorageDirectory().getAbsolutePath()=" + Environment.getExternalStorageDirectory().getAbsolutePath());
        @SuppressLint("SdCardPath")
        //final String APP_SD_PATH = "/storage/emulated/0/data/data/com.example.calendarofevents";
        String path = getActivity().getApplicationInfo().dataDir;

        System.out.println("=path=" + path);
        //openPdf();
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
    //@SuppressLint("ObsoleteSdkInt")


        @Override
        public void onDestroyView () {
            super.onDestroyView();
            binding = null;
    }
}
