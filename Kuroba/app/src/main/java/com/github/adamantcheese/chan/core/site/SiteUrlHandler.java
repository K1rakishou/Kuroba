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
package com.github.adamantcheese.chan.core.site;

import com.github.adamantcheese.chan.core.model.orm.Loadable;

import okhttp3.HttpUrl;

public abstract class SiteUrlHandler {
    abstract public Class<? extends Site> getSiteClass();

    abstract public boolean matchesName(String value);

    abstract public boolean respondsTo(HttpUrl url);

    public String desktopUrlForThread(Loadable loadable) {
        // -1 here means that we only want to get a link for a thread
        return desktopUrlForPost(loadable, -1);
    }

    abstract public String desktopUrlForPost(Loadable loadable, final int postNo);

    abstract public Loadable resolveLoadable(Site site, HttpUrl url);
}
