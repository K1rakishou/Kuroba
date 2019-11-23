package com.github.adamantcheese.chan.core.di.component.activity;

import com.github.adamantcheese.chan.StartActivity;
import com.github.adamantcheese.chan.core.di.module.activity.StartActivityModule;
import com.github.adamantcheese.chan.core.di.scope.PerActivity;
import com.github.adamantcheese.chan.ui.adapter.DrawerAdapter;
import com.github.adamantcheese.chan.ui.captcha.CaptchaLayout;
import com.github.adamantcheese.chan.ui.captcha.LegacyCaptchaLayout;
import com.github.adamantcheese.chan.ui.captcha.v1.CaptchaNojsLayoutV1;
import com.github.adamantcheese.chan.ui.captcha.v2.CaptchaNoJsLayoutV2;
import com.github.adamantcheese.chan.ui.cell.CardPostCell;
import com.github.adamantcheese.chan.ui.cell.PostCell;
import com.github.adamantcheese.chan.ui.cell.ThreadStatusCell;
import com.github.adamantcheese.chan.ui.controller.AlbumDownloadController;
import com.github.adamantcheese.chan.ui.controller.AlbumViewController;
import com.github.adamantcheese.chan.ui.controller.AppearanceSettingsController;
import com.github.adamantcheese.chan.ui.controller.ArchiveController;
import com.github.adamantcheese.chan.ui.controller.BaseFloatingController;
import com.github.adamantcheese.chan.ui.controller.BehaviourSettingsController;
import com.github.adamantcheese.chan.ui.controller.BoardSetupController;
import com.github.adamantcheese.chan.ui.controller.BrowseController;
import com.github.adamantcheese.chan.ui.controller.DeveloperSettingsController;
import com.github.adamantcheese.chan.ui.controller.DrawerController;
import com.github.adamantcheese.chan.ui.controller.ExperimentalSettingsController;
import com.github.adamantcheese.chan.ui.controller.FiltersController;
import com.github.adamantcheese.chan.ui.controller.HistoryController;
import com.github.adamantcheese.chan.ui.controller.ImageOptionsController;
import com.github.adamantcheese.chan.ui.controller.ImageReencodeOptionsController;
import com.github.adamantcheese.chan.ui.controller.ImageViewerController;
import com.github.adamantcheese.chan.ui.controller.ImageViewerNavigationController;
import com.github.adamantcheese.chan.ui.controller.ImportExportSettingsController;
import com.github.adamantcheese.chan.ui.controller.LicensesController;
import com.github.adamantcheese.chan.ui.controller.LoginController;
import com.github.adamantcheese.chan.ui.controller.LogsController;
import com.github.adamantcheese.chan.ui.controller.MainSettingsController;
import com.github.adamantcheese.chan.ui.controller.MediaSettingsController;
import com.github.adamantcheese.chan.ui.controller.PopupController;
import com.github.adamantcheese.chan.ui.controller.PostRepliesController;
import com.github.adamantcheese.chan.ui.controller.RemovedPostsController;
import com.github.adamantcheese.chan.ui.controller.ReportController;
import com.github.adamantcheese.chan.ui.controller.SaveLocationController;
import com.github.adamantcheese.chan.ui.controller.SiteSetupController;
import com.github.adamantcheese.chan.ui.controller.SitesSetupController;
import com.github.adamantcheese.chan.ui.controller.SplitNavigationController;
import com.github.adamantcheese.chan.ui.controller.StyledToolbarNavigationController;
import com.github.adamantcheese.chan.ui.controller.ThemeSettingsController;
import com.github.adamantcheese.chan.ui.controller.ThreadController;
import com.github.adamantcheese.chan.ui.controller.ThreadSlideController;
import com.github.adamantcheese.chan.ui.controller.ToolbarNavigationController;
import com.github.adamantcheese.chan.ui.controller.ViewThreadController;
import com.github.adamantcheese.chan.ui.controller.WatchSettingsController;
import com.github.adamantcheese.chan.ui.layout.ArchivesLayout;
import com.github.adamantcheese.chan.ui.layout.BoardAddLayout;
import com.github.adamantcheese.chan.ui.layout.BrowseBoardsFloatingMenu;
import com.github.adamantcheese.chan.ui.layout.FilterLayout;
import com.github.adamantcheese.chan.ui.layout.ReplyLayout;
import com.github.adamantcheese.chan.ui.layout.ThreadLayout;
import com.github.adamantcheese.chan.ui.layout.ThreadListLayout;
import com.github.adamantcheese.chan.ui.theme.ThemeHelper;
import com.github.adamantcheese.chan.ui.toolbar.Toolbar;
import com.github.adamantcheese.chan.ui.toolbar.ToolbarContainer;
import com.github.adamantcheese.chan.ui.view.FloatingMenu;
import com.github.adamantcheese.chan.ui.view.MultiImageView;
import com.github.adamantcheese.chan.ui.view.ThumbnailView;

import dagger.BindsInstance;
import dagger.Subcomponent;

@PerActivity
@Subcomponent(modules = {StartActivityModule.class})
public interface StartActivityComponent {
    ThemeHelper getThemeHelper();

    void inject(StartActivity startActivity);
    void inject(BrowseBoardsFloatingMenu browseBoardsFloatingMenu);
    void inject(CaptchaNoJsLayoutV2 captchaNoJsLayoutV2);
    void inject(CaptchaNojsLayoutV1 captchaNojsLayoutV1);
    void inject(ThumbnailView thumbnailView);
    void inject(MultiImageView multiImageView);
    void inject(ThreadLayout threadLayout);
    void inject(ArchivesLayout archivesLayout);
    void inject(FilterLayout filterLayout);
    void inject(ReplyLayout replyLayout);
    void inject(ThreadListLayout threadListLayout);
    void inject(ToolbarContainer toolbarContainer);
    void inject(CardPostCell cardPostCell);
    void inject(LegacyCaptchaLayout legacyCaptchaLayout);
    void inject(CaptchaLayout captchaLayout);
    void inject(FloatingMenu floatingMenu);
    void inject(Toolbar toolbar);
    void inject(ThreadStatusCell threadStatusCell);
    void inject(BoardAddLayout boardAddLayout);
    void inject(PostCell postCell);
    void inject(DrawerAdapter drawerAdapter);

    void inject(AlbumDownloadController albumDownloadController);
    void inject(AlbumViewController albumViewController);
    void inject(AppearanceSettingsController appearanceSettingsController);
    void inject(ArchiveController archiveController);
    void inject(BaseFloatingController baseFloatingController);
    void inject(BehaviourSettingsController behaviourSettingsController);
    void inject(BoardSetupController boardSetupController);
    void inject(BrowseController browseController);
    void inject(DeveloperSettingsController developerSettingsController);
    void inject(DrawerController drawerController);
    void inject(ExperimentalSettingsController experimentalSettingsController);
    void inject(FiltersController filtersController);
    void inject(HistoryController historyController);
    void inject(ImageOptionsController imageOptionsController);
    void inject(ImageReencodeOptionsController imageReencodeOptionsController);
    void inject(ImageViewerController imageViewerController);
    void inject(ImageViewerNavigationController imageViewerNavigationController);
    void inject(ImportExportSettingsController importExportSettingsController);
    void inject(LicensesController licensesController);
    void inject(LoginController loginController);
    void inject(LogsController logsController);
    void inject(MainSettingsController mainSettingsController);
    void inject(MediaSettingsController mediaSettingsController);
    void inject(PopupController popupController);
    void inject(PostRepliesController postRepliesController);
    void inject(RemovedPostsController removedPostsController);
    void inject(ReportController reportController);
    void inject(SaveLocationController saveLocationController);
    void inject(SitesSetupController sitesSetupController);
    void inject(SiteSetupController siteSetupController);
    void inject(SplitNavigationController splitNavigationController);
    void inject(StyledToolbarNavigationController styledToolbarNavigationController);
    void inject(ThemeSettingsController themeSettingsController);
    void inject(ThreadController threadController);
    void inject(ThreadSlideController threadSlideController);
    void inject(ToolbarNavigationController toolbarNavigationController);
    void inject(ViewThreadController viewThreadController);
    void inject(WatchSettingsController watchSettingsController);

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        Builder startActivity(StartActivity activity);
        @BindsInstance
        Builder startActivityModule(StartActivityModule module);

        StartActivityComponent build();
    }
}
