package com.android.capturescreenlibrary.utilities;

import android.support.annotation.NonNull;
import android.util.Log;

public final class Logger {

    private static final String TAG = "CaptureScreen-Library";

    private final boolean loggingEnabled;

    public Logger(final boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
    }

    public void d(@NonNull final CharSequence message) {
        if (loggingEnabled) {
            Log.d(TAG, message.toString());
        }
    }

    public void e(@NonNull final CharSequence message) {
        if (loggingEnabled) {
            Log.e(TAG, message.toString());
        }
    }

    public void printStackTrace(@NonNull final Throwable throwable) {
        if (loggingEnabled) {
            Log.e(TAG, "Logging caught exception", throwable);
        }
    }

}
