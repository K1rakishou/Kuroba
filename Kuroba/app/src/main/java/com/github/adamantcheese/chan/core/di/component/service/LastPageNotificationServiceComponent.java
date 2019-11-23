package com.github.adamantcheese.chan.core.di.component.service;

import com.github.adamantcheese.chan.core.di.scope.PerService;
import com.github.adamantcheese.chan.ui.service.LastPageNotification;

import dagger.Subcomponent;

@PerService
@Subcomponent()
public interface LastPageNotificationServiceComponent {
    void inject(LastPageNotification lastPageNotification);
}
