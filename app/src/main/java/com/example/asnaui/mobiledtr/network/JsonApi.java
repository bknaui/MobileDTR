package com.example.asnaui.mobiledtr.network;

import android.content.Context;
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
import com.example.asnaui.mobiledtr.contract.LoginContract;
import com.example.asnaui.mobiledtr.database.DBContext;
import com.example.asnaui.mobiledtr.model.UserModel;
import com.example.asnaui.mobiledtr.util.Constant;
import com.example.asnaui.mobiledtr.view.activity.Home;
import com.example.asnaui.mobiledtr.view.fragment.CompensatoryTimeOff;
import com.example.asnaui.mobiledtr.view.fragment.DTR;
import com.example.asnaui.mobiledtr.view.fragment.Leave;
import com.example.asnaui.mobiledtr.view.fragment.OfficeOrder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class JsonApi {
    private static JsonApi mInstance;
    private RequestQueue mRequestQueue;
    DBContext dbContext;


    private static Context mCtx;

    private JsonApi(Context context) {
        mCtx = context;
        dbContext = new DBContext(mCtx);
        mRequestQueue = getRequestQueue();
    }

    public static synchronized JsonApi getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new JsonApi(context);
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
                Log.e("onResponse", response);
                if (response.equalsIgnoreCase("1")) {
                    if ((time_position + 1) < DTR.list.get(date_position).list.size()) {
                        dbContext.deleteLogs(DTR.list.get(date_position).list.get(time_position).date, DTR.list.get(date_position).list.get(time_position).time);
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
                Log.e("HELLO", error.getMessage()+" ");

                if(error == null || error.networkResponse == null){
                    Toast.makeText(mCtx, "Something went wrong, please contact administrator", Toast.LENGTH_SHORT).show();
                }
                else if (error instanceof NoConnectionError) {
                    Toast.makeText(mCtx, "No network connection", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(mCtx, "Connection time out", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mCtx, error.getMessage()+" ", Toast.LENGTH_SHORT).show();
                }
                Home.pd.dismiss();
                Log.e("InsertLogs", error.getMessage() + " ");
                Home.dtr.displayList();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userid", Home.userModel.id);
                params.put("time", DTR.list.get(date_position).list.get(time_position).time);
                params.put("event", DTR.list.get(date_position).list.get(time_position).status);
                params.put("date", DTR.list.get(date_position).list.get(time_position).date);
                params.put("latitude", DTR.list.get(date_position).list.get(time_position).latitude);
                params.put("longitude", DTR.list.get(date_position).list.get(time_position).longitude);
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
                                    "\nLocation: " + DTR.list.get(date_position).list.get(time_position).latitude +" "+DTR.list.get(date_position).list.get(time_position).longitude+
                                    "\nEvent: " + DTR.list.get(date_position).list.get(time_position).status+
                                    "\nEncoded: " + image_str);

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
                params.put("userid", Home.userModel.id);
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
                params.put("userid", Home.userModel.id);
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
                params.put("userid", Home.userModel.id);
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

    public void Login(String url, final String imei, final LoginContract.LoginCallback loginCallback) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("0")) {
                    loginCallback.onFail("IMEI not registered");
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String userid = jsonObject.getString("userid");
                        String name = jsonObject.getString("fname") + " " + jsonObject.getString("lname");

                        UserModel userModel = new UserModel(userid,name);
                        loginCallback.onSuccess(userModel);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        loginCallback.onFail(e.getMessage()+"");
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    loginCallback.onFail("No network connection");

                } else if (error instanceof TimeoutError) {
                    loginCallback.onFail("Connection time out");
                } else {
                    loginCallback.onFail("Something went wrong, please contact administrator");
                }
                Log.e("LoginError", error.getMessage() + " A");

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
