/*
 * Kuroba - *chan browser https://github.com/Adamantcheese/Kuroba/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.adamantcheese.chan.core.model.orm;

import android.util.Pair;

import com.github.adamantcheese.chan.core.model.json.site.SiteConfig;
import com.github.adamantcheese.chan.core.settings.json.JsonSettings;
import com.github.adamantcheese.chan.utils.Logger;
import com.google.gson.Gson;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "site")
public class SiteModel {
    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
    public int id;

    @DatabaseField
    public String configuration;

    @DatabaseField
    public String userSettings;

    @DatabaseField
    public int order;

    public SiteModel(int id, String configuration, String userSettings, int order) {
        this.id = id;
        this.configuration = configuration;
        this.userSettings = userSettings;
        this.order = order;
    }

    public SiteModel() {
    }

    public void storeConfig(Gson gson, SiteConfig config) {
        this.configuration = gson.toJson(config);
    }

    public void storeUserSettings(Gson gson, JsonSettings userSettings) {
        this.userSettings = gson.toJson(userSettings);
        Logger.test("userSettings = " + this.userSettings);
    }

    public Pair<SiteConfig, JsonSettings> loadConfigFields(Gson gson) {
        SiteConfig config = gson.fromJson(this.configuration, SiteConfig.class);
        JsonSettings settings = gson.fromJson(this.userSettings, JsonSettings.class);
        Logger.d("SiteModel", "Config: " + configuration + ", Settings: " + userSettings);
        return Pair.create(config, settings);
    }
}
