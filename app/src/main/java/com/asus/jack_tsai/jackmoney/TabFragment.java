package com.asus.jack_tsai.jackmoney;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;

/**
 * Created by Jack_Tsai on 2016/4/21.
 */
public  class  TabFragment extends Fragment {

    private SlidingTabLayout tabs;
    private ViewPager pager;
    private TabFragmentPagerAdapter adapter;


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
         final LinkedList<BaseFragment> fragments = getFragments();
        adapter = new TabFragmentPagerAdapter(getChildFragmentManager(), fragments);
        ((SecondActivity)getActivity()).setActivityAdapter(adapter);
        //pager
        pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(adapter);

        //tabs
        tabs = (SlidingTabLayout) view.findViewById(R.id.tabs);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

            @Override
            public int getIndicatorColor(int position) {

                return fragments.get(position).getIndicatorColor();
            }

            @Override
            public int getDividerColor(int position) {
                return fragments.get(position).getDividerColor();
            }
        });


        tabs.setBackgroundResource(R.color.pedo_actionbar_divider);
        tabs.setCustomTabView(R.layout.view_tab_indicator, R.id.tabtext, R.id.tab_iv_image);
        tabs.setViewPager(pager);
    }

    private LinkedList<BaseFragment> getFragments(){
        int indicatorColor = Color.parseColor(this.getResources().getString(+R.color.colorAccent));
        int dividerColor = Color.TRANSPARENT;

        LinkedList<BaseFragment> fragments = new LinkedList<BaseFragment>();
        fragments.add(BlankFragment1.newInstance("home","jackfunny", indicatorColor, dividerColor,R.drawable.tab_home));
        fragments.add(BlankFragment2.newInstance("statistics","jackfunny", indicatorColor, dividerColor,R.drawable.tab_statistics));
        fragments.add(BlankFragment3.newInstance("setting", "jackfunny", indicatorColor, dividerColor, R.drawable.tab_setting));
        //fragments.add(GoodFragment.newInstance("Good", Color.BLUE, dividerColor));
       // fragments.add(LookFragment.newInstance("Look", Color.CYAN, dividerColor));
       // fragments.add(WoodFragment.newInstance("Wood", Color.MAGENTA, dividerColor));
        return fragments;
    }




}





