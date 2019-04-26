package com.android.capturescreenlibrary.flow.email.screenshot;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.android.capturescreenlibrary.utilities.Logger;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public abstract class BaseScreenshotProvider implements ScreenshotProvider {

    @NonNull
    private final Context applicationContext;

    @NonNull
    private final Logger logger;

    public BaseScreenshotProvider(
            @NonNull final Context applicationContext,
            @NonNull final Logger logger) {

        this.applicationContext = applicationContext;
        this.logger = logger;
    }

    @NonNull
    public abstract Observable<Bitmap> getScreenshotBitmap(@NonNull final Activity activity);

    @NonNull
    @Override
    public final Observable<Uri> getScreenshotUri(@NonNull final Activity activity) {
        return getScreenshotBitmap(activity)
                .observeOn(Schedulers.io())
                .flatMap(new Func1<Bitmap, Observable<Uri>>() {
                    @Override
                    public Observable<Uri> call(final Bitmap bitmap) {
                        return ScreenshotUriObservable.create(applicationContext, bitmap, logger);
                    }
                });
    }

    protected final Observable<Bitmap> getNonMapViewsBitmap(@NonNull final Activity activity) {
        return NonMapViewsBitmapObservable.create(activity);
    }

}
