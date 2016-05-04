package com.asus.jack_tsai.jackmoney;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MoneyAddActivity extends AppCompatActivity {
    EditText itemText,priceText,cateText,memoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        itemText=(EditText)findViewById(R.id.editText_itemName);
        priceText=(EditText)findViewById(R.id.editText_price);
        cateText=(EditText)findViewById(R.id.editText_Category);
        memoText=(EditText)findViewById(R.id.editText_Memo);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
   public void onOKpressed(View view){

       ContentValues values = new ContentValues();
       values.put(MoneyProvider.NAME,itemText.getText().toString());
       values.put(MoneyProvider.PRICE,priceText.getText().toString());
       values.put(MoneyProvider.CATEGORY,cateText.getText().toString());
       values.put(MoneyProvider.MEMO,memoText.getText().toString());
       values.put(MoneyProvider.DATE,"2016-5-4");
       Uri uri = getContentResolver().insert(MoneyProvider.CONTENT_URI, values);
       Toast.makeText(getBaseContext(),uri.toString(), Toast.LENGTH_LONG).show();
       Log.e("jackfunny",uri.toString());
   }

    public void onCancelpressed(View view) {
        // Retrieve student records
       // String URL = "content://com.asus.provider.Money/students";
        Uri Uri_Table1 = Uri.parse(MoneyProvider.URL);
        String[] projection = new String[]{MoneyProvider.NAME,MoneyProvider.PRICE,MoneyProvider._ID};
        Cursor c =  getContentResolver().query(Uri_Table1, projection, null, null, "name");
        Log.e("jackfunny",""+c.getColumnIndex(MoneyProvider._ID));
        Log.e("jackfunny",""+c.getColumnIndex(MoneyProvider.NAME));
        Log.e("jackfunny",""+c.getColumnIndex(MoneyProvider.PRICE));
        if (c.moveToFirst()) {
            do{

                Log.e("jackfunny",c.getString(c.getColumnIndex(MoneyProvider._ID)) +
                        ", " +  c.getString(c.getColumnIndex( MoneyProvider.NAME)) +
                        ", " + c.getString(c.getColumnIndex( MoneyProvider.PRICE)));
            } while (c.moveToNext());
        }
    }

}
