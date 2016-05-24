package com.asus.jack_tsai.jackmoney;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
    //member field
    private  int mActionflag;
    private EditText mItemText,mPriceText,mCateText,mMemoText;
    private TextView mDateText;
    private String mDate;


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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }


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
        }
        else {Log.e("jackfunny","MoneyAddActivity receive a wrong action!!");}
    }
   public void onOKpressed(View view){

       ContentValues values = new ContentValues();
       values.put(MoneyProvider.NAME,mItemText.getText().toString());
       values.put(MoneyProvider.PRICE,mPriceText.getText().toString());
       values.put(MoneyProvider.CATEGORY,mCateText.getText().toString());
       values.put(MoneyProvider.MEMO, mMemoText.getText().toString());
       values.put(MoneyProvider.DATE, mDate);
       if (mActionflag==ACTION_ADD) {
           Uri uri = getContentResolver().insert(MoneyProvider.CONTENT_URI, values);
           Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
           Log.e("jackfunny", "MoneyAddActivity : insert into uri=" + uri.toString());
       }
       else if (mActionflag==ACTION_EDIT){

           Uri uri_ItemPos =Uri.parse(MoneyProvider.URL + "/" + getIntent().getExtras().getInt(_ID));
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
            Bitmap mbmp = (Bitmap) data.getExtras().get("data");
            ImageView img =(ImageView ) findViewById(R.id.imagePic);
            img.setImageBitmap(mbmp);
            String imgUri = MediaStore.Images.Media.insertImage(getContentResolver(), mbmp, null, null);
            Log.e("jackfunny", "imgUri  = " + imgUri );
            Log.e("jackfunny", "imgUri  = " + getFilePathByContentResolver(this,Uri.parse(imgUri)) );

            /*
            try {
                // 取得外部儲存裝置路徑
                String path = Environment.getExternalStorageDirectory().toString();

                Log.e("jackfunny","extenal path = "+path+ " getFilesDir() = "+getFilesDir());
                //開啟file
                File file = new File( path, "JackMoney");
                if(!file.exists()){
                    file.mkdirs();
                }

                // 開啟檔案

                // 開啟檔案串流
                FileOutputStream out = new FileOutputStream(file);
                // 將 Bitmap壓縮成指定格式的圖片並寫入檔案串流
                if (mbmp != null) {
                    mbmp.compress ( Bitmap. CompressFormat.PNG , 90 , out);
                }
                // 刷新並關閉檔案串流
                out.flush ();
                out.close ();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace ();
            }
            */
            /*File out = new File(getFilesDir(), "newImage.jpg");
            Bitmap mBitmap = BitmapFactory.decodeFile(out.getAbsolutePath());
            img.setImageBitmap(mBitmap);
            */
        }
    }
    private  String getFilePathByContentResolver(Context context, Uri uri) {
        if (null == uri) {
            return null;
        }
        Cursor c = context.getContentResolver().query(uri, null, null, null, null);
        String filePath  = null;
        if (null == c) {
            throw new IllegalArgumentException(
                    "Query on " + uri + " returns null result.");
        }
        try {
            if ((c.getCount() != 1) || !c.moveToFirst()) {
            } else {
                filePath = c.getString(
                        c.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
            }
        } finally {
            c.close();
        }
        return filePath;
    }
}
