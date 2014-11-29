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
import android.preference.PreferenceCategory;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

import com.android.settings.cyanogenmod.SystemSettingSwitchPreference;

public class CustomSettings extends SettingsPreferenceFragment {

    private static final String CATEGORY_STATUS_BAR = "status_bar";

    private static final String LOCKSCREEN_CARRIER_LABEL = "lock_screen_show_carrier";

    private SystemSettingSwitchPreference mShowCarrierLabel;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.custom_settings);

        PreferenceCategory status_bar = (PreferenceCategory) findPreference(CATEGORY_STATUS_BAR);

        mShowCarrierLabel =
                (SystemSettingSwitchPreference) findPreference(LOCKSCREEN_CARRIER_LABEL);

        if (!Utils.isVoiceCapable(getActivity())) {
            status_bar.removePreference(mShowCarrierLabel);
        }
    }
}
