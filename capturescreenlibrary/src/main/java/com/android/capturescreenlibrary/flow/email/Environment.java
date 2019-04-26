package com.android.capturescreenlibrary.flow.email;

import android.os.Build;
import android.support.annotation.NonNull;

public final class Environment {

    @NonNull
    public String getAndroidVersionName() {
        return Build.VERSION.RELEASE;
    }

    public int getAndroidVersionCode() {
        return Build.VERSION.SDK_INT;
    }

}
