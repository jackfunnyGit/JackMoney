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
public class ExportAsyncTask extends AsyncTask<String, Integer , Boolean> {

    private Context context;
    private ProgressDialog progressDialog;


    public ExportAsyncTask(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e("jackfunny", "ExportAsyncTask :  onPreExecute  ");
        progressDialog.setMessage("正在匯出Excel檔");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
    }



    @Override
    protected Boolean doInBackground(String... params) {
        String CSV_FILE_NAME = params[0];
        Cursor cursor = context.getContentResolver().query(Uri.parse(MoneyProvider.URL), null, null, null, MoneyProvider.DATE);
        File sdCardDir = Environment.getExternalStorageDirectory();
        File saveFile = new File(sdCardDir, CSV_FILE_NAME);
        Log.e("jackfunny", "ExportAsyncTask doInBackground : saveFile path =  " + saveFile.toString());
        FileWriter fw = null;
        try {
            fw = new FileWriter(saveFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter bw = null;
        if (fw != null) {
            bw = new BufferedWriter(fw);
        }
        int rowcount = cursor.getCount();
        int colcount = cursor.getColumnCount();
        try {
            if (rowcount > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < colcount; i++) {

                    if (i != colcount - 1) {
                        bw.write(cursor.getColumnName(i) + ",");
                    } else {
                        bw.write(cursor.getColumnName(i));
                    }

                }
                bw.newLine();
                for (int i = 0; i < rowcount; i++) {
                    cursor.moveToPosition(i);
                    for (int j = 0; j < colcount; j++) {
                        if (j != colcount - 1)
                            bw.write(cursor.getString(j) + ",");
                        else
                            bw.write(cursor.getString(j));
                    }
                    bw.newLine();
                    Thread.sleep(100);

                    publishProgress((int) ((float) i/rowcount*100));

                }
                bw.flush();

            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        cursor.close();
    return true;
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Log.e("jackfunny", "ExportAsyncTask :  onProgressUpdate nowloading "+values[0]+"%");
        progressDialog.setProgress(values[0]);

    }
    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        Log.e("jackfunny", "ExportAsyncTask :  onPreExecute(boolean)  ");
        if(progressDialog.isShowing())
            progressDialog.dismiss();

        if(!aBoolean)
            Toast.makeText(context, "匯出失敗", Toast.LENGTH_SHORT).show();

    }
}
