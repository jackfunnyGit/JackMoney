package com.asus.jack_tsai.jackmoney;



import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;


public class HomeMoneyActivity extends AppCompatActivity implements HomeMoneyCalendarFragment.OnFragmentInteractionListener,HomeMoneyViewFragment.OnFragmentInteractionListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private  String mDate;
    private  TabFragmentPagerAdapter mPagerAdapter ;
    private ItemMoneyCursorAdapter mItemAdapter;
    static public GoogleApiClient mGoogleApiClient;
    private boolean mResolvingError = false;
    private static final int DIALOG_ERROR_CODE =100;
    public static final String LOADACTION="laod_action";
    public static final String UPLOADACTION="uplaod_action";
    public static final String DOWNLOADACTION="downlaod_action";
    public static final String CSV_FILE_NAME = "JackMoney.csv";
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_download:
                //TODO download

                if (mGoogleApiClient.isConnected()) {
                    Intent intent = new Intent(this,BackupService.class);
                    intent.putExtra(LOADACTION, DOWNLOADACTION);
                    startService(intent);
                }

                return true;
            case R.id.action_upload:
                //TODO upload

                if (mGoogleApiClient.isConnected()) {
                    Intent intent = new Intent(this,BackupService.class);
                    intent.putExtra(LOADACTION, UPLOADACTION);
                    startService(intent);
                }

                return true;
            case R.id.action_settings:
                //TODO setting
                Log.e("jackfunny","onOptionsItemSelected  R.id.action_settings ...");
                if (getSupportFragmentManager().findFragmentByTag("setting_prefFragment")==null){
                    Log.e("jackfunny","R.id.action_settings FindFragmentByTag(setting_prefFragment)==null !!!!");
                    SettingPrefFragment settingPrefFragment = new SettingPrefFragment();
                    getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_fragment, settingPrefFragment, "setting_prefFragment")
                        .addToBackStack(null)
                        .commit();
                }
                return true;
            case R.id.action_export:
                //TODO export
                Log.e("jackfunny", "onOptionsItemSelected  R.id.action_export ...");
                ExportAsyncTask exportAsyncTask = new ExportAsyncTask(this);
                exportAsyncTask.execute(CSV_FILE_NAME);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homemoney);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initToolbar();
        initTabFragment(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        if(!mResolvingError) {
            mGoogleApiClient.connect(); // Connect the client to Google Drive
        }

    }
    private void initToolbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("JackMoneyApp");
    }

    private void initTabFragment(Bundle savedInstanceState){
        if (savedInstanceState == null) {
            TabFragment tabFragment = new TabFragment();

            getSupportFragmentManager()
            .beginTransaction()
            .add(R.id.content_fragment, tabFragment)
                    .commit();

        }
    }
    public void onFragmentInteraction(Uri uri){
        //callback fucntion

    }
    public void setDate(String Date){mDate=Date;}
    public String getDate() {return mDate;}
    public void setActivityAdapter(TabFragmentPagerAdapter Adapter){mPagerAdapter=Adapter;}
    public TabFragmentPagerAdapter getActivityAdapter(){return mPagerAdapter;}
    public  void setCursorAdapter(ItemMoneyCursorAdapter itemMoneyCursorAdapter){mItemAdapter= itemMoneyCursorAdapter;}
    public ItemMoneyCursorAdapter getCursorAdapter(){return mItemAdapter;}

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.e("jackfunny", "Connection failed");
        if(mResolvingError) { // If already in resolution state, just return.
            return;
        } else if(result.hasResolution()) { // Error can be resolved by starting an intent with user interaction
            mResolvingError = true;
            try {
                result.startResolutionForResult(this, DIALOG_ERROR_CODE);

            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else { // Error cannot be resolved. Display Error Dialog stating the reason if possible.
            Toast.makeText(this, "Connection failed Error cannot be resolved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == DIALOG_ERROR_CODE) {
            mResolvingError = false;
            if(resultCode == RESULT_OK) { // Error was resolved, now connect to the client if not done so.
                if(!mGoogleApiClient.isConnecting() && !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.e("jackfunny", "Connected successfully");

        /* Connection to Google Drive established. Now request for Contents instance, which can be used to provide file contents.
           The callback is registered for the same. */

    }



    @Override
    public void onConnectionSuspended(int cause) {
        // TODO Auto-generated method stub
        Log.e("jackfunny", "Connection suspended");

    }




}
