package com.asus.jack_tsai.jackmoney;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class BackupService extends Service {

    public static final String BROADCAST_ACTION = "com.asus.jack_tsai.jackmoney.Broadcast.DataChange";
    public static final String BROADCAST_URL_KEY = "url_key";
    public static final String UPLOADACTION = "com.asus.jack_tsai.jackmoney.uplaod_action";
    public static final String DOWNLOADACTION = "com.asus.jack_tsai.jackmoney.downlaod_action";
    private static final String GOOGLE_DRIVE_FILE_NAME = "JackMoney_backup.zip";
    private static final String BACKUP_FILE_ZIP_NAME="JackMoney.zip";
    private static final int  BUFFER_SIZE = 1024;
    //member field
    private GoogleApiClient mGoogleapiClient;
    private DriveFile mfile;

    public BackupService() {
    }

    @Override
    public void onCreate() {
        Log.e("jackfunny", "BackupService onCreate : ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("jackfunny", "BackupService Received start id " + startId + ": " + intent);
        Toast.makeText(this, String.format("%s%d%s%s", getString(R.string.Toast_BackupService_Received_start_id), startId, getString(R.string.Toast_BackupService_Received_ACTION), intent.getAction()), Toast.LENGTH_SHORT).show();
        //adjust in the future
        mGoogleapiClient = HomeMoneyActivity.mGoogleApiClient;

        String Action = intent.getAction();
        if (Action.equals(UPLOADACTION)) {
            UploadThread uploadThread = new UploadThread(startId);
            uploadThread.start();
        } else if (Action.equals(DOWNLOADACTION)) {
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
        Toast.makeText(this, getString(R.string.Toast_service_done), Toast.LENGTH_SHORT).show();
    }

    class DownloadThread extends Thread {

        //projected startID
        private int startId = 0;

        public DownloadThread(int startId) {
            this.startId = startId;

        }

        @Override
        public void run() {
            Log.e("jackfunny", "download Thread run(" + startId + ")...");
            Log.e("jackfunny", "downloading run thread = " + Thread.currentThread().getId());
            downloadDriveBackup();
            Log.e("jackfunny", "download done stopSelf(" + startId + ")");
            stopSelf(startId);
        }
    }

    class UploadThread extends Thread {

        //projected startID
        private int startId = 0;
        public UploadThread(int startId) {
            this.startId = startId;
        }

        @Override
        public void run() {
            Log.e("jackfunny", "upload Thread run(" + startId + ")");
            Log.e("jackfunnyZip", "upload Thread run ZipBackupFile...");
            ZipBackupFile();
            uploadDriveBackupFile();
            Log.e("jackfunny", "upload done and stopSelf(" + startId + ")");
            stopSelf(startId);
        }
    }

    private void uploadDriveBackupFile() {
        //find if the file exists
        Query query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.TITLE, GOOGLE_DRIVE_FILE_NAME))
                .build();

        Drive.DriveApi.query(mGoogleapiClient, query).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
            @Override
            public void onResult(DriveApi.MetadataBufferResult metadataBufferResult) {
                int count = metadataBufferResult.getMetadataBuffer().getCount();
                Log.e("jackfunny", "count = " + count);

                //if file exits
                if (count > 0) {
                    DriveId driveId = metadataBufferResult.getMetadataBuffer().get(0).getDriveId();
                    Log.e("jackfunny", "driveID = " + driveId);
                    Log.e("jackfunny", "filesize in cloud " + metadataBufferResult.getMetadataBuffer().get(0).getFileSize());

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
                else {
                    Drive.DriveApi.newDriveContents(mGoogleapiClient).setResultCallback(newcontentsCallback);
                }
                metadataBufferResult.getMetadataBuffer().release();
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
                FileInputStream is = new FileInputStream(new File(Environment.getExternalStorageDirectory(),BACKUP_FILE_ZIP_NAME));
                BufferedInputStream in = new BufferedInputStream(is);
                byte[] buffer = new byte[BUFFER_SIZE];
                DriveContents content = result.getDriveContents();
                BufferedOutputStream out = new BufferedOutputStream(content.getOutputStream());
                int n;
                while ((n = in.read(buffer)) > 0) {
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

    public void downloadDriveBackup() {
        Query query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.TITLE, GOOGLE_DRIVE_FILE_NAME))
                .build();
        Log.e("jackfunny", "downloading downloadDriveBackup thread = " + Thread.currentThread().getId());

        Drive.DriveApi.query(mGoogleapiClient, query).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
            @Override
            public void onResult(DriveApi.MetadataBufferResult metadataBufferResult) {

                Log.e("jackfunny", "downloading onResult thread = " + Thread.currentThread().getId());
                int count = metadataBufferResult.getMetadataBuffer().getCount();
                if (count<0){
                    metadataBufferResult.getMetadataBuffer().release();
                    return;
                }
                Log.e("jackfunny", "count = " + count);
                DriveId driveId = metadataBufferResult.getMetadataBuffer().get(0).getDriveId();
                Log.e("jackfunny", "driveID = " + driveId);
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
                        Log.e("jackfunny", "Unable to open file, try again.");
                        return;
                    }

                    File external_path = Environment.getExternalStorageDirectory();
                    File backup_zipFile = new File(external_path,BACKUP_FILE_ZIP_NAME);

                    DriveContents contents = result.getDriveContents();
                    try {
                        FileOutputStream fos = new FileOutputStream(backup_zipFile);
                        BufferedOutputStream bos = new BufferedOutputStream(fos);
                        BufferedInputStream in = new BufferedInputStream(contents.getInputStream());

                        byte[] buffer = new byte[BUFFER_SIZE];
                        int n, count = 0;
                        while ((n = in.read(buffer)) > 0) {
                            bos.write(buffer, 0, n);
                            count += n;
                            bos.flush();
                        }
                        Log.e("jackfunny", "total write to buffer ccount  = " + count);

                        bos.close();
                        unZipBackupFile();
                        Log.e("jackfunnyZip", "download Thread run upZipBackupFile...");

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //notify db change ----
                    Log.e("jackfunny", "downloading ResultCallback thread = " + Thread.currentThread().getId());

                    Intent intent = new Intent(BROADCAST_ACTION);
                    intent.putExtra(BROADCAST_URL_KEY, MoneyProvider.URL);
                    sendBroadcast(intent);
                    contents.discard(mGoogleapiClient); //because we just need reading the drivefile without changes so we use discard not commit

                }
            };

    private File getDbPath() {
        return this.getDatabasePath(MoneyProvider.DATABASE_NAME);
    }

    private  void ZipBackupFile() {
        try {
            File external_path = Environment.getExternalStorageDirectory();
            File backup_zipFile = new File(external_path,BACKUP_FILE_ZIP_NAME);
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(backup_zipFile));
            File sourceFile =getFilesDir();
            if(sourceFile.isDirectory()){
                File[] files = sourceFile.listFiles();
                for (File file1 : files) {
                    readFiletoZip(file1,zipOut);
                }
            }
            File DBFile =getDbPath();
            readFiletoZip(DBFile,zipOut);
            zipOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void readFiletoZip(File sourceFile,ZipOutputStream zipOut) throws IOException {
        InputStream input = new FileInputStream(sourceFile);
        zipOut.putNextEntry(new ZipEntry( sourceFile.getName()));
        Log.e("jackZip", "File = " +  sourceFile.getName());
        byte[] buffer = new byte[BUFFER_SIZE];
        int n;
        while ((n = input.read(buffer)) > 0) {
            zipOut.write(buffer,0,n);
            Log.e("jackZip","n= "+n);
        }
        input.close();

    }
    private  void unZipBackupFile(){
        try {
            File external_path = Environment.getExternalStorageDirectory();
            File backup_zipFile = new File(external_path,BACKUP_FILE_ZIP_NAME);
            ZipFile zipFile = new ZipFile(backup_zipFile);
            ZipInputStream zipInput = new ZipInputStream(new FileInputStream(backup_zipFile));
            ZipEntry entry ;
            while((entry = zipInput.getNextEntry()) != null){
                Log.e("jackZip" ,"unZip "+ entry.getName() );
                File outFile;
                if (entry.getName().equals(MoneyProvider.DATABASE_NAME)){
                    outFile =getDbPath();
                    Log.e("jackZip" ,"unZip file to"+ entry.getName() +" outFile ="+  outFile  );
                }
                else {
                    outFile = new File(String.format("%s%s%s", getFilesDir(), File.separator, entry.getName()));
                    Log.e("jackZip" ,"unZip file to"+ entry.getName() +" outFile ="+  outFile  );
                }

                if(!outFile.exists()){
                    outFile.createNewFile();
                }
                InputStream input = zipFile.getInputStream(entry);
                OutputStream output = new FileOutputStream(outFile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int n;
                while ((n = input.read(buffer)) > 0) {
                    output.write(buffer,0,n);
                }
                input.close();
                output.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
