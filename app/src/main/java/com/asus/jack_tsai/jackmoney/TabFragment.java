package com.asus.jack_tsai.jackmoney;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asus.jack_tsai.jackmoney.tabsliding.SlidingTabLayout;

import java.util.ArrayList;

/**
 * Created by Jack_Tsai on 2016/4/21.
 */
public  class  TabFragment extends Fragment {


    public static Fragment newInstance(){
        TabFragment f = new TabFragment();
        return f;
    }
    public void onCreate(Bundle savedInstanceState) {

        Log.e("jackfunny","TabFragment onCreate");
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("jackfunny","TabFragment onCreateView");
        return inflater.inflate(R.layout.frg_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //adapter
        Log.e("jackfunny","TabFragment onViewCreated");
        final ArrayList<HomeMoneyBaseFragment> fragments = getFragments();
        TabFragmentPagerAdapter adapter = new TabFragmentPagerAdapter(getChildFragmentManager(), fragments);
        //mPager
        ViewPager mPager = (ViewPager) view.findViewById(R.id.pager);
        mPager.setAdapter(adapter);

        //mTabs
        SlidingTabLayout mTabs = (SlidingTabLayout) view.findViewById(R.id.tabs);
        mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

            @Override
            public int getIndicatorColor(int position) {

                return fragments.get(position).getIndicatorColor();
            }

            @Override
            public int getDividerColor(int position) {
                return fragments.get(position).getDividerColor();
            }
        });


        mTabs.setBackgroundResource(R.color.pedo_actionbar_divider);
        mTabs.setCustomTabView(R.layout.view_tab_indicator, R.id.tabtext, R.id.tab_iv_image);
        mTabs.setViewPager(mPager);
    }

    private ArrayList<HomeMoneyBaseFragment> getFragments(){

        int indicatorColor =this.getResources().getColor(R.color.color_primary);
        int dividerColor = Color.TRANSPARENT;

        ArrayList<HomeMoneyBaseFragment> fragments = new ArrayList<>();
        fragments.add(HomeMoneyCalendarFragment.newInstance(getString(R.string.tab_home),  indicatorColor, dividerColor, R.drawable.tab_home));
        fragments.add(HomeMoneyViewFragment.newInstance(getString(R.string.tab_statistics), indicatorColor, dividerColor, R.drawable.tab_statistics));
        //fragments.add(BlankFragment3.newInstance("setting", "jackfunny", indicatorColor, dividerColor, R.drawable.tab_setting));
        return fragments;
    }




}





