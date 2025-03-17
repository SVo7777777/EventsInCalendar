package com.example.calendarhours;

import static android.os.ParcelFileDescriptor.MODE_APPEND;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

public class SaveNewHours {
    @SuppressLint("SdCardPath")
    private static final String APP_SD_PATH = "/data/data/com.example.calendarhours/files/hours.txt";
    static StringBuilder inputStr;
    public static void fileChangeLine1(String line1, StringBuilder hours){
        try {
            // input the file content to the StringBuffer "input"
            BufferedReader file = new BufferedReader(new FileReader("hours.txt"));
            StringBuffer inputBuffer = new StringBuffer();
            String line;

            while ((line = file.readLine()) != null) {

                boolean contains = line.contains(line1);
                if (contains) {
                    inputBuffer.append(line1).append(" ").append(hours);
                    inputBuffer.append('\n');
                }else{
                    inputBuffer.append(line);
                    inputBuffer.append('\n');
                }
            }
            file.close();
            String inputStr = inputBuffer.toString();

            System.out.println("содержимое файла:\n"+inputStr); // display the original file for debugging

            // logic to replace lines in the string (could use regex here to be generic)

            // write the new string with the replaced line OVER the same file
            FileOutputStream fileOut = new FileOutputStream("hours.txt");
            fileOut.write(inputStr.getBytes());
            fileOut.close();

        } catch (Exception e) {
            System.out.println("Problem reading file.");
        }
    }
    public static void fileChangeLine(String line1, StringBuilder hours) {
        try {
            // Create a RandomAccessFile object in write mode
            RandomAccessFile file = new RandomAccessFile(APP_SD_PATH, "rw");
            String str;
            do {
                str = file.readLine();
                boolean contains = str.contains(line1);
                if (contains) {
                    System.out.println("мы тут4");
                    String data = line1+" "+hours;
                    file.write(data.getBytes());
                    System.out.println(data);

                }else {
                    file.write(str.getBytes());
                }


            } while (str != null);
            // Specify the position at which we want to write the data
            //file.seek(0); // writes at the beginning of the file


            // Write data to the file

            //Toast.makeText( "Введите часы!",this, Toast.LENGTH_LONG).show();


            // Close the file
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void fileChangeLine3(String line1, StringBuilder hours) {
        try {
            // Create a RandomAccessFile object in write mode
            RandomAccessFile file = new RandomAccessFile(APP_SD_PATH, "rw");
            String str;
            file.seek(0);
            file.writeUTF("\n"+line1+" "+hours);
            while (( file.readLine()) != null) {
                System.out.println("мы тут3");
                System.out.println("file.readLine()"+file.readLine());
                str = line1+" "+hours;
                boolean contains = file.readLine().contains(line1+" "+hours);
                if (contains) {
                    System.out.println("мы тут4");
                    file.write(str.getBytes());
                    System.out.println(line1+" "+hours);
                }
            }
            // Specify the position at which we want to write the data
            //file.seek(0); // writes at the beginning of the file


            // Write data to the file

            //Toast.makeText( "Введите часы!",this, Toast.LENGTH_LONG).show();


            // Close the file
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
