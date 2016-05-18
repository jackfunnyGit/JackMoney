package com.asus.jack_tsai.jackmoney;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.LinkedList;

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

    LinkedList<BaseFragment> fragments = null;

    public TabFragmentPagerAdapter(FragmentManager fm, LinkedList<BaseFragment> fragments) {
        super(fm);
        if (fragments == null) {
            this.fragments = new LinkedList<BaseFragment>();
        }else{
            this.fragments = fragments;
        }
    }

    @Override
    public BaseFragment getItem(int position) {
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
        if (object instanceof BlankFragment2) {
            ((BlankFragment2) object).updateListView();

        }
        return super.getItemPosition(object);
    }
}
