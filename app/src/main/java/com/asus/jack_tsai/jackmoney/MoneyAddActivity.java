package com.asus.jack_tsai.jackmoney;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
        mItemText=(EditText)findViewById(R.id.editText_itemName);
        mPriceText=(EditText)findViewById(R.id.editText_price);
        mCateText=(EditText)findViewById(R.id.editText_Category);
        mMemoText=(EditText)findViewById(R.id.editText_Memo);
        mDateText=(TextView)findViewById(R.id.Date);

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
           Log.e("jackfunny", "MoneyAddActivity : insert into uri="+uri.toString());
       }
       else if (mActionflag==ACTION_EDIT){

           Uri uri_ItemPos =Uri.parse( MoneyProvider.URL+"/"+getIntent().getExtras().getInt(_ID));
           getContentResolver().update(uri_ItemPos,values, null,null);
           Toast.makeText(getBaseContext(), uri_ItemPos.toString(), Toast.LENGTH_LONG).show();
           Log.e("jackfunny", "update data "+uri_ItemPos.toString());
       }
       else {
           Log.e("jackfunny","MoneyAddActivity receive a wrong action!!");
       }
   }

    public void onCancelpressed(View view) {
        finish();
    }

    public void OpenCamera(View view){
        Log.e("jackfunny", "OpenCamera ....");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap mbmp = (Bitmap) data.getExtras().get("data");
            ImageView img =(ImageView ) findViewById(R.id.imagePic);
            img.setImageBitmap(mbmp);
            try {
                // 取得外部儲存裝置路徑
                String path = Environment.getExternalStorageDirectory().toString();
                Log.e("jackfunny","extenal path = "+path);
                // 開啟檔案
                File file = new File( path, "Image.png");
                // 開啟檔案串流
                FileOutputStream out = new FileOutputStream(file);
                // 將 Bitmap壓縮成指定格式的圖片並寫入檔案串流
                if (mbmp != null) {
                    mbmp.compress ( Bitmap. CompressFormat.PNG , 90 , out);
                }
                // 刷新並關閉檔案串流
                out.flush ();
                out.close ();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace ();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace ();
            }
        }
    }
}
