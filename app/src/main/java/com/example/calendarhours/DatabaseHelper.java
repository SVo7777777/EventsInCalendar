package com.example.calendarhours;

import java.util.ArrayList;
import java.util.Objects;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "hours33.db"; // название бд
    private static final int SCHEMA = 2; // версия базы данных
    public static final String TABLE = "hours"; // название таблицы в бд
    public static final String TABLE1 = "plan1"; // название таблицы в бд
    public static final String TABLE2 = "plan2"; // название таблицы в бд
    // названия столбцов
    //public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MONTH_YEAR = "month_year";
    public static final String COLUMN_HOURS = "hour";
    public static final String  COLUMN_QUANTITY_HOURS = "quantity_hour";
    public static final String COLUMN_SALARY = "salary";
    public static final String COLUMN_PRICE = "price";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "create table hours " +
                        "(id integer primary key, month_year text, hour text, quantity_hour text, salary text, price text)");

        db.execSQL(
                "create table plan1 " +
                        "(id integer primary key, month_year text, hour text, quantity_hour text, salary text, price text)");
        db.execSQL(
                "create table plan2 " +
                        "(id integer primary key, month_year text, hour text, quantity_hour text, salary trxt, price text)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE);
      switch (oldVersion) {
        case 1:
            db.execSQL("DROP TABLE IF EXISTS " + TABLE);

        case 2:
            db.execSQL("ALTER TABLE hours ADD COLUMN price TEXT");
            }


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

    public void insertContact(String month_year, String hours, String salary, String price, String quantity_hour, String table_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("month_year", month_year);
        contentValues.put("hour", hours);
        contentValues.put("quantity_hour", quantity_hour);
        contentValues.put("salary", salary);
        contentValues.put("price", price);
        db.insert(table_name, null, contentValues);
    }

    public boolean updateHours(Integer id, String month_year, String hours, String table_name, String quantity_hour) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("month_year", month_year);
        contentValues.put("hour", hours);
        contentValues.put("quantity_hour", quantity_hour);
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
        return db.delete("hours",
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public ArrayList<ArrayList<String>> getAllRows() {
        ArrayList<ArrayList<String>>  array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        int i = 0;
        //int j = 0;
        @SuppressLint("Recycle")
        Cursor res = db.rawQuery("select * from hours", null);
        res.moveToFirst();
//        while (!res.isAfterLast()) {
//
//            array_list.add(res.getString(res.getColumnIndex(String.valueOf(0))));
//
//            res.moveToNext();
//        }

        if(res.getCount() > 0) {
            if (res.moveToFirst()) {
                do {
                    ArrayList<String> row = new ArrayList<String>();
                    row.add(res.getString(1));
                    row.add(res.getString(2));
                    row.add(res.getString(3));
                    row.add(res.getString(4));
                    row.add(res.getString(5));
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
        //String query = "SELECT * FROM " + TABLE + " WHERE " + COLUMN_MONTH_YEAR + " = " + value;
        //String query = "select * from hours" + " where " + COLUMN_MONTH_YEAR + " like ?";
        String query = "SELECT * FROM " + TABLE3 + " WHERE " + COLUMN_MONTH_YEAR + " like ?";

        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{"%" + value + "%"});
        //Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;  // return false if value not exists in database
        }
        cursor.close();
        return true;  // return true if value exists in database
    }

    public String getHours(String month_year, String TABLE3) {
        SQLiteDatabase db = this.getReadableDatabase();
        //String query = "select * from hours" + " where " + COLUMN_MONTH_YEAR + " like ?";
        String query = "SELECT * FROM " + TABLE3 + " WHERE "+ COLUMN_MONTH_YEAR + " like ?";
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
        String hours = cursor.getString(cursor.getColumnIndex(COLUMN_HOURS));
        cursor.close();
        return hours;
    }
    public String getQuantityHours(String month_year, String TABLE3) {
        SQLiteDatabase db = this.getReadableDatabase();
        //String query = "select * from hours" + " where " + COLUMN_MONTH_YEAR + " like ?";
        String query = "SELECT * FROM " + TABLE3 + " WHERE "+ COLUMN_MONTH_YEAR + " like ?";
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
        String hours = cursor.getString(cursor.getColumnIndex(COLUMN_QUANTITY_HOURS));
        cursor.close();
        return hours;
    }
    public String getSalary(String month_year, String TABLE3) {
        SQLiteDatabase db = this.getReadableDatabase();
        //String query = "select * from hours" + " where " + COLUMN_MONTH_YEAR + " like ?";
        String query = "SELECT * FROM " + TABLE3 + " WHERE "+ COLUMN_MONTH_YEAR + " like ?";
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
        String hours = cursor.getString(cursor.getColumnIndex(COLUMN_SALARY));
        cursor.close();
        return hours;
    }
    public String getPrice(String month_year, String TABLE3) {
        SQLiteDatabase db = this.getReadableDatabase();
        //String query = "select * from hours" + " where " + COLUMN_MONTH_YEAR + " like ?";
        String query = "SELECT * FROM " + TABLE3 + " WHERE "+ COLUMN_MONTH_YEAR + " like ?";
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
        String hours = cursor.getString(cursor.getColumnIndex(COLUMN_PRICE));
        cursor.close();
        return hours;
    }
    public int GetId(String currentNote, String TABLE3) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        @SuppressLint("Recycle")
        Cursor getNoteId = myDB.rawQuery("select id from'"+ TABLE3 +"' where month_year = '" + currentNote + "'", null);
        //Cursor getNoteId = myDB.rawQuery("select id from notepadData where notepad like + "'" + currentNote + "'", null);
        if (getNoteId != null && getNoteId.moveToFirst()) {
            return getNoteId.getInt(0);
        } else {
            return Integer.parseInt(null);  // because you have to return something
        }
    }
}
