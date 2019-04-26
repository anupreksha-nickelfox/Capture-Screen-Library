package com.android.capturescreenlibrary.flow.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

public final class AppCompatDialogProvider implements DialogProvider {

    @NonNull
    @Override
    public Dialog getAlertDialog(
            @NonNull final Activity activity,
            @NonNull final DialogInterface.OnClickListener reportBugClickListener) {

        return new AlertDialog.Builder(activity)
                .setTitle(ALERT_DIALOG_TITLE)
                .setMessage(ALERT_DIALOG_MESSAGE)
                .setPositiveButton(ALERT_DIALOG_POSITIVE_BUTTON, reportBugClickListener)
                .setNegativeButton(ALERT_DIALOG_NEGATIVE_BUTTON, null)
                .setCancelable(ALERT_DIALOG_CANCELABLE)
                .create();
    }

}
