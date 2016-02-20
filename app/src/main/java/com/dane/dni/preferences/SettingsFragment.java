package com.dane.dni.preferences;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.dane.dni.R;

/**
 * Created by Dane on 11/1/2015.
 */
public class SettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    BroadcastReceiver br;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ((DateTimePreference) SettingsFragment.this.findPreference("custom_date_time"))
                    .updateTime();
            }
        };

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        findPreference("custom_date_time").setEnabled(
                !getPreferenceScreen().getSharedPreferences()
                        .getBoolean("system_time", false));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("system_time")) {
            findPreference("custom_date_time").setEnabled(
                    !sharedPreferences.getBoolean("system_time", false));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
            .registerOnSharedPreferenceChangeListener(this);
        getActivity().registerReceiver(br, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
        getActivity().unregisterReceiver(br);
    }
}
