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
    public static final String PREFERENCE_EDITTEXT_DAYBUDGET_KEY="Preference_EditText_Daybudget";
    public static final String PREFERENCE_EDITTEXT_MONTHBUDGET_KEY="Preference_EditText_Monthbudget";
    public static final String PREFERENCE_EDITTEXT_USERNAME_KEY="Preference_EditText_Username";
    public static final int PREFERENCE_EDITTEXT_DAYBUDGET_DFAULTVALUE=0;
    public static final int PREFERENCE_EDITTEXT_MONTHBUDGET_DFAULTVALUE=0;
    public static final String PREFERENCE_EDITTEXT_USERNAME_DFAULTTEXT="Username";
    //Menber field
    private SharedPreferences mSp;

    public static SettingPrefFragment newInstance() {
        Log.e("jackfunny ", "PreFragment1 newInstance() ");
        SettingPrefFragment fragment = new SettingPrefFragment();
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

     return super.onPreferenceTreeClick( preference);
 }

    public void initView(){
        mSp = getPreferenceManager().getSharedPreferences();
        int value=mSp.getInt(PREFERENCE_EDITTEXT_DAYBUDGET_KEY, PREFERENCE_EDITTEXT_DAYBUDGET_DFAULTVALUE);
        findPreference(PREFERENCE_EDITTEXT_DAYBUDGET_KEY).setSummary(String.format("$ %d", value));
        findPreference(PREFERENCE_EDITTEXT_DAYBUDGET_KEY).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.e("jackfunny", "onPreferenceChange Daybudget");
                String summarytext=(String)newValue;
                if (summarytext.matches("^(0|[1-9][0-9]*)$"))
                preference.setSummary(String.format("$ %s", newValue));
                return true;

            }

        });
        value=mSp.getInt(PREFERENCE_EDITTEXT_MONTHBUDGET_KEY, PREFERENCE_EDITTEXT_MONTHBUDGET_DFAULTVALUE);
        findPreference(PREFERENCE_EDITTEXT_MONTHBUDGET_KEY).setSummary(String.format("$ %d", value));
        findPreference(PREFERENCE_EDITTEXT_MONTHBUDGET_KEY).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.e("jackfunny", "onPreferenceChange Monthbudget");
                String summarytext=(String)newValue;
                if (summarytext.matches("^(0|[1-9][0-9]*)$"))
                preference.setSummary(String.format("$ %s", newValue));
                return true;

            }

        });
        String username=mSp.getString(PREFERENCE_EDITTEXT_USERNAME_KEY,PREFERENCE_EDITTEXT_USERNAME_DFAULTTEXT );
        findPreference(PREFERENCE_EDITTEXT_USERNAME_KEY).setSummary(mSp.getString(PREFERENCE_EDITTEXT_USERNAME_KEY,  username));
        findPreference(PREFERENCE_EDITTEXT_USERNAME_KEY).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.e("jackfunny", "onPreferenceChange USERNAME");
                preference.setSummary( (String) newValue);
                return true;

            }

        });
    }
}
