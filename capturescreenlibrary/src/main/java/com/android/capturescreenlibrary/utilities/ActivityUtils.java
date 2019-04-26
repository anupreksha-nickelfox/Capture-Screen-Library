package com.android.capturescreenlibrary.utilities;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;

public final class ActivityUtils {

    public static Window getWindow(@NonNull final Activity activity) {
        return activity.getWindow();
    }

    public static View getRootView(@NonNull final Activity activity) {
        return getWindow(activity).getDecorView().getRootView();
    }

    private ActivityUtils() {

    }

}
