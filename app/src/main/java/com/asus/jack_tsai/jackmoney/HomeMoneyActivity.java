package com.asus.jack_tsai.jackmoney;


import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;


public class HomeMoneyActivity extends AppCompatActivity implements HomeMoneyCalendarFragment.OnFragmentInteractionListener, HomeMoneyViewFragment.OnFragmentInteractionListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, SettingPrefFragment.settingcallback {
    static public GoogleApiClient mGoogleApiClient;
    private String mDate;
    private ItemMoneyCursorAdapter mItemAdapter;
    private boolean mResolvingError = false;
    private static final int DIALOG_ERROR_CODE = 100;
    public static final String UPLOADACTION = "com.asus.jack_tsai.jackmoney.uplaod_action";
    public static final String DOWNLOADACTION = "com.asus.jack_tsai.jackmoney.downlaod_action";
    public static final String CSV_FILE_NAME_EXTENSION = ".csv";
    public static final String SETTING_FRAGMENT_TAG = "setting_prefFragment";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_download:
                //TODO download
                Log.e("jackfunny", "onOptionsItemSelected  mainThread = " + Thread.currentThread().getId());
                if (mGoogleApiClient.isConnected()) {
                    Intent intent = new Intent(this, BackupService.class);
                    intent.setAction(DOWNLOADACTION);
                    startService(intent);
                }
                return true;
            case R.id.action_upload:
                //TODO upload
                if (mGoogleApiClient.isConnected()) {
                    Intent intent = new Intent(this, BackupService.class);
                    intent.setAction(UPLOADACTION);
                    startService(intent);
                }
                return true;
            case R.id.action_settings:
                //TODO setting
                Log.e("jackfunny", "onOptionsItemSelected  R.id.action_settings ...");
                if (getSupportFragmentManager().findFragmentByTag(SETTING_FRAGMENT_TAG) == null) {
                    Log.e("jackfunny", "R.id.action_settings FindFragmentByTag(setting_prefFragment)==null !!!!");
                    SettingPrefFragment settingPrefFragment = new SettingPrefFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_fragment, settingPrefFragment, SETTING_FRAGMENT_TAG)
                            .addToBackStack(null)
                            .commit();
                }
                return true;
            case R.id.action_export:
                //TODO export
                Log.e("jackfunny", "onOptionsItemSelected  R.id.action_export ...");
                ExportAsyncTask exportAsyncTask = new ExportAsyncTask(this);
                String csvFileName = PreferenceManager.getDefaultSharedPreferences(this).getString(SettingPrefFragment.PREFERENCE_EDITTEXT_USERNAME_KEY, SettingPrefFragment.PREFERENCE_EDITTEXT_USERNAME_DFAULTTEXT);
                exportAsyncTask.execute(String.format("%s%s", csvFileName, CSV_FILE_NAME_EXTENSION));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homemoney);
        initToolbar();
        initTabFragment(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        boolean connection = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SettingPrefFragment.PREFERENCE_CHECKBOX_KEY, SettingPrefFragment.PREFERENCE_CHECKBOX_DEFAULT_VALUE);
        if (connection) {
            if (!mResolvingError) {
                mGoogleApiClient.connect(); // Connect the client to Google Drive
            }
        } else {
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }
        }

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.app_name));
        }
    }

    private void initTabFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            TabFragment tabFragment = new TabFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content_fragment, tabFragment)
                    .commit();

        }
    }

    @Override
    public void onSetDate(String date) {
        //callback function
        mDate = date;
    }

    @Override
    public String onGetDate() {
        //callback function
        return mDate;
    }

    @Override
    public void onSetAdapter(ItemMoneyCursorAdapter ItemAdapter) {
        //callback function
        mItemAdapter = ItemAdapter;
    }

    @Override
    public ItemMoneyCursorAdapter ongetCursorAdapter() {
        //callback function
        return mItemAdapter;
    }

    @Override
    public void connectCallback() {
        if (!mResolvingError) {
            mGoogleApiClient.connect(); // Connect the client to Google Drive
        }

    }

    @Override
    public void disconnectCallback() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
            Toast.makeText(this, getString(R.string.Toast_drive_disconnect), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.e("jackfunny", "Connection failed");
        if (mResolvingError) { // If already in resolution state, just return.
            return;
        } else if (result.hasResolution()) { // Error can be resolved by starting an intent with user interaction
            mResolvingError = true;
            try {
                result.startResolutionForResult(this, DIALOG_ERROR_CODE);

            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else { // Error cannot be resolved. Display Error Dialog stating the reason if possible.
            Toast.makeText(this, getString(R.string.Toast_connection_failed), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DIALOG_ERROR_CODE) {
            mResolvingError = false;
            if (resultCode == RESULT_OK) { // Error was resolved, now connect to the client if not done so.
                if (!mGoogleApiClient.isConnecting() && !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.e("jackfunny", "Connected successfully");
        Toast.makeText(this, getString(R.string.Toast_drive_connect_success), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onConnectionSuspended(int cause) {
        Log.e("jackfunny", "Connection suspended");
        Toast.makeText(this, getString(R.string.Toast_drive_connect_suspended), Toast.LENGTH_SHORT).show();
    }


}
