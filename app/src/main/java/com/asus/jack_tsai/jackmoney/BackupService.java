package com.asus.jack_tsai.jackmoney;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class BackupService extends Service {
    private GoogleApiClient mGoogleapiClient;
    private DriveFile mfile;
    public static final String BROADCAST_ACTION = "com.asus.jack_tsai.jackmoney.Broadcast.DataChange";
    public static final String BROADCAST_URI_KEY="uri_key";
    private static final String GOOGLE_DRIVE_FILE_NAME = "JackMoney_db_backup";
    public BackupService() {
    }
    @Override
    public void onCreate() {
        Log.e("jackfunny", "BackupService onCreate : ");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("jackfunny", "BackupService Received start id " + startId + ": " + intent);
        Toast.makeText(this, "BackupService Received start id " + startId + ": " + intent, Toast.LENGTH_SHORT).show();
        //adjust in the future
        mGoogleapiClient=SecondActivity.mGoogleApiClient;
        //
        String Action=intent.getStringExtra(SecondActivity.LOADACTION);
        if (Action.equals(SecondActivity.UPLOADACTION)) {
            UploadThread uploadThread = new UploadThread(startId);
            uploadThread.start();
        }
        else if (Action.equals(SecondActivity.DOWNLOADACTION)){
            DownloadThread downloadThread = new DownloadThread(startId);
            downloadThread.start();
        }
        return START_NOT_STICKY;
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.e("jackfunny", "BackupServie onBind :");
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("jackfunny", "BackupServie onDestroy:");
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

    class DownloadThread extends Thread {

        //projected startID
        private int startId = 0;

        public DownloadThread(int startId){
            this.startId = startId;

        }

        @Override
        public void run() {
            Log.e("jackfunny", "download Thread run(" + startId + ")");
            //TODO
            restoreDriveBackup();
            for (int i=15;i>0;i--) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Log.e("jackfunny", "downloading remaining time = "+i);
            }
            Log.e("jackfunny", "download done stopSelf(" + startId + ")");
            stopSelf(startId);
        }
    }

    class UploadThread extends Thread {

        //projected startID
        private int startId = 0;

        public UploadThread(int startId){
            this.startId = startId;

        }

        @Override
        public void run() {
            Log.e("jackfunny", "upload Thread run(" + startId + ")");
            //TODO
            storeDriveBackup();


            Log.e("jackfunny", "upload done and stopSelf(" + startId + ")");
            stopSelf(startId);
        }
    }

    private void storeDriveBackup(){
        //find if the file exists
        Query query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.TITLE, GOOGLE_DRIVE_FILE_NAME))
                .build();

        Drive.DriveApi.query(mGoogleapiClient, query).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
            @Override
            public void onResult(DriveApi.MetadataBufferResult metadataBufferResult) {


                int count = metadataBufferResult.getMetadataBuffer().getCount() ;
                Log.e("jackfunny", "count = " + count);
                DriveId driveId = metadataBufferResult.getMetadataBuffer().get(0).getDriveId();
                Log.e("jackfunny", "driveID = " + driveId);
                //debug("driveId: " + driveId);
                Log.e("jackfunny", "filesize in cloud " + metadataBufferResult.getMetadataBuffer().get(0).getFileSize());
                metadataBufferResult.getMetadataBuffer().release();
                //if file exits
                if (count>0) {
                    mfile = driveId.asDriveFile();
                    mfile.open(mGoogleapiClient, DriveFile.MODE_WRITE_ONLY, new DriveFile.DownloadProgressListener() {
                        @Override
                        public void onProgress(long bytesDown, long bytesExpected) {
                            Log.e("jackfunny", "Downloading... (" + bytesDown + "/" + bytesExpected + ")");
                        }
                    })
                            .setResultCallback(contentsOpenedCallback);
                }
                // if file not exit create a new file
                else {Drive.DriveApi.newDriveContents(mGoogleapiClient).setResultCallback(newcontentsCallback);}
            }
        });



    }

    final private ResultCallback<DriveApi.DriveContentsResult> newcontentsCallback = new ResultCallback<DriveApi.DriveContentsResult>() {

        @Override
        public void onResult(DriveApi.DriveContentsResult result) {
            if (!result.getStatus().isSuccess()) {
                Log.e("jackfunny", "Error while trying to create new file contents");
                return;
            }
            //create metadata to attach file
            String mimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType("db");
            Log.e("jackfunny", "mimeType = " + mimeType);
            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                    .setTitle(GOOGLE_DRIVE_FILE_NAME) // Google Drive File name
                    .setMimeType(mimeType)
                    .setStarred(true).build();
            // create a file on root folder
            Drive.DriveApi.getRootFolder(mGoogleapiClient)
                    .createFile(mGoogleapiClient, changeSet, result.getDriveContents())
                    .setResultCallback(createfileCallback);
        }

    };

    final private ResultCallback<DriveFolder.DriveFileResult> createfileCallback = new ResultCallback<DriveFolder.DriveFileResult>() {

        @Override
        public void onResult(DriveFolder.DriveFileResult result) {
            if (!result.getStatus().isSuccess()) {
                Log.e("jackfunny", "Error while trying to create the file");
                return;
            }
            mfile = result.getDriveFile();
            mfile.open(mGoogleapiClient, DriveFile.MODE_WRITE_ONLY, null).setResultCallback(contentsOpenedCallback);
        }
    };



    final private ResultCallback<DriveApi.DriveContentsResult> contentsOpenedCallback = new ResultCallback<DriveApi.DriveContentsResult>() {

        @Override
        public void onResult(DriveApi.DriveContentsResult result) {

            if (!result.getStatus().isSuccess()) {
                Log.e("jackfunny", "Error opening file");
                return;
            }

            try {
                FileInputStream is = new FileInputStream(getDbPath());
                BufferedInputStream in = new BufferedInputStream(is);
                byte[] buffer = new byte[8 * 1024];
                DriveContents content = result.getDriveContents();
                BufferedOutputStream out = new BufferedOutputStream(content.getOutputStream());
                int n ;
                while( ( n = in.read(buffer) ) > 0 ) {
                    out.write(buffer, 0, n);
                }
                out.close();//remember to close and flush because output may go wrong with the last data
                in.close();
                content.commit(mGoogleapiClient, null).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status result) {
                        // Handle the response status
                    }
                });
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    };

    public  void restoreDriveBackup() {
        Query query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.TITLE, GOOGLE_DRIVE_FILE_NAME))
                .build();

        Drive.DriveApi.query(mGoogleapiClient, query).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
            @Override
            public void onResult(DriveApi.MetadataBufferResult metadataBufferResult) {


                int count = metadataBufferResult.getMetadataBuffer().getCount() ;
                Log.e("jackfunny", "count = " + count);
                DriveId driveId = metadataBufferResult.getMetadataBuffer().get(0).getDriveId();
                Log.e("jackfunny", "driveID = " + driveId);
                //debug("driveId: " + driveId);
                Log.e("jackfunny", "filesize in cloud " + metadataBufferResult.getMetadataBuffer().get(0).getFileSize());
                metadataBufferResult.getMetadataBuffer().release();


                mfile = driveId.asDriveFile();
                mfile.open(mGoogleapiClient, DriveFile.MODE_READ_ONLY, new DriveFile.DownloadProgressListener() {
                    @Override
                    public void onProgress(long bytesDown, long bytesExpected) {
                        Log.e("jackfunny", "Downloading... (" + bytesDown + "/" + bytesExpected + ")");
                    }
                })
                        .setResultCallback(restoreContentsOpenCallback);
            }
        });
    }

    final private ResultCallback<DriveApi.DriveContentsResult> restoreContentsOpenCallback =
            new ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Log.e("jackfunny","Unable to open file, try again.");
                        return;
                    }

                    //utilsM.dbClose();

                    File db_file = getDbPath();
                    String path = db_file.getPath();

                    if ( !db_file.exists())
                        db_file.delete();

                    db_file = new File(path);

                    DriveContents contents = result.getDriveContents();
                    //debug("driveId:(2)" + contents.getDriveId());

                    try {
                        FileOutputStream fos = new FileOutputStream(db_file);
                        BufferedOutputStream bos = new BufferedOutputStream(fos);
                        BufferedInputStream in = new BufferedInputStream(contents.getInputStream());

                        byte[] buffer = new byte[1024];
                        int n, cnt=0;


                        //debug("before read " + in.available());

                        while( ( n = in.read(buffer) ) > 0) {
                            bos.write(buffer, 0, n);
                            cnt += n;
                            /*
                            Log.e("jackfunny","buffer0: " + buffer[0]);
                            Log.e("jackfunny", "buffer1: " +buffer[1]);
                            Log.e("jackfunny", "buffer2: " +buffer[2]);
                            Log.e("jackfunny","buffer3: "  +buffer[3]);
                            */
                            bos.flush();
                        }

                        //debug(" read done: " + cnt);

                        bos.close();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // mToast(act.getResources().getString(R.string.restoreComplete));
                    // DialogFragment_Sync.dismissDialog();
                    //notify db change ----
                    //utilsM.dbOpen();
                    //mRecyclerView.invalidate();
                    //mAdapter.notifyDataSetChanged();
                    Intent intent = new Intent(BROADCAST_ACTION);
                    intent.putExtra(BROADCAST_URI_KEY, MoneyProvider.URL);
                    sendBroadcast(intent);
                    contents.discard(mGoogleapiClient); //because we just need reading the drivefile without changes so we use discard not commit

                }
            };

    private File getDbPath() {
        return this.getDatabasePath(MoneyProvider.DATABASE_NAME);
    }

}
