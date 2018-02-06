//package com.example.dell.popularmovies.activities;
//
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.support.v7.preference.CheckBoxPreference;
//import android.support.v7.preference.ListPreference;
//import android.support.v7.preference.PreferenceFragmentCompat;
//import android.support.v7.preference.PreferenceScreen;
//
//import android.preference.Preference;
//import android.preference.PreferenceActivity;
//import android.preference.PreferenceFragment;
//import android.support.annotation.Nullable;
//import android.support.v4.app.FragmentManager;
//
//import com.example.dell.popularmovies.R;
//
//public class SettingFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
//
//
//    @Override
//    public void onCreatePreferences(Bundle bundle, String s) {
//
//        addPreferencesFromResource(R.xml.prefrencess);
//        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
//        PreferenceScreen prefScreen = getPreferenceScreen();
//        int count = prefScreen.getPreferenceCount();
//
//        // Go through all of the preferences, and set up their preference summary.
////        for (int i = 0; i < count; i++) {
//            android.support.v7.preference.Preference p = prefScreen.getPreference(count);
//            // You don't need to set up preference summaries for checkbox preferences because
//            // they are already set up in xml using summaryOff and summary On
////            if (!(p instanceof CheckBoxPreference)) {
//                String value = sharedPreferences.getString(p.getKey(), "");
//                setPreferenceSummary(p, value);
////            }
////        }
//    }
//
//
//
//
//
//
//    @Override
//    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        android.support.v7.preference.Preference preference = findPreference(key);
//        if (null != preference) {
//            // Updates the summary for the preference
//            String value = sharedPreferences.getString(preference.getKey(), "");
//            setPreferenceSummary(preference, value);
//        }
//    }
//
//    private void setPreferenceSummary(android.support.v7.preference.Preference preference, String value) {
//        if (preference instanceof ListPreference) {
//            // For list preferences, figure out the label of the selected value
//            ListPreference listPreference = (ListPreference) preference;
//            int prefIndex = listPreference.findIndexOfValue(value);
//            if (prefIndex >= 0) {
//                // Set the summary to that label
//                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
//            }
//        }
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getPreferenceScreen().getSharedPreferences()
//                .registerOnSharedPreferenceChangeListener(this);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        getPreferenceScreen().getSharedPreferences()
//                .unregisterOnSharedPreferenceChangeListener(this);
//    }
//    }
