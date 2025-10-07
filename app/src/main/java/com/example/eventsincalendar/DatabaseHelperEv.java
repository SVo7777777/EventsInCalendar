package com.example.eventsincalendar;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Objects;

public class DatabaseHelperEv extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "my_events.db"; // название бд
    private static final int SCHEMA = 1; // версия базы данных
    public static final String TABLE = "events"; // название таблицы в бд
    public static final String TABLE1 = "plan1"; // название таблицы в бд
    public static final String TABLE2 = "plan2"; // название таблицы в бд
    // названия столбцов
    //public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATA = "data";
    public static final String COLUMN_EVENTS = "event";
    public static final String COLUMN_REPEAT_DAY = "day";
    public static final String COLUMN_REPEAT_WEEK = "week";
    public static final String COLUMN_REPEAT_MONTH = "month";
    public static final String COLUMN_REPEAT_YEAR = "year";






    public DatabaseHelperEv(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "create table events " +
                        "(id integer primary key, data text, event text, day text, week text, month text, year text)");

//        db.execSQL(
//                "create table plan1 " +
//                        "(id integer primary key, month_year text, hour text, quantity_hour text, salary text, price text)");
//        db.execSQL(
//                "create table plan2 " +
//                        "(id integer primary key, month_year text, hour text, quantity_hour text, salary trxt, price text)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
//      switch (oldVersion) {
//        case 1:
//            db.execSQL("DROP TABLE IF EXISTS " + TABLE);
//
////        case 2:
////            db.execSQL("ALTER TABLE hours ADD COLUMN price TEXT");
//            }


        onCreate(db);
    }
    public void AddnewTable(String name_table){
        //At first you will need a Database object.Lets create it.
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(
                "create table if not exists'" + name_table +
                        "'(id integer primary key, month_year text, hour text, quantity_hour text, salary trxt)");

        //db.execSQL(CreateTableString);//CreateTableString is the SQL Command String
    }

    public void insertContact(String data, String event, String day, String week, String month, String year, String table_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("data", data);
        contentValues.put("event", event);
        contentValues.put("day", day);
        contentValues.put("week", week);
        contentValues.put("month", month);
        contentValues.put("year", year);

        db.insert(table_name, null, contentValues);
    }

    public boolean updateEvents(Integer id, String data, String event, String table_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("data", data);
        contentValues.put("event", event);
        //contentValues.put("quantity_hour", quantity_hour);
        db.update(table_name, contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }
    public boolean updateSalary(Integer id, String month_year, String salary, String table_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("month_year", month_year);
        contentValues.put("salary", salary);
        //contentValues.put("quantity_hour", quantity_hour);
        db.update(table_name, contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }
    public boolean updatePrice(Integer id, String month_year, String price, String table_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("month_year", month_year);
        contentValues.put("price", price);
        //contentValues.put("quantity_hour", quantity_hour);
        db.update(table_name, contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public Integer deleteContact(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("events",
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public ArrayList<ArrayList<String>> getAllRows() {
        ArrayList<ArrayList<String>>  array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle")
        Cursor res = db.rawQuery("select * from events", null);
        res.moveToFirst();

        if(res.getCount() > 0) {
            if (res.moveToFirst()) {
                do {
                    ArrayList<String> row = new ArrayList<>();
                    row.add(res.getString(1));
                    row.add(res.getString(2));
                    row.add(res.getString(3));
                    row.add(res.getString(4));
                    row.add(res.getString(5));
                    row.add(res.getString(6));
                    array_list.add(row);
                    //array_list.add(String.valueOf(res.getColumnIndex(COLUMN_MONTH_YEAR)));

                } while (res.moveToNext());
                res.close();
            }

        }
        return array_list;
    }

    public boolean checkDataExistOrNot(String value, String TABLE3) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE3 + " WHERE " + COLUMN_DATA + " like ?";

        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{"%" + value + "%"});
        //Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;  // return false if value not exists in database
        }
        cursor.close();
        return true;  // return true if value exists in database
    }


    public boolean checkBaseEmptyOrNot(String TABLE3) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        //String query = "SELECT * FROM " + TABLE3;

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE3, null);
        //Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.getCount() == 0) {
            // для пустого возвращает true
            cursor.close();
            return true;
        } else {
            // не пустой возвращает false
            cursor.close();
            return false;
        }

    }

    public String getEvents(String month_year, String TABLE3) {
        SQLiteDatabase db = this.getReadableDatabase();
        //String query = "select * from hours" + " where " + COLUMN_MONTH_YEAR + " like ?";
        String query = "SELECT * FROM " + TABLE3 + " WHERE "+ COLUMN_DATA + " like ?";
        Cursor cursor = db.rawQuery(query, new String[]{"%" + month_year + "%"});
        if (cursor.getCount() < 1) {
            cursor.close();
            return "DOES NOT EXIST";
        }
        cursor.moveToFirst();
        while (!Objects.equals(month_year, cursor.getString(1))) {
            cursor.moveToNext();
        }

        @SuppressLint("Range")
        String events = cursor.getString(cursor.getColumnIndex(COLUMN_EVENTS));
        cursor.close();
        return events;
    }
    public String getData(String month_year, String TABLE3) {
        SQLiteDatabase db = this.getReadableDatabase();
        //String query = "select * from hours" + " where " + COLUMN_MONTH_YEAR + " like ?";
        String query = "SELECT * FROM " + TABLE3 + " WHERE "+ COLUMN_DATA + " like ?";
        Cursor cursor = db.rawQuery(query, new String[]{"%" + month_year + "%"});
        if (cursor.getCount() < 1) {
            cursor.close();
            return "DOES NOT EXIST";
        }
        cursor.moveToFirst();
        while (!Objects.equals(month_year, cursor.getString(1))) {
            cursor.moveToNext();
        }

        @SuppressLint("Range")
        String data = cursor.getString(cursor.getColumnIndex(COLUMN_DATA));
        cursor.close();
        return data;
    }
    public String getDay(String month_year, String TABLE3) {
        SQLiteDatabase db = this.getReadableDatabase();
        //String query = "select * from hours" + " where " + COLUMN_MONTH_YEAR + " like ?";
        String query = "SELECT * FROM " + TABLE3 + " WHERE "+ COLUMN_REPEAT_DAY + " like ?";
        Cursor cursor = db.rawQuery(query, new String[]{"%" + month_year + "%"});
        if (cursor.getCount() < 1) {
            cursor.close();
            return "DOES NOT EXIST";
        }
        cursor.moveToFirst();
        while (!Objects.equals(month_year, cursor.getString(1))) {
            cursor.moveToNext();
        }

        @SuppressLint("Range")
        String events = cursor.getString(cursor.getColumnIndex(COLUMN_REPEAT_DAY));
        cursor.close();
        return events;
    }
    public String getWeek(String month_year, String TABLE3) {
        SQLiteDatabase db = this.getReadableDatabase();
        //String query = "select * from hours" + " where " + COLUMN_MONTH_YEAR + " like ?";
        String query = "SELECT * FROM " + TABLE3 + " WHERE "+ COLUMN_REPEAT_WEEK + " like ?";
        Cursor cursor = db.rawQuery(query, new String[]{"%" + month_year + "%"});
        if (cursor.getCount() < 1) {
            cursor.close();
            return "DOES NOT EXIST";
        }
        cursor.moveToFirst();
        while (!Objects.equals(month_year, cursor.getString(1))) {
            cursor.moveToNext();
        }

        @SuppressLint("Range")
        String events = cursor.getString(cursor.getColumnIndex(COLUMN_REPEAT_WEEK));
        cursor.close();
        return events;
    }
    public String getMonth(String month_year, String TABLE3) {
        SQLiteDatabase db = this.getReadableDatabase();
        //String query = "select * from hours" + " where " + COLUMN_MONTH_YEAR + " like ?";
        String query = "SELECT * FROM " + TABLE3 + " WHERE "+ COLUMN_REPEAT_MONTH + " like ?";
        Cursor cursor = db.rawQuery(query, new String[]{"%" + month_year + "%"});
        if (cursor.getCount() < 1) {
            cursor.close();
            return "DOES NOT EXIST";
        }
        cursor.moveToFirst();
        while (!Objects.equals(month_year, cursor.getString(1))) {
            cursor.moveToNext();
        }

        @SuppressLint("Range")
        String events = cursor.getString(cursor.getColumnIndex(COLUMN_REPEAT_MONTH));
        cursor.close();
        return events;
    }

    public String getYear(String month_year, String TABLE3) {
        SQLiteDatabase db = this.getReadableDatabase();
        //String query = "select * from hours" + " where " + COLUMN_MONTH_YEAR + " like ?";
        String query = "SELECT * FROM " + TABLE3 + " WHERE "+ COLUMN_REPEAT_YEAR + " like ?";
        Cursor cursor = db.rawQuery(query, new String[]{"%" + month_year + "%"});
        if (cursor.getCount() < 1) {
            cursor.close();
            return "DOES NOT EXIST";
        }
        cursor.moveToFirst();
        while (!Objects.equals(month_year, cursor.getString(1))) {
            cursor.moveToNext();
        }

        @SuppressLint("Range")
        String events = cursor.getString(cursor.getColumnIndex(COLUMN_REPEAT_YEAR));
        cursor.close();
        return events;
    }

    public int GetId(String currentNote, String TABLE3) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        @SuppressLint("Recycle")
        Cursor getNoteId = myDB.rawQuery("select id from'"+ TABLE3 +"' where data = '" + currentNote + "'", null);
        //Cursor getNoteId = myDB.rawQuery("select id from notepadData where notepad like + "'" + currentNote + "'", null);
        if (getNoteId.moveToFirst()) {
            return getNoteId.getInt(0);
        } else {
            return Integer.parseInt(null);  // because you have to return something
        }
    }
}
