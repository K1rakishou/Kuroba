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
package com.github.adamantcheese.chan.core.di.module.application;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.github.adamantcheese.chan.core.database.DatabaseManager;
import com.github.adamantcheese.chan.core.image.ImageLoaderV2;
import com.github.adamantcheese.chan.core.manager.ArchivesManager;
import com.github.adamantcheese.chan.core.manager.BoardManager;
import com.github.adamantcheese.chan.core.manager.FilterEngine;
import com.github.adamantcheese.chan.core.manager.FilterWatchManager;
import com.github.adamantcheese.chan.core.manager.PageRequestManager;
import com.github.adamantcheese.chan.core.manager.ReplyManager;
import com.github.adamantcheese.chan.core.manager.SavedThreadLoaderManager;
import com.github.adamantcheese.chan.core.manager.ThreadSaveManager;
import com.github.adamantcheese.chan.core.manager.WakeManager;
import com.github.adamantcheese.chan.core.manager.WatchManager;
import com.github.adamantcheese.chan.core.model.json.site.SiteConfig;
import com.github.adamantcheese.chan.core.pool.ChanLoaderFactory;
import com.github.adamantcheese.chan.core.repository.BoardRepository;
import com.github.adamantcheese.chan.core.repository.SavedThreadLoaderRepository;
import com.github.adamantcheese.chan.core.settings.json.JsonSettings;
import com.github.adamantcheese.chan.core.site.Site;
import com.github.adamantcheese.chan.core.site.SiteService;
import com.github.adamantcheese.chan.core.site.http.HttpCallManager;
import com.github.adamantcheese.chan.core.site.parser.CommentParser;
import com.github.adamantcheese.chan.core.site.parser.CommentParserHelper;
import com.github.adamantcheese.chan.core.site.sites.chan4.Chan4;
import com.github.adamantcheese.chan.ui.settings.base_directory.LocalThreadsBaseDirectory;
import com.github.adamantcheese.chan.ui.settings.base_directory.SavedFilesBaseDirectory;
import com.github.adamantcheese.chan.ui.theme.ThemeHelper;
import com.github.adamantcheese.chan.utils.Logger;
import com.github.k1rakishou.fsaf.FileManager;
import com.github.k1rakishou.fsaf.manager.base_directory.DirectoryManager;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class ManagerModule {

    @Provides
    @Singleton
    public BoardManager provideBoardManager(
            BoardRepository boardRepository
    ) {
        Logger.d(AppModule.DI_TAG, "Board manager");
        return new BoardManager(boardRepository);
    }

    @Provides
    @Singleton
    public FilterEngine provideFilterEngine(DatabaseManager databaseManager) {
        Logger.d(AppModule.DI_TAG, "Filter engine");
        return new FilterEngine(databaseManager);
    }

    @Provides
    @Singleton
    public ReplyManager provideReplyManager(Context applicationContext) {
        Logger.d(AppModule.DI_TAG, "Reply manager");
        return new ReplyManager(applicationContext);
    }

    @Provides
    @Singleton
    public ChanLoaderFactory provideChanLoaderFactory(
            RequestQueue volleyRequestQueue,
            DatabaseManager databaseManager,
            SavedThreadLoaderManager savedThreadLoaderManager,
            FilterEngine filterEngine
    ) {
        Logger.d(AppModule.DI_TAG, "Chan loader factory");
        return new ChanLoaderFactory(
                volleyRequestQueue,
                databaseManager,
                savedThreadLoaderManager,
                filterEngine
        );
    }

    @Provides
    @Singleton
    public WatchManager provideWatchManager(
            DatabaseManager databaseManager,
            ChanLoaderFactory chanLoaderFactory,
            WakeManager wakeManager,
            PageRequestManager pageRequestManager,
            ThreadSaveManager threadSaveManager
    ) {
        Logger.d(AppModule.DI_TAG, "Watch manager");
        return new WatchManager(
                databaseManager,
                chanLoaderFactory,
                wakeManager,
                pageRequestManager,
                threadSaveManager
        );
    }

    @Provides
    @Singleton
    public WakeManager provideWakeManager() {
        Logger.d(AppModule.DI_TAG, "Wake manager");
        return new WakeManager();
    }

    @Provides
    @Singleton
    public FilterWatchManager provideFilterWatchManager(
            WakeManager wakeManager,
            FilterEngine filterEngine,
            WatchManager watchManager,
            ChanLoaderFactory chanLoaderFactory,
            BoardRepository boardRepository,
            DatabaseManager databaseManager
    ) {
        Logger.d(AppModule.DI_TAG, "Filter watch manager");
        return new FilterWatchManager(wakeManager, filterEngine, watchManager, chanLoaderFactory, boardRepository, databaseManager);
    }

    @Provides
    @Singleton
    public PageRequestManager providePageRequestManager() {
        Logger.d(AppModule.DI_TAG, "Page request manager");
        return new PageRequestManager();
    }

    @Provides
    @Singleton
    public ArchivesManager provideArchivesManager(
            OkHttpClient okHttpClient,
            ImageLoaderV2 imageLoaderV2,
            HttpCallManager httpCallManager,
            RequestQueue requestQueue,
            BoardManager boardManager,
            CommentParser commentParser,
            CommentParserHelper commentParserHelper,
            SiteService siteService,
            ThemeHelper themeHelper
    ) {
        Logger.d(AppModule.DI_TAG, "Archives manager (4chan only)");

        //archives are only for 4chan, make a dummy site instance for this method
        Site chan4 = new Chan4();
        chan4.initialize(
                9999,
                okHttpClient,
                imageLoaderV2,
                new SiteConfig(),
                new JsonSettings(),
                httpCallManager,
                requestQueue,
                boardManager,
                commentParser,
                commentParserHelper,
                siteService,
                themeHelper
        );

        chan4.postInitialize();

        return new ArchivesManager(chan4);
    }

    @Provides
    @Singleton
    public ThreadSaveManager provideSaveThreadManager(
            DatabaseManager databaseManager,
            SavedThreadLoaderRepository savedThreadLoaderRepository,
            FileManager fileManager) {
        Logger.d(AppModule.DI_TAG, "Thread save manager");
        return new ThreadSaveManager(
                databaseManager,
                savedThreadLoaderRepository,
                fileManager);
    }

    @Provides
    @Singleton
    public SavedThreadLoaderManager provideSavedThreadLoaderManager(
            SavedThreadLoaderRepository savedThreadLoaderRepository,
            FileManager fileManager,
            ThemeHelper themeHelper,
            Gson gson
    ) {
        Logger.d(AppModule.DI_TAG, "Saved thread loader manager");
        return new SavedThreadLoaderManager(
                savedThreadLoaderRepository,
                fileManager,
                themeHelper,
                gson
        );
    }

    @Provides
    @Singleton
    public FileManager provideFileManager(Context applicationContext) {
        DirectoryManager directoryManager = new DirectoryManager();

        // Add new base directories here
        LocalThreadsBaseDirectory localThreadsBaseDirectory = new LocalThreadsBaseDirectory();
        SavedFilesBaseDirectory savedFilesBaseDirectory = new SavedFilesBaseDirectory();

        FileManager fileManager = new FileManager(
                applicationContext,
                directoryManager
        );

        fileManager.registerBaseDir(
                LocalThreadsBaseDirectory.class,
                localThreadsBaseDirectory
        );
        fileManager.registerBaseDir(
                SavedFilesBaseDirectory.class,
                savedFilesBaseDirectory
        );

        return fileManager;
    }

}
