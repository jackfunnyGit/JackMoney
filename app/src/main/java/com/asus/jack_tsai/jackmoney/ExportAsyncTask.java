package com.asus.jack_tsai.jackmoney;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Jack_Tsai on 2016/5/18.
 */
public class ExportAsyncTask extends AsyncTask<String, Integer, Boolean> {

    private static final int PARAMETER_INDEX_FILE_NAME = 0;
    private static final int SLEEPTIME = 100;
    private static final int HUNDRED_PERCENT_COEFFICIENT = 100;
    private static final String COMMA_TEXT = ",";
    private Context mContext;
    private ProgressDialog mProgressDialog;

    public ExportAsyncTask(Context mContext) {
        this.mContext = mContext;
        mProgressDialog = new ProgressDialog(mContext);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e("jackfunny", "ExportAsyncTask :  onPreExecute  ");
        mProgressDialog.setMessage(mContext.getString(R.string.exoprt_csvfile));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.show();
    }


    @Override
    protected Boolean doInBackground(String... params) {
        String CSV_FILE_NAME = params[PARAMETER_INDEX_FILE_NAME];
        Cursor cursor = mContext.getContentResolver().query(Uri.parse(MoneyProvider.URL), null,
                null, null, MoneyProvider.DATE);
        if (cursor == null) {
            return false;
        }
        File sdCardDir = Environment.getExternalStorageDirectory();
        File saveFile = new File(sdCardDir, CSV_FILE_NAME);
        Log.e("jackfunny", "ExportAsyncTask doInBackground : saveFile path =  " + saveFile
                .toString());
        try {
            FileWriter fw = new FileWriter(saveFile);
            BufferedWriter bw = new BufferedWriter(fw);
            int rowcount = cursor.getCount();
            int colcount = cursor.getColumnCount();
            if (rowcount > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < colcount; i++) {
                    if (i != colcount - 1) {
                        bw.write(String.format("%s%s", cursor.getColumnName(i), COMMA_TEXT));
                    } else {
                        bw.write(cursor.getColumnName(i));
                    }
                }
                bw.newLine();
                for (int i = 0; i < rowcount; i++) {
                    cursor.moveToPosition(i);
                    for (int j = 0; j < colcount; j++) {
                        if (j != colcount - 1) {
                            bw.write(String.format("%s%s", cursor.getString(j), COMMA_TEXT));
                        } else {
                            bw.write(cursor.getString(j));
                        }
                    }
                    bw.newLine();
                    Thread.sleep(SLEEPTIME);
                    publishProgress(i * HUNDRED_PERCENT_COEFFICIENT / rowcount);
                }
                bw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return true;
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Log.e("jackfunny", "ExportAsyncTask :  onProgressUpdate nowloading " + values[0] + "%");
        mProgressDialog.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Boolean Boolean) {
        super.onPostExecute(Boolean);
        Log.e("jackfunny", "ExportAsyncTask :  onPreExecute(boolean)  ");
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
        if (!Boolean) {
            Toast.makeText(mContext, mContext.getString(R.string.exoprt_csvfile), Toast
                    .LENGTH_SHORT).show();
        }
    }
}
