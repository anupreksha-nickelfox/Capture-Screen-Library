package com.android.capturescreenlibrary.flow.email.screenshot;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.android.capturescreenlibrary.utilities.Logger;

import rx.Observable;

public final class BasicScreenShotProvider extends BaseScreenshotProvider {

    public BasicScreenShotProvider(
            @NonNull final Context applicationContext,
            @NonNull final Logger logger) {

        super(applicationContext, logger);
    }

    @NonNull
    @Override
    public Observable<Bitmap> getScreenshotBitmap(@NonNull final Activity activity) {
        return getNonMapViewsBitmap(activity);
    }

}
