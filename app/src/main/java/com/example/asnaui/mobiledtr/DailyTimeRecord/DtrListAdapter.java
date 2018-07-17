package com.example.asnaui.mobiledtr.DailyTimeRecord;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asnaui.mobiledtr.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Asnaui on 1/23/2018.
 */

public class DtrListAdapter extends BaseAdapter {
    ArrayList<DTRDate> dateList;
    Context context;
    LayoutInflater layoutInflater;

    public DtrListAdapter(ArrayList<DTRDate> dateList, Context context) {
        this.dateList = dateList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        Log.e("MOBSTAZ","DTRADAPTER");
    }

    @Override
    public int getCount() {
        return dateList.size();
    }

    @Override
    public Object getItem(int i) {
        return dateList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Handler handler;
        if (view == null) {
            handler = new Handler();
            view = layoutInflater.inflate(R.layout.dtr_item_template, null, true);
            handler.container = view.findViewById(R.id.time_logs);
            handler.date = view.findViewById(R.id.date);
            view.setTag(handler);
        } else {
            handler = (Handler) view.getTag();
            if(dateList.get(i).list.size() < handler.container.getChildCount()){
                handler.container.removeAllViews();
            }
        }
        handler.date.setText(dateList.get(i).date);
        for (int z = 0; z < dateList.get(i).list.size(); z++)
        {
            handler.container.addView(TimeLogs(dateList.get(i).list.get(z).time, dateList.get(i).list.get(z).status, dateList.get(i).list.get(z).filePath));
        }
        return view;
    }

    public View TimeLogs(String time, String status, String filePath) {
        View view = layoutInflater.inflate(R.layout.logs_item_template, null, false);
        TextView mTime = view.findViewById(R.id.time);
        TextView mStatus = view.findViewById(R.id.status);
        /*
        ImageView mImage = view.findViewById(R.id.image);

        Bitmap myBitmap = rorateImage(filePath);

        if (myBitmap != null) {
            mImage.setImageBitmap(myBitmap);
        }
        */
        mTime.setText(time);
        mStatus.setText(status);
        return view;
    }

    public Bitmap rorateImage(String image) {
        Bitmap bitmap = null;
        try {
            File file = new File(image);
            ExifInterface exif = new ExifInterface(file.getPath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            int angle = 0;

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                angle = 90;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                angle = 180;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                angle = 270;
            }

            Matrix mat = new Matrix();
            mat.postRotate(angle);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;

            Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(file),
                    null, options);
            bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                    bmp.getHeight(), mat, true);
            ByteArrayOutputStream outstudentstreamOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100,
                    outstudentstreamOutputStream);
            //imageView.setImageBitmap(bitmap);

        } catch (IOException e) {
            Log.w("TAG", "-- Error in setting image");
        } catch (OutOfMemoryError oom) {
            Log.w("TAG", "-- OOM Error in setting image");
        }
        return bitmap;
    }

    class Handler {
        LinearLayout container;
        TextView date;
    }

}
