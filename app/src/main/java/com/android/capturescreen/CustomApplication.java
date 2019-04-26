package com.android.capturescreen;

import android.app.Application;

import com.android.capturescreenlibrary.CaptureScreen;
import com.android.capturescreenlibrary.flow.dialog.AlertDialogType;


public final class CustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CaptureScreen.get(this)
                 .setEmailAddresses("someone@example.com")
                 .setLoggingEnabled(BuildConfig.DEBUG)
                 .setAlertDialogType(AlertDialogType.APP_COMPAT)
                 .assemble()
                 .start();
    }

}
