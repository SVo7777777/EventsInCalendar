package com.example.calendarhours;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "myhours.db"; // название бд
    private static final int SCHEMA = 1; // версия базы данных
    public static final String TABLE = "hours"; // название таблицы в бд
    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MONTH_YEAR = "month_year";
    public static final String COLUMN_HOURS = "hour";

    private HashMap hp;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

//        db.execSQL("CREATE TABLE hours (" + COLUMN_ID
//                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_MONTH_YEAR
//                + " TEXT, "+COLUMN_HOURS+" TEXT);");
        db.execSQL(
                "create table hours " +
                        "(id integer primary key, month_year text,hour text)");



    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
    public void insertContact (String month_year,  String hours) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("month_year", month_year);
        contentValues.put("hour", hours);
        db.insert("hours", null, contentValues);
    }
    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from hours where id="+id+"", null );
        return res;
    }
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE);
        return numRows;
    }
    public boolean updateContact (Integer id, String month_year, String hours) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("month_year", month_year);
        contentValues.put("hour", hours);
        db.update("hours", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }
    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("hours",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }
    public ArrayList<String> getAllRows() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from hours", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            array_list.add(res.getString(res.getColumnIndex(String.valueOf(1))));
            res.moveToNext();
        }
        return array_list;
    }

    public boolean checkDataExistOrNot(String value) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        //String query = "SELECT * FROM " + TABLE + " WHERE " + COLUMN_MONTH_YEAR + " = " + value;
        String query = "select * from hours" +   " where " + COLUMN_MONTH_YEAR+" like ?";

        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[] { "%" + value + "%" });
        //Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;  // return false if value not exists in database
        }
        cursor.close();
        return true;  // return true if value exists in database
    }
    public String getHours(String month_year) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from hours" +   " where " + COLUMN_MONTH_YEAR+" like ?";
        Cursor cursor = db.rawQuery(query, new String[] { "%" + month_year + "%" });
        if (cursor.getCount() < 1) {
            cursor.close();
            return "DOES NOT EXIST";
        }
        cursor.moveToFirst();
        while (!Objects.equals(month_year, cursor.getString(1)))
        {
            cursor.moveToNext();
        }

        String hours = cursor.getString(cursor.getColumnIndex(COLUMN_HOURS));
        cursor.close();
        return hours;
    }


}