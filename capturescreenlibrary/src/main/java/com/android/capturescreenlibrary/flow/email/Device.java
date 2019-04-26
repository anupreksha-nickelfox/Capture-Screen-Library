package com.android.capturescreenlibrary.flow.email;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import static java.util.Locale.US;

public final class Device {

    @NonNull
    private static String getDensityBucketString(@NonNull final DisplayMetrics displayMetrics) {
        switch (displayMetrics.densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                return "ldpi";
            case DisplayMetrics.DENSITY_MEDIUM:
                return "mdpi";
            case DisplayMetrics.DENSITY_HIGH:
                return "hdpi";
            case DisplayMetrics.DENSITY_XHIGH:
                return "xhdpi";
            case DisplayMetrics.DENSITY_XXHIGH:
                return "xxhdpi";
            case DisplayMetrics.DENSITY_XXXHIGH:
                return "xxxhdpi";
            case DisplayMetrics.DENSITY_TV:
                return "tvdpi";
            default:
                return "Unknown";
        }
    }

    @NonNull
    private final String resolution;

    @NonNull
    private final String actualDensity;

    @NonNull
    private final String densityBucket;

    public Device(@NonNull final Context context) {
        final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        resolution = displayMetrics.heightPixels + "x" + displayMetrics.widthPixels;
        actualDensity = displayMetrics.densityDpi + "dpi";
        densityBucket = getDensityBucketString(displayMetrics);
    }

    @NonNull
    public String getManufacturer() {
        return Build.MANUFACTURER.toUpperCase(US);
    }

    @NonNull
    public String getModel() {
        return Build.MODEL.toUpperCase(US);
    }

    @NonNull
    public String getResolution() {
        return resolution;
    }

    @NonNull
    public String getActualDensity() {
        return actualDensity;
    }

    @NonNull
    public String getDensityBucket() {
        return densityBucket;
    }

}
