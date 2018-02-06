package com.example.dell.popularmovies.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.example.dell.popularmovies.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_settings);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }
    public static class SettingsFragment extends PreferenceFragment{
        @Override
        public void onCreate(final Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefrencess);
        }
    }
}

//        public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
//
//            public void onCreatePreferences(Bundle bundle, String s) {
//
//                addPreferencesFromResource(R.xml.prefrencess);
//                SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
//                PreferenceScreen prefScreen = getPreferenceScreen();
//                int count = prefScreen.getPreferenceCount();
//
//                // Go through all of the preferences, and set up their preference summary.
////        for (int i = 0; i < count; i++) {
//               Preference p = prefScreen.getPreference(count);
//                // You don't need to set up preference summaries for checkbox preferences because
//                // they are already set up in xml using summaryOff and summary On
////            if (!(p instanceof CheckBoxPreference)) {
//                String value = sharedPreferences.getString(p.getKey(), "");
//                setPreferenceSummary(p, value);
////            }
////        }
//            }
//
//
//
//
//
//
//            @Override
//            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//               Preference preference = findPreference(key);
//                if (null != preference) {
//                    // Updates the summary for the preference
//                    String value = sharedPreferences.getString(preference.getKey(), "");
//                    setPreferenceSummary(preference, value);
//                }
//            }
//
//            private void setPreferenceSummary(Preference preference, String value) {
//                if (preference instanceof ListPreference) {
//                    // For list preferences, figure out the label of the selected value
//                    ListPreference listPreference = (ListPreference) preference;
//                    int prefIndex = listPreference.findIndexOfValue(value);
//                    if (prefIndex >= 0) {
//                        // Set the summary to that label
//                        listPreference.setSummary(listPreference.getEntries()[prefIndex]);
//                    }
//                }
//            }
//
//            @Override
//            public void onCreate(Bundle savedInstanceState) {
//                super.onCreate(savedInstanceState);
//                getPreferenceScreen().getSharedPreferences()
//                        .registerOnSharedPreferenceChangeListener(this);
//            }
//
//            @Override
//            public void onDestroy() {
//                super.onDestroy();
//                getPreferenceScreen().getSharedPreferences()
//                        .unregisterOnSharedPreferenceChangeListener(this);
//            }
//        }
//}
