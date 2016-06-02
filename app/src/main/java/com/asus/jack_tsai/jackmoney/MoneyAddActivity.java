package com.asus.jack_tsai.jackmoney;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

public class MoneyAddActivity extends AppCompatActivity {


    public static final int ACTION_EDIT_FLAG = 1;
    public static final int ACTION_ADD_FLAG = 0;
    public static final int ACTION_WRONG_FLAG = -1;
    public static final int CAMERA_RESULT = 0;
    public static final String FILENAME_EXTENSION = ".jpg";
    //action
    private static final String ACTION_ADD = "com.asus.jack_tsai.jackmoney.ACTION_ADD";
    private static final String ACTION_EDIT = "com.asus.jack_tsai.jackmoney.ACTION_EDIT";
    //member field
    private int mActionflag = ACTION_WRONG_FLAG;
    private EditText mItemText, mPriceText, mCateText, mMemoText;
    private TextView mDateText;
    private ImageView mImageView;
    private String mDate;
    private Bitmap mBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_add);
        initToolbar();
        initView();
        Intent intent = getIntent();
        String action = intent.getAction();
        if (action.equals(ACTION_ADD)) {
            mActionflag = ACTION_ADD_FLAG;
        } else if (action.equals(ACTION_EDIT)) {
            mActionflag = ACTION_EDIT_FLAG;
        }

        if (mActionflag == ACTION_ADD_FLAG) {
            Bundle bundle = intent.getExtras();
            mDate = bundle.getString(MoneyProvider.DATE);
            mDateText.setText(mDate);
        } else if (mActionflag == ACTION_EDIT_FLAG) {
            Bundle bundle = intent.getExtras();
            mItemText.setText(bundle.getString(MoneyProvider.NAME));
            mPriceText.setText(bundle.getString(MoneyProvider.PRICE));
            mCateText.setText(bundle.getString(MoneyProvider.CATEGORY));
            mMemoText.setText(bundle.getString(MoneyProvider.MEMO));
            mDate = bundle.getString(MoneyProvider.DATE);
            mDateText.setText(mDate);
            Uri uri_ItemPos = Uri.parse(String.format("%s/%d", MoneyProvider.URL, getIntent()
                    .getExtras().getInt(MoneyProvider._ID)));
            ImageLoadAsyncTask imageLoadAsyncTask = new ImageLoadAsyncTask(this, mImageView);
            imageLoadAsyncTask.execute(uri_ItemPos);
        } else {
            Log.e("jackfunny", "MoneyAddActivity receive a wrong action!!");
        }
    }

    public void onOKpressed(View view) {
        String FilePath = String.format("%s/%d%s", getFilesDir().toString(), System
                .currentTimeMillis(), FILENAME_EXTENSION);
        ContentValues values = new ContentValues();
        values.put(MoneyProvider.NAME, mItemText.getText().toString());
        values.put(MoneyProvider.PRICE, mPriceText.getText().toString());
        values.put(MoneyProvider.CATEGORY, mCateText.getText().toString());
        values.put(MoneyProvider.MEMO, mMemoText.getText().toString());
        values.put(MoneyProvider.DATE, mDate);

        if (mActionflag == ACTION_ADD_FLAG) {
            //if user takes a picture ,save image path to DB.if not,save "" to DB
            values.put(MoneyProvider.IMAGE_DATA_PATH, mBitmap != null ? FilePath : "");
            Uri uri_ItemPos = getContentResolver().insert(MoneyProvider.CONTENT_URI, values);
            //if user takes a picture ,mBitmap will not be null
            if (mBitmap != null) {
                bitmapSavetoFile(mBitmap, uri_ItemPos);
            }
            Toast.makeText(getBaseContext(), uri_ItemPos.toString(), Toast.LENGTH_LONG).show();
            Log.e("jackfunny", "MoneyAddActivity : insert into uri=" + uri_ItemPos.toString());
            Log.e("jackfunny", "MoneyAddActivity : insert into filePath=" + FilePath);
            finish();
        } else if (mActionflag == ACTION_EDIT_FLAG) {
            Uri uri_ItemPos = Uri.parse(String.format("%s/%d", MoneyProvider.URL, getIntent()
                    .getExtras().getInt(MoneyProvider._ID)));
            //if user take a picture ,mBitmap will not be null
            if (mBitmap != null) {
                if (URI_ImageisEmpty(uri_ItemPos)) {
                    values.put(MoneyProvider.IMAGE_DATA_PATH, FilePath);
                    getContentResolver().update(uri_ItemPos, values, null, null);
                }
                bitmapSavetoFile(mBitmap, uri_ItemPos);

            } else {
                getContentResolver().update(uri_ItemPos, values, null, null);
            }
            Toast.makeText(getBaseContext(), uri_ItemPos.toString(), Toast.LENGTH_LONG).show();
            Log.e("jackfunny", "update data " + uri_ItemPos.toString());
            finish();
        } else {
            Log.e("jackfunny", "MoneyAddActivity receive a wrong action!!");
        }
    }

    public void onCancelpressed(View view) {
        finish();
    }

    public void OpenCamera(View view) {
        Log.e("jackfunny", "OpenCamera .......");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_RESULT);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CAMERA_RESULT) {
            mBitmap = (Bitmap) data.getExtras().get("data");
            mImageView.setImageBitmap(mBitmap);

        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void initView() {
        mItemText = (EditText) findViewById(R.id.edittext_itemname);
        mPriceText = (EditText) findViewById(R.id.edittext_price);
        mCateText = (EditText) findViewById(R.id.edittext_category);
        mMemoText = (EditText) findViewById(R.id.edittext_memo);
        mDateText = (TextView) findViewById(R.id.date);
        mImageView = (ImageView) findViewById(R.id.item_pic);

    }

    private void bitmapSavetoFile(Bitmap bitmap, Uri uri) {
        try {

            OutputStream imageOut = getContentResolver().openOutputStream(uri);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, imageOut);
            if (imageOut != null) {
                imageOut.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean URI_ImageisEmpty(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, new String[]{MoneyProvider
                .IMAGE_DATA_PATH}, null, null, null);
        if (cursor == null) {
            return true;
        }
        if (cursor.moveToFirst()) {
            String data = cursor.getString(0);
            cursor.close();
            return TextUtils.isEmpty(data);
        } else {
            cursor.close();
            return true;
        }

    }
}
