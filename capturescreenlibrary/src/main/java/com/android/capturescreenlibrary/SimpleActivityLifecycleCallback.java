package com.android.capturescreenlibrary;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

abstract class SimpleActivityLifecycleCallback implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(final Activity activity, final Bundle savedInstanceState) {
        // This method intentionally left blank
    }

    @Override
    public void onActivityResumed(final Activity activity) {
        // This method intentionally left blank
    }

    @Override
    public void onActivityStarted(final Activity activity) {
        // This method intentionally left blank
    }

    @Override
    public void onActivityPaused(final Activity activity) {
        // This method intentionally left blank
    }

    @Override
    public void onActivityStopped(final Activity activity) {
        // This method intentionally left blank
    }

    @Override
    public void onActivitySaveInstanceState(final Activity activity, final Bundle outState) {
        // This method intentionally left blank
    }

    @Override
    public void onActivityDestroyed(final Activity activity) {
        // This method intentionally left blank
    }

}
