package com.github.adamantcheese.chan.ui.controller;

import android.content.Context;

import com.github.adamantcheese.chan.R;
import com.github.adamantcheese.chan.core.database.DatabaseManager;
import com.github.adamantcheese.chan.core.manager.ThreadSaveManager;
import com.github.adamantcheese.chan.core.model.orm.Pin;
import com.github.adamantcheese.chan.core.model.orm.PinType;
import com.github.adamantcheese.chan.core.settings.ChanSettings;
import com.github.adamantcheese.chan.ui.settings.BooleanSettingView;
import com.github.adamantcheese.chan.ui.settings.SettingView;
import com.github.adamantcheese.chan.ui.settings.SettingsController;
import com.github.adamantcheese.chan.ui.settings.SettingsGroup;
import com.github.adamantcheese.chan.utils.IOUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.github.adamantcheese.chan.Chan.inject;

public class ExperimentalSettingsController extends SettingsController {
    public ExperimentalSettingsController(Context context) {
        super(context);
    }

    private BooleanSettingView incrementalThreadDownloadingSetting;

    @Inject
    ThreadSaveManager threadSaveManager;
    @Inject
    DatabaseManager databaseManager;

    @Override
    public void onCreate() {
        super.onCreate();

        navigation.setTitle(R.string.settings_experimental_settings_title);

        setupLayout();
        populatePreferences();
        buildPreferences();

        inject(this);
    }

    @Override
    public void onPreferenceChange(SettingView item) {
        super.onPreferenceChange(item);

        if (item == incrementalThreadDownloadingSetting && !ChanSettings.incrementalThreadDownloadingEnabled.get()) {
            cancelAllDownloads();
        }
    }

    private void cancelAllDownloads() {
        threadSaveManager.cancelAllDownloading();

        databaseManager.runTask(() -> {
            List<Pin> pins = databaseManager.getDatabasePinManager().getPins().call();
            if (pins.isEmpty()) {
                return null;
            }

            List<Pin> downloadPins = new ArrayList<>(10);

            for (Pin pin : pins) {
                if (PinType.hasDownloadFlag(pin.pinType)) {
                    downloadPins.add(pin);
                }
            }

            if (downloadPins.isEmpty()) {
                return null;
            }

            databaseManager.getDatabaseSavedThreadManager().deleteAllSavedThreads().call();

            for (Pin pin : downloadPins) {
                pin.pinType = PinType.removeDownloadNewPostsFlag(pin.pinType);

                if (PinType.hasWatchNewPostsFlag(pin.pinType)) {
                    continue;
                }

                // We don't want to delete all of the users's bookmarks so we just change their
                // types to WatchNewPosts
                pin.pinType = PinType.addWatchNewPostsFlag(pin.pinType);
            }

            databaseManager.getDatabasePinManager().updatePins(downloadPins).call();

            for (Pin pin : downloadPins) {
                String threadSubDir = ThreadSaveManager.getThreadSubDir(pin.loadable);
                File threadSaveDir = new File(ChanSettings.saveLocation.get(), threadSubDir);

                if (!threadSaveDir.exists() || !threadSaveDir.isDirectory()) {
                    continue;
                }

                IOUtils.deleteDirWithContents(threadSaveDir);
            }

            return null;
        });
    }

    private void populatePreferences() {
        SettingsGroup group = new SettingsGroup(context.getString(R.string.experimental_settings_group));

        incrementalThreadDownloadingSetting = new BooleanSettingView(this,
                ChanSettings.incrementalThreadDownloadingEnabled,
                context.getString(R.string.incremental_thread_downloading_title),
                context.getString(R.string.incremental_thread_downloading_description));

        requiresRestart.add(incrementalThreadDownloadingSetting);
        group.add(incrementalThreadDownloadingSetting);

        groups.add(group);
    }
}
