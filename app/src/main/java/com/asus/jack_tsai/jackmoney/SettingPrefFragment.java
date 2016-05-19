package com.asus.jack_tsai.jackmoney;

/**
 * Created by Jack_Tsai on 2016/4/29.
 */



import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import android.util.Log;

public class SettingPrefFragment extends PreferenceFragmentCompat {
    static final String PREFERENCE_EDITTEXT_DAYBUDGET="Preference_EditText_Daybudget";
    static final String PREFERENCE_EDITTEXT_MONTHBUDGET="Preference_EditText_Monthbudget";
    static final String PREFERENCE_EDITTEXT_USERNAME="Preference_EditText_Username";

    //Menber field
    private SharedPreferences mSp;

    public static SettingPrefFragment newInstance(String param1, String param2,int IndicatorColor,int DividerColor,int IconId) {
        Log.e("jackfunny ", "PreFragment1 newInstance() ");
        SettingPrefFragment fragment = new SettingPrefFragment();
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
    addPreferencesFromResource(R.xml.preferences);
    initView();

    }
 @Override
 public boolean onPreferenceTreeClick(Preference preference) {
     Log.e("jackfunny", "PreferenceTreeClick");
     mSp = getPreferenceManager().getSharedPreferences();

/*g
     if (preference.getKey().equals(PREFERENCE_EDITTEXT_USERNAME)) {
         EditTextPreference edittext_preference = (EditTextPreference) preference;
         edittext_preference.setSummary(mSp.getString(PREFERENCE_EDITTEXT_USERNAME,"username"));
     }
     else if (preference.getKey().equals(PREFERENCE_EDITTEXT_DAYBUDGET)) {
         EditTextPreference edittext_preference = (EditTextPreference) preference;
         edittext_preference.setSummary(mSp.getString(PREFERENCE_EDITTEXT_DAYBUDGET,""));
     }
     else if (preference.getKey().equals(PREFERENCE_EDITTEXT_MONTHBUDGET)) {
         EditTextPreference edittext_preference = (EditTextPreference) preference;
         edittext_preference.setSummary(mSp.getString(PREFERENCE_EDITTEXT_MONTHBUDGET ,""));
     }

     preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

         @Override
         public boolean onPreferenceChange(Preference preference, Object newValue) {
             Log.e("jackfunny", "onPreferenceChange");
             String key = preference.getKey();
             if (key.equals(PREFERENCE_EDITTEXT_USERNAME)) {
                 //动态改变summary的值
                 if (((String) newValue).trim().equals(""))
                     preference.setSummary(R.string.action_settings);
                 else
                     preference.setSummary((String) newValue);

             } else if (key.equals("listkey")) {
                 preference.setSummary(getResources().getString(R.string.action_settings) + newValue);
             }
             return true;

         }

     });
*/
     return super.onPreferenceTreeClick( preference);
 }
    public void initView(){
        mSp = getPreferenceManager().getSharedPreferences();
        findPreference(PREFERENCE_EDITTEXT_DAYBUDGET).setSummary("$ "+mSp.getString(PREFERENCE_EDITTEXT_DAYBUDGET,"Day Budget"));
        findPreference(PREFERENCE_EDITTEXT_DAYBUDGET).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.e("jackfunny", "onPreferenceChange Daybudget");
                preference.setSummary("$ " +  newValue);
                return true;

            }

        });
        findPreference(PREFERENCE_EDITTEXT_MONTHBUDGET).setSummary("$ " + mSp.getString(PREFERENCE_EDITTEXT_MONTHBUDGET, "Month Budget"));
        findPreference(PREFERENCE_EDITTEXT_MONTHBUDGET).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.e("jackfunny", "onPreferenceChange MONTHBUDGET");
                preference.setSummary("$ " +  newValue);
                return true;

            }

        });
        findPreference(PREFERENCE_EDITTEXT_USERNAME).setSummary(mSp.getString(PREFERENCE_EDITTEXT_USERNAME, "Username"));
        findPreference(PREFERENCE_EDITTEXT_USERNAME).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.e("jackfunny", "onPreferenceChange USERNAME");
                preference.setSummary( (String) newValue);
                return true;

            }

        });
    }
}
