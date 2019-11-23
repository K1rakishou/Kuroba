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


import com.android.volley.RequestQueue;
import com.github.adamantcheese.chan.core.database.DatabaseManager;
import com.github.adamantcheese.chan.core.image.ImageLoaderV2;
import com.github.adamantcheese.chan.core.manager.BoardManager;
import com.github.adamantcheese.chan.core.model.orm.SiteModel;
import com.github.adamantcheese.chan.core.repository.SiteRepository;
import com.github.adamantcheese.chan.core.settings.json.JsonSettings;
import com.github.adamantcheese.chan.core.site.http.HttpCallManager;
import com.github.adamantcheese.chan.core.site.parser.CommentParser;
import com.github.adamantcheese.chan.core.site.parser.CommentParserHelper;
import com.github.adamantcheese.chan.ui.theme.ThemeHelper;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import okhttp3.OkHttpClient;

public class SiteService {
    private SiteRepository siteRepository;
    private SiteResolver resolver;
    private OkHttpClient okHttpClient;
    private DatabaseManager databaseManager;
    private ImageLoaderV2 imageLoaderV2;
    private HttpCallManager httpCallManager;
    private RequestQueue requestQueue;
    private BoardManager boardManager;
    private CommentParser commentParser;
    private CommentParserHelper commentParserHelper;
    private Gson gson;
    private ThemeHelper themeHelper;

    private boolean initialized = false;

    @Inject
    public SiteService(
            SiteRepository siteRepository,
            SiteResolver resolver,
            OkHttpClient okHttpClient,
            DatabaseManager databaseManager,
            ImageLoaderV2 imageLoaderV2,
            HttpCallManager httpCallManager,
            RequestQueue requestQueue,
            BoardManager boardManager,
            CommentParser commentParser,
            CommentParserHelper commentParserHelper,
            Gson gson,
            ThemeHelper themeHelper
    ) {
        this.siteRepository = siteRepository;
        this.resolver = resolver;
        this.okHttpClient = okHttpClient;
        this.databaseManager = databaseManager;
        this.imageLoaderV2 = imageLoaderV2;
        this.httpCallManager = httpCallManager;
        this.requestQueue = requestQueue;
        this.boardManager = boardManager;
        this.commentParser = commentParser;
        this.commentParserHelper = commentParserHelper;
        this.gson = gson;
        this.themeHelper = themeHelper;
    }

    public boolean areSitesSetup() {
        return !siteRepository.all().getAll().isEmpty();
    }

    public void addSite(String url, SiteAddCallback callback) {
        Site existing = resolver.findSiteForUrl(url);
        if (existing != null) {
            callback.onSiteAddFailed("site already added");
            return;
        }

        SiteResolver.SiteResolverResult resolve = resolver.resolveSiteForUrl(url);

        Class<? extends Site> siteClass;
        if (resolve.match == SiteResolver.SiteResolverResult.Match.BUILTIN) {
            siteClass = resolve.builtinResult;
        } else if (resolve.match == SiteResolver.SiteResolverResult.Match.EXTERNAL) {
            callback.onSiteAddFailed("external sites not hardcoded is not implemented yet");
            return;
        } else {
            callback.onSiteAddFailed("not a url");
            return;
        }

        Site site = siteRepository.createFromClass(
                siteClass,
                okHttpClient,
                imageLoaderV2,
                httpCallManager,
                requestQueue,
                boardManager,
                commentParser,
                commentParserHelper,
                this,
                themeHelper,
                gson
        );

        callback.onSiteAdded(site);
    }

    public void updateUserSettings(Site site, JsonSettings jsonSettings) {
        SiteModel siteModel = siteRepository.byId(site.id());
        if (siteModel == null) throw new NullPointerException("siteModel == null");
        siteRepository.updateSiteUserSettingsAsync(gson, siteModel, jsonSettings);
    }

    public void updateOrdering(List<Site> sitesInNewOrder) {
        siteRepository.updateSiteOrderingAsync(sitesInNewOrder);
    }

    public void initialize() {
        if (initialized) {
            throw new IllegalStateException("Already initialized");
        }
        initialized = true;
        siteRepository.initialize(
                okHttpClient,
                databaseManager,
                imageLoaderV2,
                httpCallManager,
                requestQueue,
                boardManager,
                commentParser,
                commentParserHelper,
                this,
                themeHelper,
                gson
        );
    }

    public interface SiteAddCallback {
        void onSiteAdded(Site site);

        void onSiteAddFailed(String message);
    }
}
