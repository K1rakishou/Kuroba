package com.github.adamantcheese.chan.core.di.module.activity;

import com.android.volley.RequestQueue;
import com.github.adamantcheese.chan.StartActivity;
import com.github.adamantcheese.chan.core.cache.FileCache;
import com.github.adamantcheese.chan.core.di.scope.PerActivity;
import com.github.adamantcheese.chan.core.manager.BoardManager;
import com.github.adamantcheese.chan.core.manager.ReplyManager;
import com.github.adamantcheese.chan.core.manager.UpdateManager;
import com.github.adamantcheese.chan.core.presenter.BoardsMenuPresenter;
import com.github.adamantcheese.chan.ui.helper.ImagePickDelegate;
import com.github.adamantcheese.chan.ui.helper.RuntimePermissionsHelper;
import com.github.k1rakishou.fsaf.FileManager;

import dagger.Module;
import dagger.Provides;

@Module
public class StartActivityModule {

    @Provides
    @PerActivity
    public BoardsMenuPresenter provideBoardsMenuPresenter(BoardManager boardManager) {
        return new BoardsMenuPresenter(boardManager);
    }

    @Provides
    @PerActivity
    public UpdateManager provideUpdateManager(
            StartActivity activityContext,
            RequestQueue requestQueue,
            FileCache fileCache
    ) {
        return new UpdateManager(activityContext, requestQueue, fileCache);
    }

    @Provides
    @PerActivity
    public ImagePickDelegate provideImagePickerDelegate(
            StartActivity activity,
            ReplyManager replyManager,
            FileManager fileManager,
            FileCache fileCache
    ) {
        return new ImagePickDelegate(activity, replyManager, fileManager, fileCache);
    }

    @Provides
    @PerActivity
    public RuntimePermissionsHelper provideRuntimePermissionHelper(StartActivity activity) {
        return new RuntimePermissionsHelper(activity);
    }
}
