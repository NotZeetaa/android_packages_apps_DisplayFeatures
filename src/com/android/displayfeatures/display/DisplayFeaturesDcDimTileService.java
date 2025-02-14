/*
 * Copyright (C) 2023 cyberknight777
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

package com.android.displayfeatures.display;

import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import androidx.preference.PreferenceManager;

import com.android.displayfeatures.R;
import com.android.displayfeatures.utils.FileUtils;

public class DisplayFeaturesDcDimTileService extends TileService {

    private DisplayFeaturesConfig mConfig;

    private void updateUI(boolean enabled) {
        final Tile tile = getQsTile();
        tile.setIcon(Icon.createWithResource(this, enabled ? R.drawable.ic_dc_dimming_on : R.drawable.ic_dc_dimming_off));
        tile.setState(enabled ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
        tile.updateTile();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        mConfig = DisplayFeaturesConfig.getInstance(this);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        updateUI(sharedPrefs.getBoolean(mConfig.DISPLAYFEATURES_DC_DIMMING_KEY, false));
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
    }

    @Override
    public void onClick() {
        super.onClick();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        final boolean enabled = !(sharedPrefs.getBoolean(mConfig.DISPLAYFEATURES_DC_DIMMING_KEY, false));

        FileUtils.writeLine(mConfig.getDcDimPath(), enabled ? "0xF00" : "0xE00");
        sharedPrefs.edit().putBoolean(mConfig.DISPLAYFEATURES_DC_DIMMING_KEY, enabled).commit();
        updateUI(enabled);
    }
}
