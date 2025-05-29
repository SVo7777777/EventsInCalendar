package com.example.calendarhours.ui.notifications;

import static android.os.ParcelFileDescriptor.MODE_APPEND;
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
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;


public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private DatabaseHelper mydb;
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
    private ImageView imageView;
    private int width, height;

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
                //bitmap = loadBitmapFromView(linear, linear.getWidth(), linear.getHeight());
                //createPdf();
                //convertXmlToPdf();
                onWindowFocusChanged(true);
                //createPdfFile();
                //createPDF();
            }
        });
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("size"," "+llPdf.getWidth() +"  "+llPdf.getWidth());
//                bitmap = loadBitmapFromView(llPdf, llPdf.getWidth(), llPdf.getHeight());
//                createPdfFile();
//            }
//        });

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
            String da = str.get(i).get(0);
            String sa = str.get(i).get(3);
            String h = str.get(i).get(2);
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
            number.addView(num);
            data.addView(dat);
            hours.addView(hou);
            salary.addView(sal);
            j += 1;

        }
        System.out.println("summer=" + summer);
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
    public  void onWindowFocusChanged(boolean hasFocus) {
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
        try (FileOutputStream fos = getActivity().openFileOutput("a-computer-engineer-pdf-test.pdf", Context.MODE_PRIVATE);
             //try (FileOutputStream fos = new FileOutputStream("exampleXML.pdf", true);
             // outputStream = new FileOutputStream (new File(patternDirectory.getAbsolutePath().toString()), true); // true will be same as Context.MODE_APPEND

             OutputStreamWriter osw = new OutputStreamWriter(fos)) {
            //String data = String.valueOf(textMultiline.getText());
            pd.writeTo(fos);
            Log.d("PDF", "PDF saved to external storage");
            Toast.makeText(getActivity(), "Written Successfully!!!", Toast.LENGTH_SHORT).show();            //вывод диалогового окна, что запись внесена

        } catch (IOException e) {
            Toast.makeText(getActivity(), "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
            System.out.println(e.toString());
            //throw new RuntimeException(e);
        }

        pd.close();

        openPdf1();
    }
    private void openPdf () {
        //File file = new File("/sdcard/Documents/page.pdf");
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "a-computer-engineer-pdf-test.pdf");

        //FileOutputStream file = getActivity().openFileInput("exampleXML.pdf", Context.MODE_PRIVATE);
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

    protected void openPdf1()
    {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        String path =  Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
//        File file = new File(path, "a-computer-engineer-pdf-test.pdf");
//        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
//        startActivity(intent);

        String storage = Environment.getExternalStorageDirectory().toString() + "/a-computer-engineer-pdf-test.txt";
        File file = new File(storage);
        Uri uri;
        if (Build.VERSION.SDK_INT < 24) {
            uri = Uri.fromFile(file);
        } else {
            uri = Uri.parse(file.getPath()); // My work-around for SDKs up to 29.
        }
        Intent viewFile = new Intent(Intent.ACTION_VIEW);
        viewFile.setDataAndType(uri, "text/plain");
        startActivity(viewFile);
    }


    private void openGeneratedPDFFile(){
        File file = new File("/sdcard/pdffromlayout.pdf");
        if (file.exists())
        {
            Intent i=new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            i.setDataAndType(uri, "application/pdf");
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try
            {
                startActivity(i);
            }
            catch(ActivityNotFoundException e)
            {
                Toast.makeText(getActivity(), "No pdf view available", Toast.LENGTH_LONG).show();
            }
        }
    }



    public void convertXmlToPdf() {
        // Inflate the XML layout file
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_dashboard, null);
        //View view = LinearLayout
        DisplayMetrics displayMetrics = new DisplayMetrics();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getActivity().getDisplay().getRealMetrics(displayMetrics);
        } else
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        view.measure(View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, View.MeasureSpec.EXACTLY));
        Log.d("mylog", "Width Now " + view.getMeasuredWidth());
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        // Create a new PdfDocument instance
        PdfDocument document = new PdfDocument();

        // Obtain the width and height of the view
        int viewWidth = view.getMeasuredWidth();
        int viewHeight = view.getMeasuredHeight();
        // Create a PageInfo object specifying the page attributes
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(viewWidth, viewHeight, 1).create();

        // Start a new page
        PdfDocument.Page page = document.startPage(pageInfo);

        // Get the Canvas object to draw on the page
        Canvas canvas = page.getCanvas();

        // Create a Paint object for styling the view
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);

        // Draw the view on the canvas
        view.draw(canvas);

        // Finish the page
        document.finishPage(page);

        // Specify the path and filename of the output PDF file
        //File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//        String fileName = "exampleXML.pdf";
//        File filePath = new File(fileName);
//
//        if(filePath.exists()){
//            Toast.makeText(getApplicationContext(), "FileFound",
//                    Toast.LENGTH_LONG).show();
//            System.out.println("FileFound");
//        } else {
//
//            Toast.makeText(getApplicationContext(), "No file found",
//                    Toast.LENGTH_LONG).show();
//            System.out.println("File not Found");
//        }

//        try {
//            // Save the document to a file
//            FileOutputStream fos = new FileOutputStream(filePath);
//            document.writeTo(fos);
//            document.close();
//            fos.close();
//            // PDF conversion successful
//            Toast.makeText(this, "XML to PDF Conversion Successful", Toast.LENGTH_LONG).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
//            System.out.println(e.toString());
//            // Error occurred while converting to PDF
//        }
        try (FileOutputStream fos = getActivity().openFileOutput("exampleXML.pdf", Context.MODE_PRIVATE);
             //try (FileOutputStream fos = new FileOutputStream("exampleXML.pdf", true);
             // outputStream = new FileOutputStream (new File(patternDirectory.getAbsolutePath().toString()), true); // true will be same as Context.MODE_APPEND

             OutputStreamWriter osw = new OutputStreamWriter(fos)) {
            //String data = String.valueOf(textMultiline.getText());
            document.writeTo(fos);
            Log.d("PDF", "PDF saved to external storage");
            Toast.makeText(getActivity(), "Written Successfully!!!", Toast.LENGTH_SHORT).show();            //вывод диалогового окна, что запись внесена

        } catch (IOException e) {
            Toast.makeText(getActivity(), "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
            System.out.println(e.toString());
            //throw new RuntimeException(e);
        }
        document.close();
    }





        public String summerHours (String h){
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
        public void onDestroyView () {
            super.onDestroyView();
            binding = null;
        }
    }
