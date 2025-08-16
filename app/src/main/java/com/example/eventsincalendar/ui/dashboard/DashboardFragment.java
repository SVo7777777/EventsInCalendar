package com.example.eventsincalendar.ui.dashboard;

import static android.content.Context.MODE_APPEND;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;

import com.example.eventsincalendar.CustomDialogFragment;
import com.example.eventsincalendar.DatabaseHelper;
import com.example.eventsincalendar.MyWidget2;
import com.example.eventsincalendar.R;
import com.example.eventsincalendar.databinding.FragmentDashboardBinding;
import com.example.eventsincalendar.FileEmpty;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    CalendarView calendarView;
    String curDate;
    public EditText textMultiline;
    EditText editTextInput;
    SearchView simpleSearchView;
    private Object MotionEvent;
    boolean addRecord;
    public String chosesData;
    String[] data = {"ПРОССМОТРЕТЬ", "ПРОССМОТРЕТЬ ЗА ДЕНЬ", "ПРОССМОТРЕТЬ ЗА МЕСЯЦ", "ПРОССМОТРЕТЬ ЗА НЕДЕЛЮ"};
    //private ActivityMain2Binding binding;
    Calendar calendar = Calendar.getInstance();
    public int current_year = calendar.get(Calendar.YEAR);
    public int current_month = calendar.get(Calendar.MONTH);
    public int current_day = calendar.get(Calendar.DATE);

    private DatabaseHelper mydb ;




    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        simpleSearchView = root.findViewById(R.id.simpleSearchView);
        calendarView = root.findViewById(R.id.calendarView10);
        textMultiline = root.findViewById(R.id.editTextTextMultiLine8);

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

        //курсор в конце строки
//        textMultiline.requestFocus();
//        textMultiline.setSelection(textMultiline.getText().length());
//обновление виджета
        Intent intentq = new Intent(getActivity(), MyWidget2.class);
        intentq.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        int[] ids = AppWidgetManager.getInstance(getActivity().getApplication()).getAppWidgetIds(new ComponentName(getActivity().getApplication(), MyWidget2.class));
        intentq.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        getActivity().sendBroadcast(intentq);


        mydb = new DatabaseHelper(getContext());
        //mydb.AddnewTable("plan3");
        // поиск по слову Set SearchView query text listener
        simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                StringBuilder sb = new StringBuilder();
                boolean exists = FileEmpty.fileExistsInSD("event_diary.txt");
                if (exists) {
                    try (FileInputStream fis = requireContext().openFileInput("event_diary.txt");
                         InputStreamReader isr = new InputStreamReader(fis);
                         BufferedReader br = new BufferedReader(isr)) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            boolean contains = line.contains(query);
                            if (contains) {
                                sb.append(line + "\n");
                            }
                        }
                        textMultiline.setText(sb.toString());
                        if (sb.length() == 0) {
                            textMultiline.setText("По слову '" + query + "' ничего не найдено. Попробуйте ввести первые несколько букв слова.");
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    textMultiline.setText("В Вашем календаре пока нет событий! Выберите дату, запишите событие  и внесите!");
                }
//                if (myList.contains(query)) {
//                    adapter.getFilter().filter(query);
//                }
//                else {
//                    Toast.makeText(MainActivity.this, "No Match found", Toast.LENGTH_LONG).show();
//                }
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

    //Например, MODE_PRIVATE — файл доступен только этому приложению, MODE_WORLD_READABLE — файл доступен для чтения всем, MODE_WORLD_WRITEABLE — файл доступен для записи всем, MODE_APPEND — файл будет дописан, а не начат заново.
    //добавяем запись в файл "event_diary.txt"
    public AlertDialog clickAdd(View view) throws FileNotFoundException {
        String data = String.valueOf(textMultiline.getText());
        System.out.println(data);
        if (!addRecord) {
            Toast.makeText(getContext(), "Выберите дату, запишите событие, а потом внесите! ", Toast.LENGTH_LONG).show();
        } else {
            if  (data.length() >= 20){
                try (FileOutputStream fos = requireContext().openFileOutput("event_diary.txt", MODE_APPEND);
                     OutputStreamWriter osw = new OutputStreamWriter(fos)) {
                    //String data = String.valueOf(textMultiline.getText());
                    osw.write(data+"\n");
                    //вывод диалогового окна, что запись внесена
                    String attention = "запись внесена";
                    CustomDialogFragment dialog = new CustomDialogFragment();
                    Bundle args = new Bundle();
                    args.putString("attention", attention);
                    dialog.setArguments(args);
                    dialog.show(getParentFragmentManager(), "custom");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                addRecord = false;
            } else {
                Toast.makeText(getContext(), "Запишите событие, а потом внесите! ", Toast.LENGTH_LONG).show();
            }
        }
        return null;
    }
    //проссмотр по дате
    @SuppressLint("SetTextI18n")
    public void clickReviewData(String data, String data2) {
        addRecord = false;


        //считываем с файла всё что есть
        StringBuilder sb = new StringBuilder();
        try (FileInputStream fis = new FileInputStream("event_diary.txt");
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {
            String line;

            while ((line = br.readLine()) != null) {
                boolean contains = line.contains(data);
                boolean contains2 = line.contains(data2);
                if ((contains) || (contains2)) {
                    String day = line.substring(0, 11);
                    String event = line.substring(11);
                    String str =  "<font color=\"#0000FF\">"  + day + "</font>" + event+ " <br>";
                    sb.append(str);
                }



            }
            textMultiline.setText(Html.fromHtml(String.valueOf(sb), Html.FROM_HTML_MODE_LEGACY));
            if (sb.length() == 0) {
                //textMultiline.setText(data + " нет событий за этот день!");
                //дата синяя
                String str = "<font color=\"#0000FF\">" + data + "</font>" + " нет событий за этот день!";
                textMultiline.setText(Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY));

                //textMultiline.setText(Html.fromHtml("<font color=\"#0000FF\">" + data  + "</font>"+ " нет событий за этот день!"));


            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //кнопка СБРОС-удаление всего из поля информации и запись текущей даты

    public void clickReset(View view) {
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
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}