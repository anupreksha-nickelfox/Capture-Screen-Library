package com.android.capturescreenlibrary.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

public final class Toaster {

    @NonNull
    private final Context applicationContext;

    public Toaster(@NonNull final Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void toast(@NonNull final String message) {
        Toast.makeText(
                applicationContext,
                message,
                Toast.LENGTH_LONG)
                .show();
    }

}
