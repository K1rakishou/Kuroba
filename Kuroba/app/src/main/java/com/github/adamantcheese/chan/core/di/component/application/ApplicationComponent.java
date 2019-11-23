package com.github.adamantcheese.chan.core.di.component.application;

import android.content.Context;

import com.github.adamantcheese.chan.Chan;
import com.github.adamantcheese.chan.core.di.component.activity.StartActivityComponent;
import com.github.adamantcheese.chan.core.di.component.service.LastPageNotificationServiceComponent;
import com.github.adamantcheese.chan.core.di.component.service.SavingNotificationComponent;
import com.github.adamantcheese.chan.core.di.component.service.WatchNotificationServiceComponent;
import com.github.adamantcheese.chan.core.di.module.application.AppModule;
import com.github.adamantcheese.chan.core.di.module.application.DatabaseModule;
import com.github.adamantcheese.chan.core.di.module.application.GsonModule;
import com.github.adamantcheese.chan.core.di.module.application.ManagerModule;
import com.github.adamantcheese.chan.core.di.module.application.NetModule;
import com.github.adamantcheese.chan.core.di.module.application.ParserModule;
import com.github.adamantcheese.chan.core.di.module.application.RepositoryModule;
import com.github.adamantcheese.chan.core.di.module.application.SiteModule;
import com.github.adamantcheese.chan.core.manager.ArchivesManager;
import com.github.adamantcheese.chan.core.receiver.WakeUpdateReceiver;
import com.github.adamantcheese.chan.core.repository.SiteRepository;
import com.github.adamantcheese.chan.core.site.parser.CommentParserHelper;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        DatabaseModule.class,
        GsonModule.class,
        ManagerModule.class,
        NetModule.class,
        RepositoryModule.class,
        SiteModule.class,
        ParserModule.class
})
public interface ApplicationComponent {
    Chan getApplication();
    SiteRepository getSiteRepository();
    ArchivesManager getArchivesManager();
    CommentParserHelper getCommentParserHelper();

    void inject(Chan application);
    void inject(WakeUpdateReceiver wakeUpdateReceiver);

    StartActivityComponent.Builder activityComponentBuilder();
    LastPageNotificationServiceComponent lastPageNotificationServiceComponentServiceComponent();
    WatchNotificationServiceComponent watchNotificationServiceComponent();
    SavingNotificationComponent savingNotificationComponent();

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder appContext(Context application);
        @BindsInstance
        Builder application(Chan application);
        @BindsInstance
        Builder appModule(AppModule appModule);
        @BindsInstance
        Builder databaseModule(DatabaseModule databaseModule);
        @BindsInstance
        Builder gsonModule(GsonModule gsonModule);
        @BindsInstance
        Builder managerModule(ManagerModule managerModule);
        @BindsInstance
        Builder netModule(NetModule netModule);
        @BindsInstance
        Builder repositoryModule(RepositoryModule repositoryModule);
        @BindsInstance
        Builder siteModule(SiteModule siteModule);
        @BindsInstance
        Builder parserModule(ParserModule parserModule);

        ApplicationComponent build();
    }
}
