package com.asus.jack_tsai.jackmoney;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jack_Tsai on 2016/5/26.
 */
public class ImageLoadAsyncTask extends AsyncTask<Uri, Void, Boolean> {
    private static final int PARAMETER_INDEX_IMAGE_URI = 0;
    private Context mContext;
    private ImageView mImageview;
    private Bitmap mBitmap;

    public ImageLoadAsyncTask(Context context, ImageView imageview) {
        mImageview = imageview;
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Boolean doInBackground(Uri... params) {
        Uri uri = params[PARAMETER_INDEX_IMAGE_URI];
        Cursor cursor = mContext.getContentResolver().query(uri, new String[]{MoneyProvider
                .IMAGE_DATA_PATH}, null, null, null);
        String imagepath = null;
        if (cursor != null && cursor.moveToFirst())
            imagepath = cursor.getString(0);

        if (!TextUtils.isEmpty(imagepath)) {
            try {
                InputStream imageIn = mContext.getContentResolver().openInputStream(uri);
                mBitmap = BitmapFactory.decodeStream(imageIn);
                if (imageIn != null) {
                    imageIn.close();
                }
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }

        }
        return false;

    }

    @Override
    protected void onPostExecute(Boolean Boolean) {
        super.onPostExecute(Boolean);
        if (mBitmap != null && Boolean) {
            mImageview.setImageBitmap(mBitmap);
        } else mImageview.setImageResource(R.drawable.eating);
    }

}