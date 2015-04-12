/*
 * Copyright (C) 2015 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.settings.cyanogenmod;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserHandle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.SearchIndexableResource;
import android.provider.Settings;
import android.os.Vibrator;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.cyanogenmod.qs.QSTiles;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;

import java.util.ArrayList;
import java.util.List;

import com.android.internal.widget.LockPatternUtils;

public class NotificationDrawerSettings extends SettingsPreferenceFragment implements Indexable,
        Preference.OnPreferenceChangeListener {
    private static final String QUICK_PULLDOWN = "quick_pulldown";
    private static final String SMART_PULLDOWN = "smart_pulldown";
    private static final String PREF_BLOCK_ON_SECURE_KEYGUARD = "status_bar_locked_on_secure_keyguard";
    private static final String QS_VIBRATE = "quick_settings_vibrate";

    private ListPreference mQuickPulldown;
    private ListPreference mSmartPulldown;
    private SwitchPreference mBlockOnSecureKeyguard;
    private SwitchPreference mQsVibrate;
    private Preference mQSTiles;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.notification_drawer_settings);

        mQSTiles = findPreference("qs_order");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        PreferenceScreen prefSet = getPreferenceScreen();
        ContentResolver resolver = getActivity().getContentResolver();
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        mQuickPulldown = (ListPreference) prefSet.findPreference(QUICK_PULLDOWN);
        mQuickPulldown.setOnPreferenceChangeListener(this);
        int quickPulldownValue = Settings.System.getIntForUser(resolver,
                Settings.System.QS_QUICK_PULLDOWN, 0, UserHandle.USER_CURRENT);
        mQuickPulldown.setValue(String.valueOf(quickPulldownValue));
        mQuickPulldown.setSummary(mQuickPulldown.getEntry());

        mSmartPulldown = (ListPreference) prefSet.findPreference(SMART_PULLDOWN);
        mSmartPulldown.setOnPreferenceChangeListener(this);
        int smartPulldownValue = Settings.System.getIntForUser(resolver,
                Settings.System.QS_SMART_PULLDOWN, 0, UserHandle.USER_CURRENT);
        mSmartPulldown.setValue(String.valueOf(smartPulldownValue));
        mSmartPulldown.setSummary(mSmartPulldown.getEntry());

        mQsVibrate = (SwitchPreference) findPreference(QS_VIBRATE);
        if (vibrator == null || !vibrator.hasVibrator()) {
            prefSet.removePreference(mQsVibrate);
        }

        final LockPatternUtils lockPatternUtils = new LockPatternUtils(getActivity());
        mBlockOnSecureKeyguard = (SwitchPreference) findPreference(PREF_BLOCK_ON_SECURE_KEYGUARD);
        if (!lockPatternUtils.isSecure()) {
            prefSet.removePreference(mBlockOnSecureKeyguard);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        int qsTileCount = QSTiles.determineTileCount(getActivity());
        mQSTiles.setSummary(getResources().getQuantityString(R.plurals.qs_tiles_summary,
                    qsTileCount, qsTileCount));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getContentResolver();
        if (preference == mQuickPulldown) {
            int quickPulldownValue = Integer.valueOf((String) newValue);
            int index = mQuickPulldown.findIndexOfValue((String) newValue);
            Settings.System.putIntForUser(resolver, Settings.System.QS_QUICK_PULLDOWN,
                    quickPulldownValue, UserHandle.USER_CURRENT);
            mQuickPulldown.setSummary(mQuickPulldown.getEntries()[index]);
            return true;
        } else if (preference == mSmartPulldown) {
            int smartPulldownValue = Integer.valueOf((String) newValue);
            int index = mSmartPulldown.findIndexOfValue((String) newValue);
            Settings.System.putIntForUser(resolver, Settings.System.QS_SMART_PULLDOWN,
                    smartPulldownValue, UserHandle.USER_CURRENT);
            mSmartPulldown.setSummary(mSmartPulldown.getEntries()[index]);
            return true;
        }
        return false;
    }

    public static final Indexable.SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                                                                            boolean enabled) {
                    ArrayList<SearchIndexableResource> result =
                            new ArrayList<SearchIndexableResource>();

                    SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.notification_drawer_settings;
                    result.add(sir);

                    return result;
                }

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    return new ArrayList<String>();
                }
            };
}
