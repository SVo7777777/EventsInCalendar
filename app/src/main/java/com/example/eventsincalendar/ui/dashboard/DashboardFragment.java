package com.example.eventsincalendar.ui.dashboard;

import static android.content.Context.MODE_APPEND;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;

import com.example.eventsincalendar.CreatPDF;
import com.example.eventsincalendar.CustomDialogFragment;
import com.example.eventsincalendar.CustomDialogFragmentOkNo;
import com.example.eventsincalendar.DatabaseHelper;
import com.example.eventsincalendar.DatabaseHelperEv;
import com.example.eventsincalendar.MyWidget2;
import com.example.eventsincalendar.R;
import com.example.eventsincalendar.databinding.FragmentDashboardBinding;
import com.example.eventsincalendar.FileEmpty;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    CalendarView calendarView;

    public EditText textMultiline;
    public LinearLayout linear;
    SearchView simpleSearchView;
    ImageButton btnd;
    ImageButton btn_open;
    boolean addRecord;
    Calendar calendar = Calendar.getInstance();
    public int current_year = calendar.get(Calendar.YEAR);
    public int current_month = calendar.get(Calendar.MONTH);
    public int current_day = calendar.get(Calendar.DATE);

    String current_data = "_"+ current_day +"-"+ (current_month + 1) +"-"+ current_year;

    String[] data = {"ПРОССМОТРЕТЬ", "ПРОССМОТРЕТЬ ЗА ДЕНЬ", "ПРОССМОТРЕТЬ ЗА МЕСЯЦ", "ПРОССМОТРЕТЬ ЗА НЕДЕЛЮ"};
    //private ActivityMain2Binding binding;


    private DatabaseHelperEv mydb ;

    @SuppressLint({"SetTextI18n", "CutPasteId"})
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);


        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        simpleSearchView = root.findViewById(R.id.simpleSearchView);
        calendarView = root.findViewById(R.id.calendarView10);
        textMultiline = root.findViewById(R.id.editTextTextMultiLine8);
        btnd = root.findViewById(R.id.btnd);
        btn_open = root.findViewById(R.id.btn_open);
        linear = root.findViewById(R.id.lineard);


        btnd.setOnClickListener(v -> {
//            CustomDialogFragmentOkNo dialog0 = new CustomDialogFragmentOkNo();
//            Bundle args0 = new Bundle();
//            String attention0 = " Вы уверены, что хотите закачать итоги поиска?";
//            args0.putString("attention", attention0);
//            dialog0.setArguments(args0);
//            dialog0.show(getParentFragmentManager(), "custom");
//
//            Bundle bundle = this.getArguments();
//            if (bundle != null) {
//                Boolean download = bundle.getBoolean("down"); // Key, default value
//            }
//            if (download)

//            assert getArguments() != null;
//            boolean download = getArguments().getBoolean("down");
//            if (download){
//                System.out.println("down="+download);
//            }else {
//                System.out.println("down="+download);
//            }

            System.out.println("hear-btnd");
            Log.d("size", linear.getWidth() + " " + linear.getWidth());
            @SuppressLint({"NewApi", "LocalSuppress"})
            boolean wr = CreatPDF.creatPDF(linear,"_search" + current_data);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (wr){
                    String attention = "Итоги загружены в телефон в папку Download. В файл itogi_results_search"+ current_data + ".pdf";
                    CustomDialogFragment dialog = new CustomDialogFragment();
                    Bundle args = new Bundle();
                    args.putString("attention", attention);
                    dialog.setArguments(args);
                    dialog.show(getParentFragmentManager(), "custom");
                }else{
                    String attention = "Включите разрешение ПАМЯТЬ для этого приложения (Настройки-->Приложения-->Календарь рабочих часов-->Разрешение-->Память--> Разрешить)";
                    CustomDialogFragment dialog = new CustomDialogFragment();
                    Bundle args = new Bundle();
                    args.putString("attention", attention);
                    dialog.setArguments(args);
                    dialog.show(getParentFragmentManager(), "custom");
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
        Calendar ci = Calendar.getInstance();
        //вывод текущей даты в поле информации при запуске приложения
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy EEEE");
        System.out.println(format.format(ci.getTime()));
        String today = format.format(ci.getTime());
        // цвет даты
        //textMultiline.setText(Html.fromHtml("<font color=\"#006400\">" + today  + "</font>"));
        CharSequence hint = textMultiline.getHint();
        String s = "Сегодня " + today + ". " + hint;
        textMultiline.setHint(s);

//обновление виджета
        Intent intentq = new Intent(getActivity(), MyWidget2.class);
        intentq.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        int[] ids = AppWidgetManager.getInstance(getActivity().getApplication()).getAppWidgetIds(new ComponentName(getActivity().getApplication(), MyWidget2.class));
        intentq.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        getActivity().sendBroadcast(intentq);
        mydb = new DatabaseHelperEv(getContext());
        //mydb.AddnewTable("plan3");
        // поиск по слову Set SearchView query text listener
        simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                StringBuilder sb = new StringBuilder();
                ArrayList<ArrayList<String>> all_data = mydb.getAllRows();
                System.out.println(all_data);
                int size = all_data.size();
                System.out.println(size);
                System.out.println(all_data.get(0).get(0));
                System.out.println(all_data.get(0).get(1));
                int s = query.length();
                System.out.println(query);
                System.out.println(s);

                boolean iskl = false;
                try {
                    String query_1 = query.substring(0, s-1);
                    String query_2 = query.substring(0, s-2);

                    for (int i = 0; i < size; i++) {
                        System.out.println(all_data.get(i).get(1));
                        String mon = all_data.get(i).get(0);
                        String ev = all_data.get(i).get(1);
                        boolean contains_1 = ev.contains(query_1);
                        boolean contains_2 = ev.contains(query_2);
                        boolean contains = ev.contains(query);
                        if ((contains) || (contains_1) || (contains_2)) {
                            System.out.println(data + "в файле есть");
                            String str = "<span style=\"background-color:#f3f402;\">" + mon + ": " + "</span>" + ev + " <br>";
                            System.out.println(str);
                            sb.append(str);
                        }
                    }
                }catch (StringIndexOutOfBoundsException e){
                    System.out.println("pass");
                    iskl = true;
                }
                textMultiline.setText(Html.fromHtml(String.valueOf(sb), Html.FROM_HTML_MODE_LEGACY));
                if (sb.length() == 0) {
                    if (iskl)
                        textMultiline.setText("Введите слово, а не букву!");
                    else
                        textMultiline.setText("По слову '" + query + "' ничего не найдено.");
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        //public void setGravity (int gravity, int xOffset, int yOffset);
        addRecord = true;
        return root;
    }
    private void openPdf () {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        String downloadDir = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));


        String sFile=downloadDir+"/itogi_results_search"+current_data+".pdf";

        //File path = new File(Environment.getExternalStorageDirectory() + "/" + "ParentDirectory" + "/" + "ChildDirectory");
        File path = new File(sFile);
        Uri uri = Uri.fromFile(path);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}