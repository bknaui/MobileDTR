package com.example.asnaui.mobiledtr.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.asnaui.mobiledtr.model.DTRDateModel;
import com.example.asnaui.mobiledtr.model.DTRTimeModel;
import com.example.asnaui.mobiledtr.model.LeaveModel;
import com.example.asnaui.mobiledtr.model.OfficeOrderModel;
import com.example.asnaui.mobiledtr.model.UserModel;

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
                "latitude" + " TEXT NOT NULL, " +
                "longitude" + " TEXT NOT NULL, " +
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
        if (i1 > i) {
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


    public void insertUser(UserModel object) {
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

    public void insertSO(OfficeOrderModel object) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("so_no", object.so);
        cv.put("date", object.date);
        sqLiteDatabase.insert(TBL_SO, null, cv);
        sqLiteDatabase.close();
    }


    public void insertLeave(LeaveModel object) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("leave_type", object.leave_type);
        cv.put("date", object.inclusive_date);
        sqLiteDatabase.insert(TBL_LEAVE, null, cv);
        sqLiteDatabase.close();
    }

    public void insertDate(DTRDateModel object) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("date", object.date);
        sqLiteDatabase.insert(TBL_DATE, null, cv);
        sqLiteDatabase.close();
    }

    public void insertLogs(DTRTimeModel object) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("date", object.date);
        cv.put("time", object.time);
        cv.put("status", object.status);
        cv.put("filePath", object.filePath);
        cv.put("latitude", object.latitude);
        cv.put("longitude", object.longitude);
        sqLiteDatabase.insert(TBL_LOGS, null, cv);
        sqLiteDatabase.close();
    }

    public ArrayList<DTRDateModel> getDate() {
        ArrayList<DTRDateModel> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TBL_DATE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                DTRDateModel myObject = new DTRDateModel(id, date, getTimeLogs(date, sqLiteDatabase));
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

    public ArrayList<LeaveModel> getLeave() {
        ArrayList<LeaveModel> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TBL_LEAVE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String leave_type = cursor.getString(cursor.getColumnIndex("leave_type"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                list.add(new LeaveModel(leave_type, date));
                cursor.moveToNext();
            }
            cursor.close();
        }
        sqLiteDatabase.close();
        return list;
    }

    public ArrayList<OfficeOrderModel> getSO() {
        ArrayList<OfficeOrderModel> list = new ArrayList<OfficeOrderModel>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TBL_SO, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String so = cursor.getString(cursor.getColumnIndex("so_no"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                OfficeOrderModel myObject = new OfficeOrderModel(so, date);
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

    public ArrayList<DTRTimeModel> getTimeLogs(String date, SQLiteDatabase sqLiteDatabase) {
        ArrayList<DTRTimeModel> list = new ArrayList<DTRTimeModel>();
        //SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TBL_LOGS, null, "date=?", new String[]{date}, null, null, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String status = cursor.getString(cursor.getColumnIndex("status"));
                String filePath = cursor.getString(cursor.getColumnIndex("filePath"));
                String latitude = cursor.getString(cursor.getColumnIndex("latitude"));
                String longitude = cursor.getString(cursor.getColumnIndex("longitude"));
                DTRTimeModel myObject = new DTRTimeModel(date, time, status, filePath, latitude, longitude);
                list.add(myObject);
                cursor.moveToNext();
            }
        }
        // sqLiteDatabase.close();
        return list;
    }

    public UserModel getUser() {
        UserModel userModel = null;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TBL_USER, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                userModel = new UserModel(id, name);
                cursor.moveToNext();
            }
            cursor.close();
        }
        sqLiteDatabase.close();
        return userModel;
    }

    public void deleteLogs() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TBL_DATE, null, null);
        sqLiteDatabase.delete(TBL_LOGS, null, null);
        sqLiteDatabase.close();
    }

    public void deleteLogs(String date, String time) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        // sqLiteDatabase.delete(TBL_DATE, null, null);
        sqLiteDatabase.delete(TBL_LOGS, "date=? AND time=?", new String[]{date, time});
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
