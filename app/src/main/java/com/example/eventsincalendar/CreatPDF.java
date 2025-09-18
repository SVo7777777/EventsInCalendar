package com.example.eventsincalendar;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class CreatPDF {
    static boolean wr;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static boolean creatPDF(LinearLayout linear, String current_data) {
        //super.onCreate();
        int width = linear.getWidth();
        int height = linear.getHeight();
        System.out.println("hear!!!!!!");
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
            wr = true;




        } catch (IOException e) {
            //Toast.makeText(getActivity(), "Something wrong: включите разрешение ПАМЯТЬ для этого приложения (Настройки-->Приложения-->Календарь часов-->Разрешение-->Память--> Разрешить)" + e.toString(), Toast.LENGTH_LONG).show();
            System.out.println(e.toString());
            wr = false;
            //вывод диалогового окна, что запись внесена

            //throw new RuntimeException(e);
        }

        pd.close();

        String downloadDir = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
        String pdf_file = "a-computer-engineer-pdf-test.pdf";
        System.out.println("Environment.getExternalStorageDirectory().toString()=" + Environment.getExternalStorageDirectory().toString());
        System.out.println(downloadDir);
        System.out.println("Environment.getExternalStorageDirectory().getAbsolutePath()=" + Environment.getExternalStorageDirectory().getAbsolutePath());
        //@SuppressLint("SdCardPath")
        //final String APP_SD_PATH = "/storage/emulated/0/data/data/com.example.calendarofevents";
        //String path = getActivity().getApplicationInfo().dataDir;

        //System.out.println("=path=" + path);
        //openPdf();
        return wr;
    }
}
