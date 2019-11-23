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
import com.android.volley.toolbox.Volley;
import com.github.adamantcheese.chan.BuildConfig;
import com.github.adamantcheese.chan.core.cache.FileCache;
import com.github.adamantcheese.chan.core.net.ProxiedHurlStack;
import com.github.adamantcheese.chan.core.settings.ChanSettings;
import com.github.adamantcheese.chan.core.site.http.HttpCallManager;
import com.github.adamantcheese.chan.utils.Logger;
import com.github.k1rakishou.fsaf.FileManager;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

import static com.github.adamantcheese.chan.utils.AndroidUtils.getApplicationLabel;

@Module
public class NetModule {
    public static final String USER_AGENT = getApplicationLabel() + "/" + BuildConfig.VERSION_NAME;

    @Provides
    @Singleton
    public RequestQueue provideRequestQueue(Context applicationContext) {
        Logger.d(AppModule.DI_TAG, "Request queue");
        return Volley.newRequestQueue(applicationContext, new ProxiedHurlStack());
    }

    @Provides
    @Singleton
    public FileCache provideFileCache(
            Context applcationContext,
            FileManager fileManager,
            ProxiedOkHttpClient proxiedOkHttpClient
    ) {
        Logger.d(AppModule.DI_TAG, "File cache");
        return new FileCache(getCacheDir(applcationContext), fileManager, proxiedOkHttpClient);
    }

    private File getCacheDir(Context applicationContext) {
        // See also res/xml/filepaths.xml for the fileprovider.
        if (applicationContext.getExternalCacheDir() != null) {
            return applicationContext.getExternalCacheDir();
        } else {
            return applicationContext.getCacheDir();
        }
    }

    @Provides
    @Singleton
    public HttpCallManager provideHttpCallManager(OkHttpClient okHttpClient) {
        Logger.d(AppModule.DI_TAG, "Http call manager");
        return new HttpCallManager(okHttpClient);
    }

    @Provides
    @Singleton
    public ProxiedOkHttpClient provideBasicOkHttpClient() {
        Logger.d(AppModule.DI_TAG, "OkHTTP client");
        return new ProxiedOkHttpClient();
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .proxy(ChanSettings.getProxy())
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .callTimeout(20, TimeUnit.SECONDS)
                .build();
    }

    //this is basically the same as OkHttpClient, but with a singleton for a proxy instance
    public class ProxiedOkHttpClient extends OkHttpClient {
        private OkHttpClient proxiedClient;

        public OkHttpClient getProxiedClient() {
            if (proxiedClient == null) {
                proxiedClient = newBuilder()
                        .proxy(ChanSettings.getProxy())
                        .connectTimeout(20, TimeUnit.SECONDS)
                        .readTimeout(20, TimeUnit.SECONDS)
                        .writeTimeout(20, TimeUnit.SECONDS)
                        .callTimeout(20, TimeUnit.SECONDS)
                        .build();
            }
            return proxiedClient;
        }
    }

}
