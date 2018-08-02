package com.example.asnaui.mobiledtr.Global;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.asnaui.mobiledtr.CompensatoryTimeOff.CompensatoryTimeOff;
import com.example.asnaui.mobiledtr.DailyTimeRecord.DTR;
import com.example.asnaui.mobiledtr.Home;
import com.example.asnaui.mobiledtr.Leave.Leave;
import com.example.asnaui.mobiledtr.MainActivity;
import com.example.asnaui.mobiledtr.OfficeOrder.OfficeOrder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class JsonAPi {
    private static JsonAPi mInstance;
    private RequestQueue mRequestQueue;
    DBContext dbContext;


    private static Context mCtx;

    private JsonAPi(Context context) {
        mCtx = context;
        dbContext = new DBContext(mCtx);
        mRequestQueue = getRequestQueue();
    }

    public static synchronized JsonAPi getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new JsonAPi(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public void InsertLogs(final String url, final int date_position, final int time_position) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("1")) {
                    if ((time_position + 1) < DTR.list.get(date_position).list.size()) {
                       // dbContext.deleteLogs(DTR.list.get(date_position).list.get(time_position).date, DTR.list.get(date_position).list.get(time_position).time);
                        InsertLogs(url, date_position, (time_position + 1));
                    } else {
                        if ((date_position + 1) < DTR.list.size()) {
                            InsertLogs(url, (date_position + 1), 0);
                        } else {
                            Home.pd.dismiss();
                            dbContext.deleteLogs();
                            Home.dtr.displayList();
                            Toast.makeText(mCtx, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                            Constant.deletePictures();
                        }
                    }
                } else {
                    Toast.makeText(mCtx, "No network connection", Toast.LENGTH_SHORT).show();
                    Home.pd.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    Toast.makeText(mCtx, "No network connection", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(mCtx, "Connection time out", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mCtx, "Something went wrong, please contact administrator", Toast.LENGTH_SHORT).show();
                }
                Home.pd.dismiss();
                Log.e("InsertLogs", error.getMessage() + " ");
                Home.dtr.displayList();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userid", Home.user.id);
                params.put("time", DTR.list.get(date_position).list.get(time_position).time);
                params.put("event", DTR.list.get(date_position).list.get(time_position).status);
                params.put("date", DTR.list.get(date_position).list.get(time_position).date);
                params.put("filename", DTR.list.get(date_position).list.get(time_position).filePath.split("/")[6]);
                try {

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(DTR.list.get(date_position).list.get(time_position).filePath),
                            null, options);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 120, 120, false);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
                    byte[] byte_arr = stream.toByteArray();
                    String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);
                    params.put("image", image_str);

                    Log.e("INSERTED",
                            "Date: " + DTR.list.get(date_position).list.get(time_position).date +
                                    "\nTime: " + DTR.list.get(date_position).list.get(time_position).time +
                                    "\nFilepath: " + DTR.list.get(date_position).list.get(time_position).filePath.split("/")[6] +
                                    "\nEncoded: " + image_str +
                                    "\nEvent: " + DTR.list.get(date_position).list.get(time_position).status);

                } catch (FileNotFoundException e) {
                    Log.e("FileNotFound", e.getMessage());
                    params.put("image", "");
                }
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }

    public void InsertLeave(final String url, final int position) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("1")) {
                    if ((position + 1) < Leave.list.size()) {
                        InsertLeave(url, (position + 1));
                    } else {
                        Home.pd.dismiss();
                        dbContext.deleteLeave("", "");
                        Home.leave.displayLeave();
                        Toast.makeText(mCtx, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mCtx, "No network connection", Toast.LENGTH_SHORT).show();
                    Home.pd.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    Toast.makeText(mCtx, "No network connection", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(mCtx, "Connection time out", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mCtx, "Something went wrong, please contact adminstrator", Toast.LENGTH_SHORT).show();
                }
                Home.pd.dismiss();
                Log.e("InsertLeave", error.getMessage() + " ");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userid", Home.user.id);
                params.put("leave_type", Leave.list.get(position).leave_type);
                params.put("daterange", Leave.list.get(position).inclusive_date);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }

    public void InsertSO(final String url, final int position) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("1")) {
                    if ((position + 1) < Leave.list.size()) {
                        InsertLeave(url, (position + 1));
                    } else {
                        Home.pd.dismiss();
                        dbContext.deleteSO("", "");
                        Home.oo.display();
                        Toast.makeText(mCtx, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mCtx, "No network connection", Toast.LENGTH_SHORT).show();
                    Home.pd.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    Toast.makeText(mCtx, "No network connection", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(mCtx, "Connection time out", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mCtx, "Something went wrong, please contact adminstrator", Toast.LENGTH_SHORT).show();
                }
                Home.pd.dismiss();
                Log.e("InsertSO", error.getMessage() + " ");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userid", Home.user.id);
                params.put("so", OfficeOrder.list.get(position).so);
                params.put("daterange", OfficeOrder.list.get(position).date);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }

    public void InsertCTO(final String url, final int position) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("1")) {
                    if ((position + 1) < Leave.list.size()) {
                        InsertLeave(url, (position + 1));
                    } else {
                        Home.pd.dismiss();
                        dbContext.deleteCTO("");
                        Home.cto.display();
                        Toast.makeText(mCtx, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mCtx, "No network connection", Toast.LENGTH_SHORT).show();
                    Home.pd.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    Toast.makeText(mCtx, "No network connection", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(mCtx, "Connection time out", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mCtx, "Something went wrong, please contact adminstrator", Toast.LENGTH_SHORT).show();
                }
                Home.pd.dismiss();
                Log.e("InsertCTO", error.getMessage() + " ");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userid", Home.user.id);
                params.put("daterange", CompensatoryTimeOff.list.get(position));
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }

    public void Login(String url, final String imei) {
        MainActivity.pd = new ProgressDialog(mCtx);
        MainActivity.pd.setMessage("Loading, please wait ...");
        MainActivity.pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("0")) {
                    Toast.makeText(mCtx, "IMEI not registered", Toast.LENGTH_SHORT).show();
                    MainActivity.pd.dismiss();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String userid = jsonObject.getString("userid");
                        String name = jsonObject.getString("fname") + " " + jsonObject.getString("lname");

                        dbContext.insertUser(new User(userid, name));
                        Intent intent = new Intent(mCtx, Home.class);
                        mCtx.startActivity(intent);
                        ((Activity) mCtx).finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        MainActivity.pd.dismiss();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    Toast.makeText(mCtx, "No network connection", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(mCtx, "Connection time out", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mCtx, "Something went wrong, please contact administrator", Toast.LENGTH_SHORT).show();
                }
                Log.e("LoginError", error.getMessage() + " A");
                MainActivity.pd.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("imei", imei);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }
}
