package com.asus.jack_tsai.jackmoney;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Jack_Tsai on 2016/4/21.
 */
public class TabFragment extends Fragment {


    public void onCreate(Bundle savedInstanceState) {
        Log.e("jackfunny", "TabFragment onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        Log.e("jackfunny", "TabFragment onCreateView");
        return inflater.inflate(R.layout.frg_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //Adapter
        Log.e("jackfunny", "TabFragment onViewCreated");
        ArrayList<Fragment> fragmentArray = getFragments();
        int images[] = {
                R.drawable.tab_home,
                R.drawable.tab_statistics,
        };
        int title[] = {
                R.string.tab_home,
                R.string.tab_statistics,
        };
        TabFragmentPagerAdapter adapter = new TabFragmentPagerAdapter(getChildFragmentManager(),
                getContext(), fragmentArray, title, images);
        //Pager
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        viewPager.setAdapter(adapter);
        //Tabs
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(adapter.getTabView(i));
            }
        }

    }

    private ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeMoneyCalendarFragment());
        fragments.add(new HomeMoneyViewFragment());
        return fragments;
    }

}





