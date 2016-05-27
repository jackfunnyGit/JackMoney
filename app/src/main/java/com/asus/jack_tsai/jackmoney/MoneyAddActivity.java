package com.asus.jack_tsai.jackmoney;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
    //final string for others to use
    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String PRICE = "price";
    public static final String CATEGORY = "category";
    public static final String MEMO ="memo";
    public static final String DATE ="data";

    //action
    public static final String ACTION="action";
    public static final int ACTION_EDIT= 1;
    public static final int ACTION_ADD= 0;
    public static final int CAMERA_RESULT=0;
    public static final String FILENAME_EXTENSION=".jpg";
    //member field
    private  int mActionflag;
    private EditText mItemText,mPriceText,mCateText,mMemoText;
    private TextView mDateText;
    private ImageView mImageView;
    private String mDate;
    private  Bitmap mBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mItemText=(EditText)findViewById(R.id.edittext_itemname);
        mPriceText=(EditText)findViewById(R.id.edittext_price);
        mCateText=(EditText)findViewById(R.id.edittext_category);
        mMemoText=(EditText)findViewById(R.id.edittext_memo);
        mDateText=(TextView)findViewById(R.id.date);
        mImageView=(ImageView)findViewById(R.id.item_pic);


        Intent intent = getIntent();
        mActionflag= intent.getIntExtra(ACTION,0);
        if (mActionflag==ACTION_ADD) {
            Bundle bundle=intent.getExtras();
            mDate=bundle.getString(DATE);
            mDateText.setText(mDate);
        }
        else if(mActionflag==ACTION_EDIT) {
            Bundle bundle=intent.getExtras();
            mItemText.setText(bundle.getString(NAME));
            mPriceText.setText(bundle.getString(PRICE));
            mCateText.setText(bundle.getString(CATEGORY));
            mMemoText.setText(bundle.getString(MEMO));
            mDate=bundle.getString(DATE);
            mDateText.setText(mDate);
            Uri uri_ItemPos =Uri.parse(String.format("%s/%d", MoneyProvider.URL, getIntent().getExtras().getInt(_ID)));
            ImageLoadAsyncTask imageLoadAsyncTask =new ImageLoadAsyncTask(this,mImageView);
            imageLoadAsyncTask.execute(uri_ItemPos);
        }
        else {Log.e("jackfunny","MoneyAddActivity receive a wrong action!!");}
    }
   public void onOKpressed(View view){

       String FilePath= String.format("%s/%d%s", getFilesDir().toString(), System.currentTimeMillis(), FILENAME_EXTENSION);

       ContentValues values = new ContentValues();
       values.put(MoneyProvider.NAME,mItemText.getText().toString());
       values.put(MoneyProvider.PRICE,mPriceText.getText().toString());
       values.put(MoneyProvider.CATEGORY,mCateText.getText().toString());
       values.put(MoneyProvider.MEMO, mMemoText.getText().toString());
       values.put(MoneyProvider.DATE, mDate);


       if (mActionflag==ACTION_ADD) {
           values.put(MoneyProvider.IMAGE_DATA_PATH, mBitmap != null?FilePath:"");
           Uri uri_ItemPos = getContentResolver().insert(MoneyProvider.CONTENT_URI, values);
           //if user take a picture ,mBitmap will not be null
           if (mBitmap!=null)
           bitmapSavetoFile(mBitmap,uri_ItemPos);

           Toast.makeText(getBaseContext(), uri_ItemPos.toString(), Toast.LENGTH_LONG).show();
           Log.e("jackfunny", "MoneyAddActivity : insert into uri=" + uri_ItemPos.toString());
           Log.e("jackfunny", "MoneyAddActivity : insert into filePath=" + FilePath);
       }
       else if (mActionflag==ACTION_EDIT){
           Uri uri_ItemPos = Uri.parse(String.format("%s/%d", MoneyProvider.URL, getIntent().getExtras().getInt(_ID)));
           //if user take a picture ,mBitmap will not be null
           if (mBitmap!=null)
               bitmapSavetoFile(mBitmap,uri_ItemPos);

           getContentResolver().update(uri_ItemPos, values, null, null);
           Toast.makeText(getBaseContext(), uri_ItemPos.toString(), Toast.LENGTH_LONG).show();
           Log.e("jackfunny", "update data " + uri_ItemPos.toString());
           finish();
       }
       else {
           Log.e("jackfunny","MoneyAddActivity receive a wrong action!!");
       }
   }

    public void onCancelpressed(View view) {
        finish();
    }

    public void OpenCamera(View view){
        Log.e("jackfunny", "OpenCamera .......");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        //intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri );

        startActivityForResult(intent, CAMERA_RESULT);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == CAMERA_RESULT) {
            mBitmap = (Bitmap) data.getExtras().get("data");
            ImageView img =(ImageView ) findViewById(R.id.item_pic);
            img.setImageBitmap(mBitmap);


        }
    }
    private void bitmapSavetoFile(Bitmap bitmap,Uri uri){
        try {

                OutputStream imageOut = getContentResolver().openOutputStream(uri);
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 50, imageOut);
                if (imageOut != null) {
                    imageOut.close();

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace ();
        }

    }

}
