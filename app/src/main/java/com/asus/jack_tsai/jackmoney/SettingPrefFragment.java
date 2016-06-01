package com.asus.jack_tsai.jackmoney;

/**
 * Created by Jack_Tsai on 2016/4/29.
 */


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;


import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

public class SettingPrefFragment extends PreferenceFragmentCompat {
    public static final String PREFERENCE_EDITTEXT_DAYBUDGET_KEY = "Preference_EditText_Daybudget";
    public static final String PREFERENCE_EDITTEXT_MONTHBUDGET_KEY = "Preference_EditText_Monthbudget";
    public static final String PREFERENCE_EDITTEXT_USERNAME_KEY = "Preference_EditText_Username";
    public static final String PREFERENCE_CHECKBOX_KEY = "Preference_checkbox";
    public static final int PREFERENCE_EDITTEXT_DFAULTVALUE = 0;
    public static final String PREFERENCE_EDITTEXT_USERNAME_DFAULTTEXT = "Username";
    public static final boolean PREFERENCE_CHECKBOX_DEFAULT_VALUE = false;
    public static final String REGULAR_EXPRESSION = "^(0|[1-9][0-9]*)$";
    //Menber field
    private SharedPreferences mSp;
    private settingcallback mListener;

    public interface settingcallback {
      void connectCallback();
      void disconnectCallback();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        initView();
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        Log.e("jackfunny", "PreferenceTreeClick");
        return super.onPreferenceTreeClick(preference);
    }

    public void initView() {
        mSp = getPreferenceManager().getSharedPreferences();
        setPreferenceBudget(PREFERENCE_EDITTEXT_DAYBUDGET_KEY);
        setPreferenceBudget(PREFERENCE_EDITTEXT_MONTHBUDGET_KEY);
        findPreference(PREFERENCE_EDITTEXT_USERNAME_KEY).setSummary(mSp.getString(PREFERENCE_EDITTEXT_USERNAME_KEY, mSp.getString(PREFERENCE_EDITTEXT_USERNAME_KEY, PREFERENCE_EDITTEXT_USERNAME_DFAULTTEXT)));
        findPreference(PREFERENCE_EDITTEXT_USERNAME_KEY).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.e("jackfunny", "onPreferenceChange USERNAME");
                preference.setSummary((String) newValue);
                return true;

            }

        });
        findPreference(PREFERENCE_CHECKBOX_KEY).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.e("jackfunny", "onPreferenceChange CHECKBOX");
                boolean Connection = (boolean) newValue;
                if (Connection) {
                    if (mListener != null) {
                        mListener.connectCallback();
                    }
                } else {
                    if (mListener != null) {
                        mListener.disconnectCallback();
                    }
                }
                return true;

            }

        });
    }

    private void setPreferenceBudget(String key) {
        EditIntegerPreference editTextPreference = (EditIntegerPreference) findPreference(key);
        editTextPreference.setSummary(String.format("$ %d", mSp.getInt(key, PREFERENCE_EDITTEXT_DFAULTVALUE)));
        editTextPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.e("jackfunny", "onPreferenceChange " + preference.getTitle());
                String summarytext = (String) newValue;
                if (summarytext.matches(REGULAR_EXPRESSION)) {
                    preference.setSummary(String.format("$ %s", newValue));
                }
                return true;

            }

        });

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("jackfunny", "HomeMoneyViewFragment  : onAttach" + getId());
        if (context instanceof settingcallback) {
            mListener = (settingcallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
}
