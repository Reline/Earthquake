package com.natrelin.earthquake;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class PreferencesActivity extends PreferenceActivity {

    public static final String PREF_AUTO_UPDATE = "PREF_AUTO_UPDATE";
    public static final String PREF_MIN_MAG = "PREF_MIN_MAG";
    public static final String PREF_UPDATE_FREQ = "PREF_UPDATE_FREQ";
    SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.userpreferences);

        Context context = getApplicationContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    protected boolean isValidFragment (String fragmentName) {
        return PreferencesActivity.class.getName().equals(fragmentName) ||
                FragmentPreferences.class.getName().equals(fragmentName) ||
                UserPreferenceFragment.class.getName().equals(fragmentName);
    }
}
