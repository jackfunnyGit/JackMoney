package com.asus.jack_tsai.jackmoney;

/**
 * Created by Jack_Tsai on 2016/4/29.
 */



import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import android.util.Log;

public class PrefFragment extends PreferenceFragmentCompat {


    public static PrefFragment newInstance(String param1, String param2,int IndicatorColor,int DividerColor,int IconId) {
        Log.e("jackfunny ", "BlankFragment1 newInstance() ");
        PrefFragment fragment = new PrefFragment();
        Bundle args = new Bundle();
       /* fragment.setTitle(param1);
        fragment.setIndicatorColor(IndicatorColor);
        fragment.setDividerColor(DividerColor);
        fragment.setIconResId(IconId);
*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey){
        //TODO


    }
@Override
public void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
         super.onCreate(savedInstanceState);

              //从xml文件加载选项
             addPreferencesFromResource(R.xml.preferences);
    }
}
