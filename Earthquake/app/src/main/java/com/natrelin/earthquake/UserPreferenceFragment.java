package com.natrelin.earthquake;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by mukondono on 10/18/15.
 */
public class UserPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.userpreferences);
    }
}
