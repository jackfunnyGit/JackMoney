package com.asus.jack_tsai.jackmoney;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by Jack_Tsai on 2016/5/4.
 */
public class ItemMoneyCursorAdapter extends CursorAdapter {

    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public ItemMoneyCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mLayoutInflater.inflate(R.layout.dayview_item, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //Log.e("jackfunny","ItemMoneyCursorAdapter bindView");
        // Find fields to populate in inflated template
        TextView textViewName = (TextView) view.findViewById(R.id.item_Name);
        TextView textViewPrice = (TextView) view.findViewById(R.id.price);
        TextView textViewCategory = (TextView) view.findViewById(R.id.category);
        TextView textViewMemo = (TextView) view.findViewById(R.id.memo);
        TextView textViewDate = (TextView) view.findViewById(R.id.date);
        ImageView imageview = (ImageView) view.findViewById(R.id.item_picture);
        // Extract properties from cursor
        String name = cursor.getString(cursor.getColumnIndex(MoneyProvider.NAME));
        int price = cursor.getInt(cursor.getColumnIndex(MoneyProvider.PRICE));
        String category = cursor.getString(cursor.getColumnIndex(MoneyProvider.CATEGORY));
        String memo = cursor.getString(cursor.getColumnIndex(MoneyProvider.MEMO));
        String date = cursor.getString(cursor.getColumnIndex(MoneyProvider.DATE));
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(MoneyProvider._ID));

        // Populate fields with extracted properties
        textViewName.setText(name);
        textViewPrice.setText(String.format("%d", price));
        textViewCategory.setText(category);
        textViewMemo.setText(memo);
        textViewDate.setText(date);
        view.setTag(id);
        Uri uri_ItemPos = Uri.parse(String.format("%s/%d", MoneyProvider.URL, id));
        ImageLoadAsyncTask imageLoadAsyncTask = new ImageLoadAsyncTask(mContext, imageview);
        imageLoadAsyncTask.execute(uri_ItemPos);


    }

}
