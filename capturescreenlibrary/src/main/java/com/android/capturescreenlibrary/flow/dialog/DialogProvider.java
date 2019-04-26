package com.android.capturescreenlibrary.flow.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

public interface DialogProvider {

    String  ALERT_DIALOG_TITLE           = "Shake detected!";
    String  ALERT_DIALOG_MESSAGE         = "Would you like to report a bug?";
    String  ALERT_DIALOG_POSITIVE_BUTTON = "Report";
    String  ALERT_DIALOG_NEGATIVE_BUTTON = "Cancel";
    boolean ALERT_DIALOG_CANCELABLE      = false;

    @NonNull
    Dialog getAlertDialog(
            @NonNull final Activity activity,
            @NonNull final DialogInterface.OnClickListener reportBugClickListener);

}
