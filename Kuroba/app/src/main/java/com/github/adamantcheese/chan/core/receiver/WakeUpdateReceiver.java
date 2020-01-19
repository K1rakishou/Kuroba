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
package com.github.adamantcheese.chan.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.github.adamantcheese.chan.core.manager.WakeManager;
import com.github.adamantcheese.chan.utils.Logger;

import java.text.DateFormat;
import java.util.Date;

import javax.inject.Inject;

import static com.github.adamantcheese.chan.Chan.inject;

public class WakeUpdateReceiver
        extends BroadcastReceiver {
    private static final String TAG = "WakeUpdateReceiver";

    @Inject
    WakeManager wakeManager;

    public WakeUpdateReceiver() {
        inject(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.d(TAG, "Alarm trigger at " + DateFormat.getTimeInstance().format(new Date()));
        wakeManager.onBroadcastReceived();
    }
}
