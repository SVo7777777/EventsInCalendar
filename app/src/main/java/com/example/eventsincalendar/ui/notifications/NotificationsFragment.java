package com.example.eventsincalendar.ui.notifications;

import android.annotation.SuppressLint;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.eventsincalendar.CustomDialogFragment;
import com.example.eventsincalendar.DatabaseHelper;
import com.example.eventsincalendar.R;
import com.example.eventsincalendar.databinding.FragmentNotificationsBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;


public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private DatabaseHelper mydb;
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

        number = root.findViewById(R.id.number);
        data = root.findViewById(R.id.data);
        hours = root.findViewById(R.id.hours);
        salary = root.findViewById(R.id.salary);
        price = root.findViewById(R.id.price);
        //button2 = root.findViewById(R.id.button2);
        linear = root.findViewById(R.id.lineard);
        btn = root.findViewById(R.id.btnd);
        rez_salary0 = root.findViewById(R.id.rez_salary);


        btn.setOnClickListener(v -> {
            Log.d("size", linear.getWidth() + " " + linear.getWidth());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                creatPDF(true);
            }

        });
        ImageButton btnop = root.findViewById(R.id.btn_open);
        btnop.setOnClickListener(v -> {
            Log.d("size", "размер" + linear.getWidth() + " " + linear.getWidth());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                openPdf();
            }

        });

        mydb = new DatabaseHelper(getContext());
        ArrayList<ArrayList<String>> str = mydb.getAllRows();
        System.out.println(str);
        System.out.println(str.get(0).get(0));
        System.out.println(str.get(0).get(1));
        int size = str.size();
        float summer = 0.0F;
        System.out.println(size);
        int j = 10;
        for (int i = 0; i < size; i++) {
            num = new TextView(getContext());
            dat = new TextView(getContext());
            hou = new TextView(getContext());
            sal = new TextView(getContext());
            pr = new TextView(getContext());
            String da = str.get(i).get(0);
            String sa = str.get(i).get(3);
            String h = str.get(i).get(2);
            String p = str.get(i).get(4);
            summer += Float.parseFloat(sa);
            num.setText(String.valueOf(i + 1));
            num.setGravity(Gravity.CENTER);
            dat.setText(da);
            dat.setGravity(Gravity.CENTER);
            hou.setText(h);
            hou.setGravity(Gravity.CENTER);
            hou.setId(i + 1);
            sal.setText(sa);
            sal.setId(j + 1);
            sal.setGravity(Gravity.CENTER);
            pr.setGravity(Gravity.CENTER);
            pr.setText(p);
            pr.setId(j+1);
            number.addView(num);
            data.addView(dat);
            hours.addView(hou);
            salary.addView(sal);
            price.addView(pr);
            j += 1;

        }
        System.out.println("summer=" + summer);
        rez_salary0.setText(String.format("всего заработано: %s", summer));

        return root;
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
