package com.example.asnaui.mobiledtr.Global;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.asnaui.mobiledtr.DailyTimeRecord.DTRDate;
import com.example.asnaui.mobiledtr.DailyTimeRecord.DTRTime;
import com.example.asnaui.mobiledtr.Leave.LeaveItem;
import com.example.asnaui.mobiledtr.OfficeOrder.OfficeOrderItem;

import java.util.ArrayList;

/**
 * Created by apangcatan on 26/04/2018.
 */

public class DBContext extends SQLiteOpenHelper {
    static String DB_DTR = "db_dtr";
    static String TBL_DATE = "tbl_date";
    static String TBL_LOGS = "tbl_logs";
    static String TBL_USER = "tbl_user";

    static String TBL_LEAVE = "tbl_leave";
    static String TBL_SO = "tbl_so";
    static String TBL_CTO = "tbl_cto";


    public DBContext(Context context) {
        super(context, DB_DTR, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(" CREATE TABLE " + TBL_USER + " (" +
                "name" + " TEXT, " +
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
                "filePath" + " TEXT NOT NULL, " +
                "status" + " TEXT NOT NULL);"
        );

        sqLiteDatabase.execSQL(" CREATE TABLE " + TBL_LEAVE + " (" +
                "id" + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "leave_type" + " TEXT NOT NULL, " +
                "date" + " TEXT NOT NULL);"
        );
        sqLiteDatabase.execSQL(" CREATE TABLE " + TBL_SO + " (" +
                "id" + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "so_no" + " TEXT NOT NULL, " +
                "date" + " TEXT NOT NULL);"
        );
        sqLiteDatabase.execSQL(" CREATE TABLE " + TBL_CTO + " (" +
                "id" + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "date" + " TEXT NOT NULL);"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if(i1 > i){
            onCreate(sqLiteDatabase);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBL_USER);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBL_DATE);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBL_LOGS);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBL_LEAVE);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBL_SO);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBL_CTO);
        }
       // onCreate(sqLiteDatabase);
        Log.e("UPGRADE", "Database successfully upgraded " + "from " + 1 + " to " + i1);
    }


    public void insertUser(User object) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id", object.id);
        cv.put("name", object.name);
        sqLiteDatabase.insert(TBL_USER, null, cv);
        sqLiteDatabase.close();
    }

    public void insertCTO(String object) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("date", object);
        sqLiteDatabase.insert(TBL_CTO, null, cv);
        sqLiteDatabase.close();
    }

    public void insertSO(OfficeOrderItem object) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("so_no", object.so);
        cv.put("date", object.date);
        sqLiteDatabase.insert(TBL_SO, null, cv);
        sqLiteDatabase.close();
    }


    public void insertLeave(LeaveItem object) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("leave_type", object.leave_type);
        cv.put("date", object.inclusive_date);
        sqLiteDatabase.insert(TBL_LEAVE, null, cv);
        sqLiteDatabase.close();
    }

    public void insertDate(DTRDate object) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("date", object.date);
        sqLiteDatabase.insert(TBL_DATE, null, cv);
        sqLiteDatabase.close();
    }

    public void insertLogs(DTRTime object) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("date", object.date);
        cv.put("time", object.time);
        cv.put("status", object.status);
        cv.put("filePath", object.filePath);
        sqLiteDatabase.insert(TBL_LOGS, null, cv);
        sqLiteDatabase.close();
    }

    public ArrayList<DTRDate> getDate() {
        ArrayList<DTRDate> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TBL_DATE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                DTRDate myObject = new DTRDate(id, date, getTimeLogs(date,sqLiteDatabase));
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
        Cursor cursor = sqLiteDatabase.query(TBL_LOGS, null, "date=?", new String[]{date}, null, null, "time DESC");
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

    public ArrayList<LeaveItem> getLeave() {
        ArrayList<LeaveItem> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TBL_LEAVE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String leave_type = cursor.getString(cursor.getColumnIndex("leave_type"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                list.add(new LeaveItem(leave_type, date));
                cursor.moveToNext();
            }
            cursor.close();
        }
        sqLiteDatabase.close();
        return list;
    }

    public ArrayList<OfficeOrderItem> getSO() {
        ArrayList<OfficeOrderItem> list = new ArrayList<OfficeOrderItem>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TBL_SO, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String so = cursor.getString(cursor.getColumnIndex("so_no"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                OfficeOrderItem myObject = new OfficeOrderItem(so, date);
                list.add(myObject);
                cursor.moveToNext();
            }
            cursor.close();
        }
        sqLiteDatabase.close();
        return list;
    }

    public ArrayList<String> getCTO() {
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TBL_CTO, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                String date = cursor.getString(cursor.getColumnIndex("date"));
                list.add(date);
                cursor.moveToNext();
            }
            cursor.close();
        }
        sqLiteDatabase.close();
        return list;
    }

    public ArrayList<DTRTime> getTimeLogs(String date,SQLiteDatabase sqLiteDatabase) {
        ArrayList<DTRTime> list = new ArrayList<DTRTime>();
        //SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TBL_LOGS, null, "date=?", new String[]{date}, null, null, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String status = cursor.getString(cursor.getColumnIndex("status"));
                String filePath = cursor.getString(cursor.getColumnIndex("filePath"));
                DTRTime myObject = new DTRTime(date, time, status,filePath);
                list.add(myObject);
                cursor.moveToNext();
            }
        }
       // sqLiteDatabase.close();
        return list;
    }

    public User getUser() {
        User user = null;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TBL_USER, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                user = new User(id, name);
                cursor.moveToNext();
            }
            cursor.close();
        }
        sqLiteDatabase.close();
        return user;
    }

    public void deleteLogs() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TBL_DATE, null, null);
        sqLiteDatabase.delete(TBL_LOGS, null, null);
        sqLiteDatabase.close();
    }

    public void deleteLogs(String date,String time) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
       // sqLiteDatabase.delete(TBL_DATE, null, null);
        sqLiteDatabase.delete(TBL_LOGS, "date=? AND time=?", new String[]{date,time});
        sqLiteDatabase.close();
    }

    public void deleteLeave(String type, String date) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        if (!date.equalsIgnoreCase("")) {
            sqLiteDatabase.delete(TBL_LEAVE, "leave_type=? AND date=?", new String[]{type, date});
        } else {
            sqLiteDatabase.delete(TBL_LEAVE, null, null);
        }

        sqLiteDatabase.close();
    }

    public void deleteSO(String so, String date) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        if (!date.equalsIgnoreCase("")) {
            sqLiteDatabase.delete(TBL_SO, "so_no=? AND date=?", new String[]{so, date});
        } else {
            sqLiteDatabase.delete(TBL_SO, null, null);
        }

        sqLiteDatabase.close();
    }

    public void deleteCTO(String date) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        if (!date.equalsIgnoreCase("")) {
            sqLiteDatabase.delete(TBL_CTO, "date=?", new String[]{date});
        } else {
            sqLiteDatabase.delete(TBL_CTO, null, null);
        }
        sqLiteDatabase.close();
    }

}
