package com.asus.jack_tsai.jackmoney;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Jack_Tsai on 2016/5/4.
 */
public class MyAdapter extends CursorAdapter {
    public MyAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.dayview_item, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //Log.e("jackfunny","MyAdapter bindView");
        // Find fields to populate in inflated template
        TextView tvName = (TextView) view.findViewById(R.id.Item_Name);
        TextView tvPrice = (TextView) view.findViewById(R.id.price);
        TextView tvCategory = (TextView) view.findViewById(R.id.Category);
        TextView tvMemo = (TextView) view.findViewById(R.id.Memo);
        TextView tvDate = (TextView) view.findViewById(R.id.Date);
        // Extract properties from cursor
        String name = cursor.getString(cursor.getColumnIndexOrThrow(MoneyProvider.NAME));
        int price = cursor.getInt(cursor.getColumnIndexOrThrow(MoneyProvider.PRICE));
        String category = cursor.getString(cursor.getColumnIndexOrThrow(MoneyProvider.CATEGORY));
        String memo = cursor.getString(cursor.getColumnIndexOrThrow(MoneyProvider.MEMO));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(MoneyProvider.DATE));
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(MoneyProvider._ID));
        // Populate fields with extracted properties
        tvName.setText(name);
        tvPrice.setText(""+price);
        tvCategory.setText(category);
        tvMemo.setText(memo);
        tvDate.setText(date);
        view.setTag(id);
    }

}
