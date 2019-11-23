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
import com.github.adamantcheese.chan.core.image.ImageLoaderV2;
import com.github.adamantcheese.chan.core.manager.BoardManager;
import com.github.adamantcheese.chan.core.model.json.site.SiteConfig;
import com.github.adamantcheese.chan.core.model.orm.Board;
import com.github.adamantcheese.chan.core.settings.SettingProvider;
import com.github.adamantcheese.chan.core.settings.json.JsonSettings;
import com.github.adamantcheese.chan.core.settings.json.JsonSettingsProvider;
import com.github.adamantcheese.chan.core.site.http.HttpCallManager;
import com.github.adamantcheese.chan.core.site.parser.CommentParser;
import com.github.adamantcheese.chan.core.site.parser.CommentParserHelper;
import com.github.adamantcheese.chan.ui.theme.ThemeHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;

public abstract class SiteBase implements Site {
    protected int id;
    protected SiteConfig config;

    protected OkHttpClient okHttpClient;
    protected ImageLoaderV2 imageLoaderV2;
    protected HttpCallManager httpCallManager;
    protected RequestQueue requestQueue;
    protected BoardManager boardManager;
    protected CommentParser commentParser;
    protected CommentParserHelper commentParserHelper;
    protected SiteService siteService;
    protected SettingProvider settingsProvider;
    protected ThemeHelper themeHelper;

    private JsonSettings userSettings;
    private boolean initialized = false;

    @Override
    public void initialize(
            int id,
            OkHttpClient okHttpClient,
            ImageLoaderV2 imageLoaderV2,
            SiteConfig config,
            JsonSettings userSettings,
            HttpCallManager httpCallManager,
            RequestQueue requestQueue,
            BoardManager boardManager,
            CommentParser commentParser,
            CommentParserHelper commentParserHelper,
            SiteService siteService,
            ThemeHelper themeHelper
    ) {
        this.id = id;
        this.okHttpClient = okHttpClient;
        this.imageLoaderV2 = imageLoaderV2;
        this.config = config;
        this.userSettings = userSettings;
        this.httpCallManager = httpCallManager;
        this.requestQueue = requestQueue;
        this.boardManager = boardManager;
        this.commentParser = commentParser;
        this.commentParserHelper = commentParserHelper;
        this.siteService = siteService;
        this.themeHelper = themeHelper;

        if (initialized) {
            throw new IllegalStateException();
        }
        initialized = true;
    }

    @Override
    public void postInitialize() {
        settingsProvider = new JsonSettingsProvider(
                userSettings,
                () -> siteService.updateUserSettings(this, userSettings));

        initializeSettings();

        if (boardsType().canList) {
            actions().boards(boards -> boardManager.updateAvailableBoardsForSite(this, boards.boards));
        }
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public Board board(String code) {
        return boardManager.getBoard(this, code);
    }

    @Override
    public List<SiteSetting> settings() {
        return new ArrayList<>();
    }

    public void initializeSettings() {
    }

    @Override
    public Board createBoard(String name, String code) {
        Board existing = board(code);
        if (existing != null) {
            return existing;
        }

        Board board = Board.fromSiteNameCode(this, name, code);
        boardManager.updateAvailableBoardsForSite(this, Collections.singletonList(board));
        return board;
    }

    public static class Boards {
        public final List<Board> boards;

        public Boards(List<Board> boards) {
            this.boards = boards;
        }
    }
}
