package com.asus.jack_tsai.jackmoney;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class PictureFileContentProvider extends ContentProvider {
    public static final String PITURE_CONTENT="com.asus.jack_tsai.jackmoney.pictureProvider/";
    public static final Uri PITURE_CONTENT_URI=Uri.parse(PITURE_CONTENT);
    private static final HashMap<String, String> MIME_TYPES = new HashMap<>();

    static {
        MIME_TYPES.put(".jpg", "image/jpeg");
        MIME_TYPES.put(".jpeg", "image/jpeg");
    }
    public PictureFileContentProvider() {
    }
    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        Log.e("jackfunny","PictureFileContentProvider onCreate:");
        try {
            File mFile = new File(getContext().getFilesDir(), "newImage.jpg");
            if(!mFile.exists()) {
                mFile.createNewFile();
            }
            getContext().getContentResolver().notifyChange(PITURE_CONTENT_URI, null);
            return (true);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {

        Log.e("jackfunny","PictureFileContentProvider openFile: uri = "+uri+" mode = "+mode);
        File file = new File(getContext().getFilesDir(), "newImage.jpg");
        if (file.exists()) {
            Log.e("jackfunny","PictureFileContentProvider openFile: file-exists on file path = "+file.toString() );
            return (ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_WRITE));
        }
        throw new FileNotFoundException(uri.getPath());
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new RuntimeException("Operation not supported");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new RuntimeException("Operation not supported");
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        throw new RuntimeException("Operation not supported");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new RuntimeException("Operation not supported");
    }
    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        String path = uri.toString();
        for (String extension : MIME_TYPES.keySet()) {
            if (path.endsWith(extension)) {
                return (MIME_TYPES.get(extension));
            }
        }
        return (null);
    }
}
