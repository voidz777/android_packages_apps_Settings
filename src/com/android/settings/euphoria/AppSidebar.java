/*
 * Copyright (C) 2015 STELIX Carbon project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.euphoria;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.euphoria.SeekBarPreference;

public class AppSidebar extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener, Preference.OnPreferenceClickListener {
    private static final String TAG = "AppSideBar";

    private static final String KEY_TRANSPARENCY = "sidebar_transparency";
    private static final String KEY_SETUP_ITEMS = "sidebar_setup_items";
    private static final String KEY_POSITION = "sidebar_position";
    private static final String KEY_HIDE_LABELS = "sidebar_hide_labels";
    private static final String KEY_TRIGGER_WIDTH = "trigger_width";
    private static final String KEY_TRIGGER_TOP = "trigger_top";
    private static final String KEY_TRIGGER_BOTTOM = "trigger_bottom";

    private SeekBarPreference mTransparencyPref;
    private ListPreference mPositionPref;
    private SeekBarPreference mTriggerWidthPref;
    private SeekBarPreference mTriggerTopPref;
    private SeekBarPreference mTriggerBottomPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.app_side_bar_settings);
        PreferenceScreen prefSet = getPreferenceScreen();

        mPositionPref = (ListPreference) prefSet.findPreference(KEY_POSITION);
        mPositionPref.setOnPreferenceChangeListener(this);
        int position = Settings.System.getInt(getContentResolver(), Settings.System.APP_SIDEBAR_POSITION, 0);
        mPositionPref.setValue(String.valueOf(position));
        updatePositionSummary(position);

        mTransparencyPref = (SeekBarPreference) findPreference(KEY_TRANSPARENCY);
        mTransparencyPref.setValue(Settings.System.getInt(getContentResolver(),
                Settings.System.APP_SIDEBAR_TRANSPARENCY, 0));
        mTransparencyPref.setOnPreferenceChangeListener(this);

        mTriggerWidthPref = (SeekBarPreference) findPreference(KEY_TRIGGER_WIDTH);
        mTriggerWidthPref.setValue(Settings.System.getInt(getContentResolver(),
                Settings.System.APP_SIDEBAR_TRIGGER_WIDTH, 10));
        mTriggerWidthPref.setOnPreferenceChangeListener(this);

        mTriggerTopPref = (SeekBarPreference) findPreference(KEY_TRIGGER_TOP);
        mTriggerTopPref.setValue(Settings.System.getInt(getContentResolver(),
                Settings.System.APP_SIDEBAR_TRIGGER_TOP, 0));
        mTriggerTopPref.setOnPreferenceChangeListener(this);

        mTriggerBottomPref = (SeekBarPreference) findPreference(KEY_TRIGGER_BOTTOM);
        mTriggerBottomPref.setValue(Settings.System.getInt(getContentResolver(),
                Settings.System.APP_SIDEBAR_TRIGGER_HEIGHT, 100));
        mTriggerBottomPref.setOnPreferenceChangeListener(this);

        findPreference(KEY_SETUP_ITEMS).setOnPreferenceClickListener(this);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mTransparencyPref) {
            int transparency = ((Integer)newValue).intValue();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.APP_SIDEBAR_TRANSPARENCY, transparency);
            return true;
        } else if (preference == mTriggerWidthPref) {
            int width = ((Integer)newValue).intValue();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.APP_SIDEBAR_TRIGGER_WIDTH, width);
            return true;
        } else if (preference == mTriggerTopPref) {
            int top = ((Integer)newValue).intValue();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.APP_SIDEBAR_TRIGGER_TOP, top);
            return true;
        } else if (preference == mTriggerBottomPref) {
            int bottom = ((Integer)newValue).intValue();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.APP_SIDEBAR_TRIGGER_HEIGHT, bottom);
            return true;
        } else if (preference == mPositionPref) {
            int position = Integer.valueOf((String) newValue);
            updatePositionSummary(position);
            return true;
        }
        return false;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if(preference.getKey().equals(KEY_SETUP_ITEMS)) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(new ComponentName("com.android.systemui",
                    "com.android.systemui.statusbar.sidebar.SidebarConfigurationActivity"));
            getActivity().startActivity(intent);
            return true;
        }
        return false;
    }

    private void updatePositionSummary(int value) {
        mPositionPref.setSummary(mPositionPref.getEntries()[mPositionPref.findIndexOfValue("" + value)]);
        Settings.System.putInt(getContentResolver(),
                Settings.System.APP_SIDEBAR_POSITION, value);
    }

    @Override
    public void onPause() {
        super.onPause();
        Settings.System.putInt(getContentResolver(),
                Settings.System.APP_SIDEBAR_SHOW_TRIGGER, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        Settings.System.putInt(getContentResolver(),
                Settings.System.APP_SIDEBAR_SHOW_TRIGGER, 1);
    }
}
