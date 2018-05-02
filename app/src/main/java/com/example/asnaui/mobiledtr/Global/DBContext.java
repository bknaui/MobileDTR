package com.example.asnaui.mobiledtr.Global;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.asnaui.mobiledtr.DailyTimeRecord.DTRDate;
import com.example.asnaui.mobiledtr.DailyTimeRecord.DTRTime;

import java.util.ArrayList;

/**
 * Created by apangcatan on 26/04/2018.
 */

public class DBContext extends SQLiteOpenHelper {
    static String DB_DTR = "db_dtr";
    static String TBL_DATE = "tbl_date";
    static String TBL_LOGS = "tbl_logs";
    static String TBL_USER = "tbl_user";

    public DBContext(Context context) {
        super(context, DB_DTR, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(" CREATE TABLE " + TBL_USER + " (" +
                "id" + " TEXT NOT NULL);"
        );
        sqLiteDatabase.execSQL(" CREATE TABLE " + TBL_DATE + " (" +
                "id" + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "date" + " TEXT NOT NULL);"
        );
        sqLiteDatabase.execSQL(" CREATE TABLE " + TBL_LOGS + " (" +
                "id" + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "date" + " TEXT NOT NULL, " +
                "time" + " TEXT NOT NULL, " +
                "status" + " TEXT NOT NULL);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBL_USER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBL_DATE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBL_LOGS);
    }

    public void insertUser(String id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id", id);
        long result = sqLiteDatabase.insert(TBL_USER, null, cv);
        sqLiteDatabase.close();
    }

    public void insertDate(DTRDate object) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("date", object.date);
        long result = sqLiteDatabase.insert(TBL_DATE, null, cv);
        sqLiteDatabase.close();
    }

    public void insertLogs(DTRTime object) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("date", object.date);
        cv.put("time", object.time);
        cv.put("status", object.status);

        long result = sqLiteDatabase.insert(TBL_LOGS, null, cv);
        sqLiteDatabase.close();
    }

    public ArrayList<DTRDate> getDate() {
        ArrayList<DTRDate> list = new ArrayList<DTRDate>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TBL_DATE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                DTRDate myObject = new DTRDate(id, date, getTimeLogs(date));
                list.add(myObject);
                cursor.moveToNext();
            }
            cursor.close();
        }
        sqLiteDatabase.close();
        return list;
    }

    public boolean dateExists(String date) {
        boolean found = false;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TBL_DATE, null, "date=?", new String[]{date}, null, null, "id DESC");
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                found = true;
                break;
            }
            cursor.close();
        }
        sqLiteDatabase.close();
        return found;
    }

    public String lastStatus(String date) {
        String status = "IN";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TBL_LOGS, null, "date=?", new String[]{date}, null, null, "id DESC");
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                status = cursor.getString(cursor.getColumnIndex("status"));
                if (status.equalsIgnoreCase("IN")) status = "OUT";
                else status = "IN";
                break;
            }
            cursor.close();
        }
        sqLiteDatabase.close();
        return status;
    }

    public ArrayList<DTRTime> getTimeLogs(String date) {
        ArrayList<DTRTime> list = new ArrayList<DTRTime>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TBL_LOGS, null, "date=?", new String[]{date}, null, null, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String status = cursor.getString(cursor.getColumnIndex("status"));
                DTRTime myObject = new DTRTime(date, time, status);
                list.add(myObject);
                cursor.moveToNext();
            }
        }
        sqLiteDatabase.close();
        return list;
    }

    public String getID() {
        String id = "";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TBL_USER, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                id = cursor.getString(cursor.getColumnIndex("id"));
                cursor.moveToNext();
            }
            cursor.close();
        }
        sqLiteDatabase.close();
        return id;
    }
}
