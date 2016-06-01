package com.asus.jack_tsai.jackmoney;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragmentArray = null;
    private int mPageImageArray[] = null;
    private int mPageTitleArray[] = null;
    private int mSize;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public TabFragmentPagerAdapter(FragmentManager fm, Context context, @NonNull ArrayList<Fragment> fragmentArray, @NonNull int[] titleArray, @NonNull int[] imageArray) {
        super(fm);
        this.fragmentArray = fragmentArray;
        mSize = fragmentArray.size();
        mPageImageArray = imageArray;
        mPageTitleArray = titleArray;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }


    @Override
    public Fragment getItem(int position) {
        Log.e("jackfunny", "TabFragmentPagerAdapter getItem position=" + position);
        //run once with the fragment created;
        return fragmentArray.get(position);
    }

    @Override
    public int getCount() {
        Log.e("jackgetCount", "TabFragmentPagerAdapter getCount " + fragmentArray.size());
        return mSize;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        Log.e("jackfunny1", "TabFragmentPagerAdapter getPageTitle position=" + position);
        return mContext.getString(mPageTitleArray[position]);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.e("jackfunny1", "TabFragmentPagerAdapter instantiateItem position = " + position);
        return super.instantiateItem(container, position);
    }

    @Override
    public int getItemPosition(Object object) {
        Log.e("jackfunny1", "TabFragmentPagerAdapter getItemPosition() ");
        return super.getItemPosition(object);
    }

    public View getTabView(int position) {
        Log.e("jackfunny1", "TabFragmentPagerAdapter getTabView position=" + position);
        View view = mLayoutInflater.inflate(R.layout.view_tab_indicator, null);
        TextView textView = (TextView) view.findViewById(R.id.tabtext);
        textView.setText(mPageTitleArray[position]);
        ImageView imageView = (ImageView) view.findViewById(R.id.tabimage);
        imageView.setImageResource(mPageImageArray[position]);
        return view;
    }
}
