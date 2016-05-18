package com.asus.jack_tsai.jackmoney;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.net.URI;

public class DataChangeReceiver extends BroadcastReceiver {
    public DataChangeReceiver() {
        Log.e("jackfunny","DataChangeReceiver onCreate");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        String URL =  intent.getStringExtra(BackupService.BROADCAST_URI_KEY);
        Log.e("jackfunny","DataChangeReceiver : onReceive URL= "+URL);
        Uri uri = Uri.parse(URL);
        context.getContentResolver().notifyChange(uri,null);
        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
