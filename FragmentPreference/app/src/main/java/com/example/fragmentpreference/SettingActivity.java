package com.example.fragmentpreference;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.RequiresApi;

public class SettingActivity extends Activity {

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public static class SettingFragment extends PreferenceFragment{
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content,new SettingFragment())
                .commit();
    }
}
