package com.asus.jack_tsai.jackmoney;

import android.content.Context;
import android.support.v7.preference.EditTextPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Jack_Tsai on 2016/5/24.
 */
public class EditIntegerPreference extends EditTextPreference {
    public static final int DEFAULT_VALUE = 0;
    public static final String REGULAR_EXPRESSION = "^(0|[1-9][0-9]*)$";
    private Context mContext;

    public EditIntegerPreference(Context context) {
        super(context);
        mContext = context;
    }

    public EditIntegerPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mContext = context;
    }

    public EditIntegerPreference(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        mContext = context;
    }

    @Override
    public String getText() {
        Log.e("jackfunny", "EditIntegerPreference getText");
        return String.valueOf(getSharedPreferences().getInt(getKey(), DEFAULT_VALUE));
    }

    @Override
    public void setText(String text) {
        Log.e("jackfunny", "EditIntegerPreference setText");
        if (!text.matches(REGULAR_EXPRESSION)) {
            Toast.makeText(getContext(), mContext.getString(R.string.Toast_wrong_syntax), Toast.LENGTH_SHORT).show();
            return;
        }
        if (getSharedPreferences() != null) {
            getSharedPreferences().edit().putInt(getKey(), Integer.parseInt(text)).commit();
        }

    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        this.setText(this.getText());
    }


}
