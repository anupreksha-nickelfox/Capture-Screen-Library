package com.android.capturescreenlibrary.flow.email.screenshot.maps;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

/* default */ final class LocatedBitmap {

    @NonNull
    private final Bitmap bitmap;

    @NonNull
    private final int[] location;

    /* default */ LocatedBitmap(@NonNull final Bitmap bitmap, @NonNull final int[] location) {
        this.bitmap = bitmap;
        this.location = location;
    }

    @NonNull
    /* default */ Bitmap getBitmap() {
        return bitmap;
    }

    @NonNull
    /* default */ int[] getLocation() {
        return location;
    }

}
