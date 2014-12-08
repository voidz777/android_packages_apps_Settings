/*
* Copyright (C) 2014 The Euphoria-OS Project
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
package com.android.settings.euphoria;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.SwitchPreference;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;
import com.android.settings.cyanogenmod.SystemSettingSwitchPreference;

import com.android.internal.widget.LockPatternUtils;

public class CustomSettings extends SettingsPreferenceFragment
            implements OnPreferenceChangeListener  {

    private static final String CATEGORY_STATUS_BAR = "status_bar";
    private static final String CATEGORY_QUICK_SETTINGS = "quick_settings";

    private static final String LOCKSCREEN_CARRIER_LABEL = "lock_screen_show_carrier";
    private static final String PREF_BLOCK_ON_SECURE_KEYGUARD = "block_on_secure_keyguard";

    private SystemSettingSwitchPreference mShowCarrierLabel;
    private SwitchPreference mBlockOnSecureKeyguard;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.custom_settings);

        PreferenceCategory status_bar = (PreferenceCategory) findPreference(CATEGORY_STATUS_BAR);
        PreferenceCategory quick_settings = (PreferenceCategory) findPreference(CATEGORY_QUICK_SETTINGS);

        mShowCarrierLabel =
                (SystemSettingSwitchPreference) findPreference(LOCKSCREEN_CARRIER_LABEL);

        if (!Utils.isVoiceCapable(getActivity())) {
            status_bar.removePreference(mShowCarrierLabel);
        }

        final LockPatternUtils lockPatternUtils = new LockPatternUtils(getActivity());
        mBlockOnSecureKeyguard = (SwitchPreference) findPreference(PREF_BLOCK_ON_SECURE_KEYGUARD);
        if (lockPatternUtils.isSecure()) {
            mBlockOnSecureKeyguard.setChecked(Settings.Secure.getInt(getContentResolver(),
                    Settings.Secure.STATUS_BAR_LOCKED_ON_SECURE_KEYGUARD, 1) == 1);
            mBlockOnSecureKeyguard.setOnPreferenceChangeListener(this);
        } else {
            quick_settings.removePreference(mBlockOnSecureKeyguard);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mBlockOnSecureKeyguard) {
            Settings.Secure.putInt(getContentResolver(),
                    Settings.Secure.STATUS_BAR_LOCKED_ON_SECURE_KEYGUARD,
                    (Boolean) newValue ? 1 : 0);
            return true;
        }
        return false;
    }
}
