package com.asus.jack_tsai.jackmoney;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;


public class DataChangeBroadcastReceiver extends BroadcastReceiver {
    public DataChangeBroadcastReceiver() {
        Log.e("jackfunny", "DataChangeBroadcastReceiver onCreate");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String URL = intent.getStringExtra(BackupService.BROADCAST_URL_KEY);
        Log.e("jackfunny", "DataChangeBroadcastReceiver : onReceive URL= " + URL);
        Uri uri = Uri.parse(URL);
        context.getContentResolver().notifyChange(uri, null);
    }
}
