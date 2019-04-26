package com.android.capturescreenlibrary.flow.email.screenshot;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;

import com.android.capturescreenlibrary.utilities.Logger;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import rx.Observable;
import rx.Subscriber;

/* default */ final class ScreenshotUriObservable {

    private static final String AUTHORITY_SUFFIX = ".bugshaker.fileprovider";
    private static final String SCREENSHOTS_DIRECTORY_NAME = "bugshaker-internal";
    private static final String SCREENSHOT_FILE_NAME = "latest-screenshot.jpg";
    private static final int JPEG_COMPRESSION_QUALITY = 90;

    /* default */ static Observable<Uri> create(
            @NonNull final Context applicationContext,
            @NonNull final Bitmap bitmap,
            @NonNull final Logger logger) {

        return Observable.create(new Observable.OnSubscribe<Uri>() {
            @Override
            public void call(final Subscriber<? super Uri> subscriber) {
                OutputStream fileOutputStream = null;

                try {
                    final File screenshotFile = getScreenshotFile(applicationContext);

                    fileOutputStream = new BufferedOutputStream(
                            new FileOutputStream(screenshotFile));

                    bitmap.compress(
                            Bitmap.CompressFormat.JPEG,
                            JPEG_COMPRESSION_QUALITY,
                            fileOutputStream);

                    fileOutputStream.flush();

                    logger.d("Screenshot saved to " + screenshotFile.getAbsolutePath());

                    final Uri result = FileProvider.getUriForFile(
                            applicationContext,
                            applicationContext.getPackageName() + AUTHORITY_SUFFIX,
                            screenshotFile);

                    logger.d("Screenshot Uri created: " + result);

                    subscriber.onNext(result);
                    subscriber.onCompleted();
                } catch (final IOException e) {
                    subscriber.onError(e);
                } finally {
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (final IOException ignored) {
                            // We did our best...
                            logger.e("Failed to close OutputStream.");
                        }
                    }
                }
            }
        });
    }

    private ScreenshotUriObservable() {
        // This constructor intentionally left blank.
    }

    private static File getScreenshotFile(@NonNull final Context applicationContext) {
        final File screenshotsDir = new File(applicationContext.getFilesDir(), SCREENSHOTS_DIRECTORY_NAME);

        //noinspection ResultOfMethodCallIgnored
        screenshotsDir.mkdirs();

        return new File(screenshotsDir, SCREENSHOT_FILE_NAME);
    }

}
