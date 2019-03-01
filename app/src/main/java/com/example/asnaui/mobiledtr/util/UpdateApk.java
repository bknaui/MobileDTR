//package com.example.asnaui.mobiledtr.util;
//
//import android.app.DownloadManager;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.net.Uri;
//import android.os.Environment;
//import android.util.Log;
//
//import java.io.File;
//
//public class UpdateApk {
//
//
//    public void downloadAndInstallApk() {
//        try {
//            final String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/mobile_dtr.apk";
//            final Uri uri = Uri.parse("file://" + destination);
//
//            File file = new File(destination);
//            if (file.exists()) file.delete();
//
//            String url = "http://192.168.100.14/pis/public/apk/app-debug.apk";
//
//            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
//            request.setDescription("Download new version of the App");
//            request.setTitle("WFP Tracking");
//
//            request.setDestinationUri(uri);
//
//            final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//            manager.enqueue(request);
//
//            BroadcastReceiver onComplete = new BroadcastReceiver() {
//                public void onReceive(Context ctxt, Intent intent) {
//                    if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
//                        pd.dismiss();
//
//                        Intent i = new Intent(Intent.ACTION_VIEW);
//                        i.setDataAndType(Uri.fromFile(new File(destination)),
//                                "application/vnd.android.package-archive");
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(i);
//                    }
//                }
//            };
//
//            IntentFilter filter = new IntentFilter();
//            filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
//
//            registerReceiver(onComplete, filter);
//        } catch (Exception e) {
//            Log.e("Exception", e.getMessage());
//            pd.dismiss();
//        }
//    }


//public int VERSION_CODE(){
//        int version_code=-1;
//        try{
//        PackageInfo pInfo=this.getPackageManager().getPackageInfo(getPackageName(),0);
//        version_code=pInfo.versionCode;
//        }catch(PackageManager.NameNotFoundException e){
//        e.printStackTrace();
//        }
//        return version_code;
//        }
//
//public void compareVersion(){
//        StringRequest stringRequest=new StringRequest(Request.Method.GET,"http://192.168.100.14/pis/CheckVersion",new Response.Listener<String>(){
//@Override
//public void onResponse(String response){
//        if(!response.equalsIgnoreCase(VERSION_CODE()+"")){
//        AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
//        builder.setTitle("Notice!");
//        builder.setMessage("WFP Tracking App v"+response+" is now available, please update your app."+
//        "\n\nNote: Updating will close the application to apply changes.");
//        builder.setPositiveButton("Download",new DialogInterface.OnClickListener(){
//@Override
//public void onClick(DialogInterface dialogInterface,int i){
//        pd=ProgressDialog.show(LoginActivity.this,"Downloading","Please wait...",false,false);
//        downloadAndInstallApk();
//        }
//        });
//        builder.setNegativeButton("Later",new DialogInterface.OnClickListener(){
//@Override
//public void onClick(DialogInterface dialogInterface,int i){
//
//        }
//        });
//        builder.show();
//        }else{
//
//        }
//        }
//        },new Response.ErrorListener(){
//@Override
//public void onErrorResponse(VolleyError error){
//        if(error instanceof TimeoutError){
//        Toast.makeText(LoginActivity.this,"Connection Timeout",Toast.LENGTH_SHORT).show();
//        }else{
//        Toast.makeText(LoginActivity.this,"No network connection : Offline Mode",Toast.LENGTH_SHORT).show();
//        }
//        }
//        });
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//        320000,
//        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        RequestQueue mRequestQueue=Volley.newRequestQueue(getApplicationContext(),new HurlStack());
//        mRequestQueue.add(stringRequest);
//        }
//}
