package com.example.mamooddiary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_name ="Mydatabase.dB";
    public static final String table_name ="Mytable";
    public static final String col_1 ="id";
    public static final String col_2 ="day";
    public static final String col_3 ="month";
    public static final String col_4 ="year";
    public static final String col_5 ="message";
    public static final String col_6 ="mood";
    public SQLiteDatabase db;





    public DBHelper(@Nullable Context context) {
        super(context, DB_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Mytable(id INTEGER PRIMARY KEY AUTOINCREMENT,day TEXT,month  TEXT,year TEXT,message TEXT,mood TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table_name);
        onCreate(db);
    }

    public boolean AddData(String day,String month,String year,String message,String mood){
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_2,day);
        contentValues.put(col_3,month);
        contentValues.put(col_4,year);
        contentValues.put(col_5,message);
        contentValues.put(col_6,mood);

        long result = db.insert(table_name,null,contentValues);
        db.close();
        if(result == -1){
            return false;
        }else {
            return true;
        }
    }

    public String  selectMood(String day,String month,String year){
        db = this.getWritableDatabase();
        String selectmood = "SELECT mood FROM Mytable WHERE day = ? AND month = ? AND year = ?";
        String mood = "";

        String[] args = new String[] { String.valueOf(day), String.valueOf(month), String.valueOf(year) };
        Cursor cursor = db.rawQuery(selectmood, args);

        if (cursor.moveToFirst()) {
            mood = cursor.getString(0);
        }

        cursor.close();
        db.close();

        return mood;
    }
    public String[] selectDiary(String day, String month, String year){
        db = this.getWritableDatabase();
        String selectmood = "SELECT message,mood FROM Mytable WHERE day = ? AND month = ? AND year = ?";
        String message = "";
        String mood = "";
        boolean success = false;

        String[] args = new String[] { String.valueOf(day), String.valueOf(month), String.valueOf(year) };
        Cursor cursor = db.rawQuery(selectmood, args);

        if (cursor.moveToFirst()) {
            message = cursor.getString(0);
            mood = cursor.getString(1);
            success = true;
        }

        cursor.close();
        db.close();

        return new String[] {message, mood, String.valueOf(success)} ;
    }


    public boolean updateDiary(String day, String month, String year, String message, String mood) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_5, message);
        contentValues.put(col_6, mood);

        String selection = "day = ? AND month = ? AND year = ?";
        String[] selectionArgs = new String[]{day, month, year};

        int result = db.update(table_name, contentValues, selection, selectionArgs);

        db.close();
        return result > 0;
    }

}
