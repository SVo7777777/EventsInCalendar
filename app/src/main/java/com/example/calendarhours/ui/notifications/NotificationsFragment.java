package com.example.calendarhours.ui.notifications;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.calendarhours.DatabaseHelper;
import com.example.calendarhours.R;
import com.example.calendarhours.databinding.FragmentNotificationsBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private DatabaseHelper mydb ;
    LinearLayout number;
    LinearLayout data;
    LinearLayout hours;
    LinearLayout salary;
    private LinearLayout linear;
    TextView num;
    TextView dat;
    TextView hou;
    TextView sal;
    String[] split;
    Button button2;
    private ImageButton btn;
    private Bitmap bitmap;

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
        button2 = root.findViewById(R.id.button2);
        linear = root.findViewById(R.id.lineard);
        btn = root.findViewById(R.id.btnd);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("size", "" + linear.getWidth() + " " + linear.getWidth());
                bitmap = LoadBitmap(linear, linear.getWidth(), linear.getHeight());
                createPdf();
                //createPDF();
            }
        });

        mydb = new DatabaseHelper(getContext());
        ArrayList<ArrayList<String>>  str = mydb.getAllRows();
        System.out.println(str);
        System.out.println(str.get(0).get(0));
        System.out.println(str.get(0).get(1));
        int size = str.size();
        float summer = 0.0F;
        System.out.println(size);
        int j = 10;
        for (int i = 0; i < size; i++) {
            num=new TextView(getContext());
            dat=new TextView(getContext());
            hou=new TextView(getContext());
            sal=new TextView(getContext());
            String da = str.get(i).get(0);
            String sa = str.get(i).get(3);
            String h = str.get(i).get(2);
            summer += Float.parseFloat(sa);
            num.setText(String.valueOf(i+1));
            num.setGravity(Gravity.CENTER);
            dat.setText(da);
            dat.setGravity(Gravity.CENTER);
            hou.setText(h);
            hou.setGravity(Gravity.CENTER);
            hou.setId(i+1);
            sal.setText(sa);
            sal.setId(j+1);
            sal.setGravity(Gravity.CENTER);
            number.addView(num);
            data.addView(dat);
            hours.addView(hou);
            salary.addView(sal);
            j += 1;

        }
        System.out.println("summer="+summer);
        button2.setText(String.format("всего заработано: %s", summer));

//        for (ArrayList<String> list : str) {
//            System.out.println("размер массива: "+list.size());
//        }
//        Collections.sort(str);
//        System.out.println(Arrays.toString(str.toArray()));


        //final TextView textView = binding.textNotifications;
        //notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }
    private Bitmap LoadBitmap(View v, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }
    private void createPdf() {
//        WindowManager wm = (WindowManager) getSystemService(getActivity().WINDOW_SERVICE);
        //  Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels;
        float width = displaymetrics.widthPixels;

        int convertHighet = (int) hight, convertWidth = (int) width;

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);

        // write the document content /sdcard/
        @SuppressLint("SdCardPath")
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileName = "example.pdf";
       //File file = new File(downloadsDir, fileName);
        String targetPdf = "page.pdf";
        //File filePath;
        File filePath = new File(downloadsDir,targetPdf);

        try {
            document.writeTo(new FileOutputStream(filePath));
            // close the document
            document.close();
            Toast.makeText(getActivity(), "successfully pdf created", Toast.LENGTH_SHORT).show();
//        } catch (FileNotFoundException e) {
//            Log.d("mylog", "Error while writing " + e.toString());
//            throw new RuntimeException(e);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
            System.out.println(e.toString());

        }////////////////////


        // close the document
//        document.close();
//        Toast.makeText(getActivity(), "successfully pdf created", Toast.LENGTH_SHORT).show();

       //openPdf();

    }
    private void openPdf() {
        File file = new File("/sdcard/Documents/page.pdf");
        if (!file.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "No Application for pdf view", Toast.LENGTH_SHORT).show();
        }
    }
    public String summerHours(String h) {
        split = h.split("-");
        System.out.println(Arrays.toString(split));
        int d = 1;
        float sum = 0.0F;
        for (int i = 0; i < 42; i++) {
            if (split[d].equals(".")) {
                System.out.println("point");
            } else {
                sum += Float.parseFloat(split[d]);
            }
            d += 1;
        }
        System.out.println(sum);
        return String.valueOf(sum);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}