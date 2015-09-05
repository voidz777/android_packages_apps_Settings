/*
 * Copyright (C) 2015 SlimRoms Project
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

import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.provider.Settings;

import android.provider.SearchIndexableResource;
import com.android.settings.euphoria.SeekBarPreference;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;

import java.util.ArrayList;
import java.util.List;

public class SlimDimSettings extends SettingsPreferenceFragment
            implements OnPreferenceChangeListener, Indexable  {

    private static final String DIM_NAV_BUTTONS_TIMEOUT = "dim_nav_buttons_timeout";
    private static final String DIM_NAV_BUTTONS_ALPHA = "dim_nav_buttons_alpha";
    private static final String DIM_NAV_BUTTONS_ANIMATE_DURATION = "dim_nav_buttons_animate_duration";

    private SeekBarPreference mDimNavButtonsTimeout;
    private SeekBarPreference mDimNavButtonsAlpha;
    private SeekBarPreference mDimNavButtonsAnimateDuration;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.slim_dim_settings);
        ContentResolver resolver = getActivity().getContentResolver();

        mDimNavButtonsTimeout = (SeekBarPreference) findPreference(DIM_NAV_BUTTONS_TIMEOUT);
        int dimNavButtonsTimeout = Settings.System.getInt(resolver,
                Settings.System.DIM_NAV_BUTTONS_TIMEOUT, 3000);
        mDimNavButtonsTimeout.setValue(dimNavButtonsTimeout / 1000);
        mDimNavButtonsTimeout.setOnPreferenceChangeListener(this);

        mDimNavButtonsAlpha = (SeekBarPreference) findPreference(DIM_NAV_BUTTONS_ALPHA);
        int dimNavButtonsAlpha = Settings.System.getInt(resolver,
                Settings.System.DIM_NAV_BUTTONS_ALPHA, 50);
        mDimNavButtonsAlpha.setValue(dimNavButtonsAlpha);
        mDimNavButtonsAlpha.setOnPreferenceChangeListener(this);

        mDimNavButtonsAnimateDuration = (SeekBarPreference) findPreference(DIM_NAV_BUTTONS_ANIMATE_DURATION);
        int dimNavButtonsAnimateDuration = Settings.System.getInt(resolver,
                Settings.System.DIM_NAV_BUTTONS_ANIMATE_DURATION, 2000);
        mDimNavButtonsAnimateDuration.setValue(dimNavButtonsAnimateDuration / 1000);
        mDimNavButtonsAnimateDuration.setOnPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mDimNavButtonsTimeout) {
            int dimNavButtonsTimeout = (Integer) newValue;
            Settings.System.putInt(getContentResolver(),
                    Settings.System.DIM_NAV_BUTTONS_TIMEOUT,
                    dimNavButtonsTimeout * 1000);
            return true;
        } else if (preference == mDimNavButtonsAlpha) {
            int dimNavButtonsAlpha = (Integer) newValue;
            Settings.System.putInt(getContentResolver(),
                    Settings.System.DIM_NAV_BUTTONS_ALPHA,
                    dimNavButtonsAlpha);
            return true;
        } else if (preference == mDimNavButtonsAnimateDuration) {
            int dimNavButtonsAnimateDuration = (Integer) newValue;
            Settings.System.putInt(getContentResolver(),
                    Settings.System.DIM_NAV_BUTTONS_ANIMATE_DURATION,
                    dimNavButtonsAnimateDuration * 1000);
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
                    sir.xmlResId = R.xml.slim_dim_settings;
                    result.add(sir);

                    return result;
                }

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    return new ArrayList<String>();
                }
            };
}
