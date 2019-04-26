package com.android.capturescreenlibrary.flow.email.screenshot;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;

import rx.Observable;

public interface ScreenshotProvider {

    @NonNull
    Observable<Uri> getScreenshotUri(@NonNull final Activity activity);

}
