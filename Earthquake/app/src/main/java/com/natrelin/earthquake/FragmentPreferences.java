package com.natrelin.earthquake;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import java.util.List;

/**
 * Created by mukondono on 10/18/15.
 */
public class FragmentPreferences extends PreferenceActivity {

    /*CheckBox autoUpdate;
    Spinner updateFreqSpinner;
    Spinner magnitudeSpinner;*/

    /*public static final String USER_PREFERENCE = "USER_PREFERENCE";
    public static final String PREF_AUTO_UPDATE = "PREF_AUTO_UPDATE";
    public static final String PREF_MIN_MAG_INDEX = "PREF_MIN_MAG_INDEX";
    public static final String PREF_UPDATE_FREQ_INDEX = "PREF_UPDATE_FREQ_INDEX";
    public static final String PREF_MIN_MAG = "PREF_MIN_MAG";
    public static final String PREF_UPDATE_FREQ = "PREF_UPDATE_FREQ";
    SharedPreferences prefs;*/

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }

    @Override
    protected boolean isValidFragment (String fragmentName) {
        return PreferencesActivity.class.getName().equals(fragmentName) ||
                FragmentPreferences.class.getName().equals(fragmentName) ||
                UserPreferenceFragment.class.getName().equals(fragmentName);
    }

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.preferences);
        addPreferencesFromResource(R.xml.userpreferences);

        *//*updateFreqSpinner = (Spinner)findViewById(R.id.spinner_update_freq);
        magnitudeSpinner = (Spinner)findViewById(R.id.spinner_quake_mag);
        autoUpdate = (CheckBox)findViewById(R.id.checkbox_auto_update);
        populateSpinners();*//*

        Context context = getApplicationContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        //updateUIFromPreferences();

        *//*Button okButton = (Button) findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //savePreferences();
                PreferencesActivity.this.setResult(RESULT_OK);
                finish();
            }
        });

        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesActivity.this.setResult(RESULT_CANCELED);
                finish();
            }
        });*//*
    }*/

    /*private void savePreferences() {
        int updateIndex = updateFreqSpinner.getSelectedItemPosition();
        int minMagIndex = magnitudeSpinner.getSelectedItemPosition();
        boolean autoUpdateChecked = autoUpdate.isChecked();
        Editor editor = prefs.edit();
        editor.putBoolean(PREF_AUTO_UPDATE, autoUpdateChecked);
        editor.putInt(PREF_UPDATE_FREQ_INDEX, updateIndex);
        editor.putInt(PREF_MIN_MAG_INDEX, minMagIndex);
        editor.commit();
    }

    public void updateUIFromPreferences() {
        boolean autoUpChecked = prefs.getBoolean(PREF_AUTO_UPDATE, false);
        int updateFreqIndex = prefs.getInt(PREF_UPDATE_FREQ_INDEX, 2);
        int minMagIndex = prefs.getInt(PREF_MIN_MAG_INDEX, 0);
        updateFreqSpinner.setSelection(updateFreqIndex);
        magnitudeSpinner.setSelection(minMagIndex);
        autoUpdate.setChecked(autoUpChecked);
    }

    private void populateSpinners() {
        ArrayAdapter<CharSequence> fAdapter;
        fAdapter = ArrayAdapter.createFromResource(
                this, R.array.update_freq_options, android.R.layout.simple_spinner_item);
        int spinner_dd_item = android.R.layout.simple_spinner_dropdown_item;
        fAdapter.setDropDownViewResource(spinner_dd_item);
        updateFreqSpinner.setAdapter(fAdapter);

        ArrayAdapter <CharSequence> mAdapter;
        mAdapter = ArrayAdapter.createFromResource(
                this, R.array.magnitude_options, android.R.layout.simple_spinner_item);
        mAdapter.setDropDownViewResource(spinner_dd_item); magnitudeSpinner.setAdapter(mAdapter);
    }*/
}
