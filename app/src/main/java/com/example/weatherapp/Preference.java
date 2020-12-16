package com.example.weatherapp;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;


public class Preference extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);
        Load_Settings();

    }
    private void Load_Settings(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean chk_night = sp.getBoolean("NIGHT",false);
        if(chk_night){
            getListView().setBackgroundResource(R.drawable.gradient_bk);
        }else{
            getListView().setBackgroundResource(R.drawable.gradient1_bg);;
        }

        CheckBoxPreference chk_night_instant = (CheckBoxPreference)findPreference("NIGHT");
        chk_night_instant.setOnPreferenceChangeListener(new android.preference.Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(android.preference.Preference preference, Object o) {
                boolean yes = (boolean)o;
                if(yes){
                       getListView().setBackgroundResource(R.drawable.gradient_bk);
                }else{
                    getListView().setBackgroundResource(R.drawable.gradient1_bg);;
                }
                return true;
            }
        });
        ListPreference lp = (ListPreference)findPreference("ORIENTATION");
        String orientation = sp.getString("ORIENTATION","false");
        if("1".equals(orientation)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
            lp.setSummary(lp.getEntry());
        }else if ("2".equals(orientation)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            lp.setSummary(lp.getEntry());
        }else if ("3".equals(orientation)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            lp.setSummary(lp.getEntry());
        }
        lp.setOnPreferenceChangeListener(new android.preference.Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(android.preference.Preference preference, Object o) {
                String items = (String) o;
                if (preference.getKey().equals("ORIENTATION")) {
                    switch (items) {
                        case "1":
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
                            break;
                        case "2":
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            break;
                        case "3":
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            break;
                    }
                    ListPreference lpp = (ListPreference) preference;
                    lpp.setSummary(lpp.getEntries()[lpp.findIndexOfValue(items)]);
                }
                return true;
            }
        });
    }
    @Override
    protected void onResume() {
        Load_Settings();
        super.onResume();
    }
}
