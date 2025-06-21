package com.example.calendarhours.ui.notifications;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.app.AlertDialog;

import com.example.calendarhours.CustomDialogFragment;
import com.example.calendarhours.DatabaseHelper;
import com.example.calendarhours.R;
import com.example.calendarhours.databinding.FragmentNotificationsBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    TextView pr;
    String[] split;
    Button button2;
    private ImageButton btn;
    private Bitmap bitmap;
    private ImageView imageView;
    private int width, height;
    Calendar calendar = Calendar.getInstance();
    public int current_year = calendar.get(Calendar.YEAR);
    public int current_month = calendar.get(Calendar.MONTH);
    public int current_day = calendar.get(Calendar.DATE);
    String current_data = "_"+String.valueOf(current_day)+"-"+String.valueOf(current_month+1)+"-"+String.valueOf(current_year);

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
        button2 = root.findViewById(R.id.button2);
        linear = root.findViewById(R.id.lineard);
        btn = root.findViewById(R.id.btnd);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("size", "" + linear.getWidth() + " " + linear.getWidth());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    creatPDF(true);
                }

            }
        });
        ImageButton btnop = root.findViewById(R.id.btn_open);
        btnop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("size", "размер" + linear.getWidth() + " " + linear.getWidth());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    openPdf();
                }

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
        button2.setText(String.format("всего заработано: %s", summer));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            creatPDF(true);
//        }
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
            String attention = "Итоги загружены в телефон в папку Загрузки.)";
            CustomDialogFragment dialog = new CustomDialogFragment();
            Bundle args = new Bundle();
            args.putString("attention", attention);
            dialog.setArguments(args);
            dialog.show(getParentFragmentManager(), "custom");
            Toast.makeText(getActivity(), "Итоги успешно загружены в телефон в папку Загрузки!!!", Toast.LENGTH_LONG).show();            //вывод диалогового окна, что запись внесена

        } catch (IOException e) {
            //Toast.makeText(getActivity(), "Something wrong: включите разрешение ПАМЯТЬ для этого приложения (Настройки-->Приложения-->Календарь часов-->Разрешение-->Память--> Разрешить)" + e.toString(), Toast.LENGTH_LONG).show();
            System.out.println(e.toString());
            //вывод диалогового окна, что запись внесена
//            CustomDialogFragment dialog2 = new CustomDialogFragment();
//            dialog2.show(getParentFragmentManager(), "custom");
            String attention = "Включите разрешение ПАМЯТЬ для этого приложения (Настройки-->Приложения-->Календарь часов-->Разрешение-->Память--> Разрешить)";
            CustomDialogFragment dialog = new CustomDialogFragment();
            Bundle args = new Bundle();
            args.putString("attention", attention);
            dialog.setArguments(args);
            dialog.show(getParentFragmentManager(), "custom");
            //throw new RuntimeException(e);
        }

        pd.close();
        String downloadDir = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
        String storage = Environment.getExternalStorageDirectory().toString() + "/Documents/itodi_results.pdf";
        String pdf_file = "a-computer-engineer-pdf-test.pdf";
        System.out.println("Environment.getExternalStorageDirectory().toString()=" + Environment.getExternalStorageDirectory().toString());
        System.out.println(downloadDir);
        System.out.println("Environment.getExternalStorageDirectory().getAbsolutePath()=" + Environment.getExternalStorageDirectory().getAbsolutePath());
        @SuppressLint("SdCardPath")
        //final String APP_SD_PATH = "/storage/emulated/0/data/data/com.example.calendarofevents";
        String path = getActivity().getApplicationInfo().dataDir;
        String sFolder = path + "/files";
        String sFile = sFolder + "/" + pdf_file;
        //boolean copy = copyFile(sFile, downloadDir);

        System.out.println("=path=" + path);
        //openPdf();
    }
    private void openPdf () {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        String path1 = getActivity().getApplicationInfo().dataDir;
        String downloadDir = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));

        String sFolder =  path1 + "/files";
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
