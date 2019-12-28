package com.github.adamantcheese.chan.core.cache.downloader

import com.github.adamantcheese.chan.core.cache.FileCacheDataSource
import com.github.adamantcheese.chan.core.cache.FileCacheListener
import com.github.adamantcheese.chan.core.cache.WebmStreamingSource
import com.github.adamantcheese.chan.utils.Logger
import java.lang.ref.WeakReference
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

/**
 * ThreadSafe
 * */
class CancelableDownload(
        val url: String,
        private val requestCancellationThread: ExecutorService,
        // If true that means that this CancelableDownload belongs to some kind of a batched request
        // i.e. either thread media prefetch or gallery download
        val isPartOfBatchDownload: AtomicBoolean = AtomicBoolean(false)
) {
    private val state: AtomicReference<DownloadState> = AtomicReference(DownloadState.Running)
    private val callbacks: MutableSet<WeakReference<FileCacheListener>> = mutableSetOf()
    /**
     * This callbacks are used to cancel a lot of things like the HEAD request, the get response
     * body request and response body read loop.
     * */
    private val disposeFuncList: MutableList<() -> Unit> = mutableListOf()

    fun isRunning(): Boolean = state.get() == DownloadState.Running
    fun getState(): DownloadState = state.get()

    @Synchronized
    fun addCallback(callback: FileCacheListener) {
        if (state.get() != DownloadState.Running) {
            return
        }

        callbacks.add(WeakReference(callback))
    }

    @Synchronized
    fun forEachCallback(func: FileCacheListener.() -> Unit) {
        callbacks.forEach { callbackRef ->
            callbackRef.get()?.let { callback -> func(callback) }
        }
    }

    @Synchronized
    fun clearCallbacks() {
        callbacks.clear()
    }

    @Synchronized
    fun addDisposeFuncList(disposeFunc: () -> Unit) {
        disposeFuncList += disposeFunc
    }

    /**
     * Use this to cancel prefetches. You can't cancel them via the regular cancel() method
     * to avoid canceling prefetches when swiping through the images in the album viewer.
     * */
    fun cancelPrefetch() {
        cancel(true)
    }

    /**
     * A regular [cancel] method that cancels active downloads but not prefetch downloads.
     * */
    fun cancel() {
        cancel(false)
    }

    /**
     * Similar to [cancel] but does not delete the output file. Used by [WebmStreamingSource]
     * to stop the download without deleting the output which is then getting transferred into
     * [FileCacheDataSource]
     * */
    fun stop() {
        if (!state.compareAndSet(DownloadState.Running, DownloadState.Stopped)) {
            // Already canceled or stopped
            return
        }

        // TODO(FileCacheV2): wtf do I do in case of this file/image being prefetched?

        dispose()
    }

    private fun cancel(canCancelBatchDownloads: Boolean) {
        if (!state.compareAndSet(DownloadState.Running, DownloadState.Canceled)) {
            // Already canceled or stopped
            return
        }

        if (isPartOfBatchDownload.get() && !canCancelBatchDownloads) {
            // When prefetching media in a thread and viewing images in the same thread at the
            // same time we may accidentally cancel a prefetch download which we don't want.
            // We only want to cancel prefetch downloads when exiting a thread, not when swiping
            // through the images in the album viewer.
            return
        }

        dispose()
    }

    private fun dispose() {
        // We need to cancel the network requests on a background thread because otherwise it will
        // throw NetworkOnMainThread exception.
        // We also want it to be blocking so that we won't end up in a race condition when you
        // cancel a download and then start a new one with the same url right away. We need a little
        // bit of time for it to get really canceled.

        try {
            requestCancellationThread.submit {
                synchronized(this) {
                    // Cancel downloads
                    disposeFuncList.forEach { func ->
                        try {
                            func.invoke()
                        } catch (error: Throwable) {
                            Logger.e(TAG, "Unhandled error in dispose function")
                        }
                    }

                    disposeFuncList.clear()
                }

                Logger.d(TAG, "Cancelling file download request, url = $url")
            }
            // We use timeout here just in case to not get deadlocked
            .get(MAX_CANCELLATION_WAIT_TIME_SECONDS, TimeUnit.SECONDS)
        } catch (error: Throwable) {
            // Catch all the exceptions so we can clear this request afterwards
            Logger.e(TAG, "Error while trying to dispose of a request for url = ($url)")
        }
    }

    companion object {
        private const val TAG = "CancelableDownload"
        private const val MAX_CANCELLATION_WAIT_TIME_SECONDS = 10L
    }
}