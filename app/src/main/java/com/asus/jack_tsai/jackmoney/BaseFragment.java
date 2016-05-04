package com.asus.jack_tsai.jackmoney;

import android.graphics.Color;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {

    private String title = "";
    private int iconResId = 0;
    private int indicatorColor = Color.BLUE;
    private int dividerColor = Color.GRAY;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getIndicatorColor() {
        return indicatorColor;
    }
    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }
    public int getIconResId() {
        return iconResId;
    }
    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
    }
    public int getDividerColor() {
        return dividerColor;
    }
    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
    }

}