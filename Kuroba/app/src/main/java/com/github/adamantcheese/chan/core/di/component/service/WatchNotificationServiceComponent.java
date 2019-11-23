package com.github.adamantcheese.chan.core.di.component.service;

import com.github.adamantcheese.chan.core.di.scope.PerService;
import com.github.adamantcheese.chan.ui.service.WatchNotification;

import dagger.Subcomponent;

@PerService
@Subcomponent()
public interface WatchNotificationServiceComponent {
    void inject(WatchNotification watchNotification);
}
