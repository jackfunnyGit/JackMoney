package com.asus.jack_tsai.jackmoney;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Jack_Tsai on 2016/4/25.
 */
public class MoneyProvider extends ContentProvider {


    static final String _ID = "_id";
    static final String NAME = "name";
    static final String PRICE = "price";
    static final String CATEGORY = "category";
    static final String MEMO ="memo";
    static final String DATE ="data";

    /**
     * Database specific constant declarations
     */
    private SQLiteDatabase db;
    static final String DATABASE_NAME = "Jack_Money";
    static final String Table1 = "Table_Money";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + Table1 +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                     + NAME +"  TEXT NOT NULL, "+
                      PRICE +"  TEXT NOT NULL, "+
                    CATEGORY +"  TEXT NOT NULL, "+
                    MEMO+","+
                    DATE+"  TEXT NOT NULL "+
                    "  );";

    static final String PROVIDER_NAME = "com.asus.jack_tsai.jackmoney.provider";
    static final String URL = "content://" + PROVIDER_NAME + "/"+Table1;
    static final Uri CONTENT_URI = Uri.parse(URL);



    private static HashMap<String, String> PROJECTION_MAP;

    static final int Table1_ALL = 1;
    static final int Table1_ID = 2;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, Table1, Table1_ALL);
        uriMatcher.addURI(PROVIDER_NAME, Table1+"/#", Table1_ID);
    }

    /*member field*/
    DatabaseHelper dbHelper;

    /**
     * Helper class that actually creates and manages
     * the provider's underlying data repository.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            Log.e("jackfunny","SQLiteOpenHelper OnCreate　　create_DB_TABLE= "+CREATE_DB_TABLE);

            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            Log.e("jackfunny","SQLiteOpenHelper OnUpgrade");
            db.execSQL("DROP TABLE IF EXISTS " +  Table1);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Log.e("jackfunny", "MoneyProvider Oncreate");
        Context context = getContext();
        dbHelper = new DatabaseHelper(context);


        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        Log.e("jackfunny", "MoneyProvider insert , uri="+uri.toString());
        db = dbHelper.getWritableDatabase();
        long rowID = db.insert(	Table1, "", values);
        /**
         * If record is added successfully
         */
        if (rowID > 0)
        {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
         throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.e("jackfunny", "MoneyProvider query , uri="+uri.toString());
        db = dbHelper.getWritableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(Table1);

        switch (uriMatcher.match(uri)) {
            case Table1_ALL:
                //project another name  for third developer  ex jackmoney.price =>price (which is actually in database collunm name)
                //qb.setProjectionMap(STUDENTS_PROJECTION_MAP);
                break;
            case Table1_ID:
                qb.appendWhere( _ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (sortOrder == null || sortOrder == ""){
            /**
             * By default sort on ID
             */
            sortOrder = _ID;
        }
        Cursor c = qb.query(db,	projection,	selection, selectionArgs,
                null, null, sortOrder);
        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count ;
        db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)){
            case Table1_ALL:
                count = db.delete(Table1, selection, selectionArgs);
                break;
            case Table1_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete( Table1, _ID +  " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count ;
        db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)){
            case Table1_ALL:
                count = db.update(Table1, values,
                        selection, selectionArgs);
                break;
            case Table1_ID:
                count = db.update(Table1, values, _ID +
                        " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            /**
             * Get all item records
             */
            case Table1_ALL:
                return "vnd.android.cursor.dir/vnd.asus.jack_tsai.Jack_Money";
            /**
             * Get a particular item
             */
            case Table1_ID:
                return "vnd.android.cursor.item/vnd.asus.jack_tsai.Jack_Money";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}
