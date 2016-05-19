package com.asus.jack_tsai.jackmoney;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;


public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

    ArrayList<HomeMoneyBaseFragment> fragments = null;

    public TabFragmentPagerAdapter(FragmentManager fm, @NonNull ArrayList<HomeMoneyBaseFragment> fragments) {
        super(fm);

            this.fragments = fragments;

    }

    @Override
    public HomeMoneyBaseFragment getItem(int position) {
        Log.e("jackfunny","getItem position="+position);
        //run once with the fragment created;
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        //Log.e("jackfunny","getCount "+fragments.size());
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getTitle();
    }


    public int getIconResId(int position) {
        return fragments.get(position).getIconResId();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //tagList.add(makeFragmentName(container.getId(), getItemId(position)));
        Log.e("jackfunny","instantiateItem position="+position);
        return super.instantiateItem(container, position);
    }
    @Override public int getItemPosition(Object object) {
        Log.e("jackfunny", "getItemPosition() ");
        if (object instanceof HomeMoneyViewFragment) {
            ((HomeMoneyViewFragment) object).updateListView();

        }
        return super.getItemPosition(object);
    }
}
