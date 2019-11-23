package com.github.adamantcheese.chan.ui.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.github.adamantcheese.chan.R;
import com.github.adamantcheese.chan.controller.Controller;
import com.github.adamantcheese.chan.core.manager.ReplyManager;
import com.github.adamantcheese.chan.core.model.orm.Loadable;
import com.github.adamantcheese.chan.core.presenter.ImageReencodingPresenter;
import com.github.adamantcheese.chan.core.settings.ChanSettings;
import com.github.adamantcheese.chan.core.site.http.Reply;
import com.github.adamantcheese.chan.ui.controller.ImageOptionsController;
import com.github.adamantcheese.chan.ui.controller.ImageReencodeOptionsController;
import com.github.adamantcheese.chan.ui.theme.ThemeHelper;
import com.google.gson.Gson;

public class ImageOptionsHelper implements
        ImageOptionsController.ImageOptionsControllerCallbacks,
        ImageReencodeOptionsController.ImageReencodeOptionsCallbacks {
    private Context context;
    private ImageOptionsController imageOptionsController = null;
    private ImageReencodeOptionsController imageReencodeOptionsController = null;
    private final ImageReencodingHelperCallback callbacks;
    private final ReplyManager replyManager;
    private final Gson gson;
    private final ThemeHelper themeHelper;

    private ImageReencodingPresenter.ImageOptions lastImageOptions;

    public ImageOptionsHelper(
            Context context,
            ReplyManager replyManager,
            Gson gson,
            ThemeHelper themeHelper,
            ImageReencodingHelperCallback callbacks
    ) {
        this.context = context;
        this.replyManager = replyManager;
        this.gson = gson;
        this.themeHelper = themeHelper;
        this.callbacks = callbacks;
    }

    public void showController(Loadable loadable, boolean supportsReencode) {
        if (imageOptionsController == null) {
            try { //load up the last image options every time this controller is created
                lastImageOptions = gson.fromJson(
                        ChanSettings.lastImageOptions.get(),
                        ImageReencodingPresenter.ImageOptions.class
                );
            } catch (Exception ignored) {
                lastImageOptions = null;
            }

            imageOptionsController = new ImageOptionsController(
                    context,
                    replyManager,
                    this,
                    this,
                    loadable,
                    lastImageOptions,
                    supportsReencode
            );

            callbacks.presentReencodeOptionsController(imageOptionsController);
        }
    }

    public void pop() {
        //first we have to pop the imageReencodeOptionsController
        if (imageReencodeOptionsController != null) {
            imageReencodeOptionsController.stopPresenting();
            imageReencodeOptionsController = null;
            return;
        }

        if (imageOptionsController != null) {
            imageOptionsController.stopPresenting();
            imageOptionsController = null;
        }
    }

    @Override
    public void onReencodeOptionClicked(@Nullable Bitmap.CompressFormat imageFormat, @Nullable Pair<Integer, Integer> dims) {
        if (imageReencodeOptionsController == null && imageFormat != null && dims != null) {
            ImageReencodingPresenter.ReencodeSettings lastOptions = lastImageOptions != null
                    ? lastImageOptions.getReencodeSettings()
                    : null;

            imageReencodeOptionsController = new ImageReencodeOptionsController(
                    context,
                    this,
                    this,
                    imageFormat,
                    dims,
                    lastOptions,
                    themeHelper
            );

            callbacks.presentReencodeOptionsController(imageReencodeOptionsController);
        } else {
            Toast.makeText(context, context.getString(R.string.image_reencode_format_error), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onImageOptionsApplied(Reply reply, boolean filenameRemoved) {
        callbacks.onImageOptionsApplied(reply, filenameRemoved);
    }

    @Override
    public void onCanceled() {
        if (imageOptionsController != null) {
            imageOptionsController.onReencodingCanceled();
        }

        pop();
    }

    @Override
    public void onOk(ImageReencodingPresenter.ReencodeSettings reencodeSettings) {
        if (imageOptionsController != null) {
            if (reencodeSettings.isDefault()) {
                imageOptionsController.onReencodingCanceled();
            } else {
                imageOptionsController.onReencodeOptionsSet(reencodeSettings);
            }
        }

        pop();
    }

    public interface ImageReencodingHelperCallback {
        void presentReencodeOptionsController(Controller controller);

        void onImageOptionsApplied(Reply reply, boolean filenameRemoved);
    }
}
