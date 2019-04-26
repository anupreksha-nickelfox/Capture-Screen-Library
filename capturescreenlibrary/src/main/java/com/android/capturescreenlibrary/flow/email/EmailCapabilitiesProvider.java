package com.android.capturescreenlibrary.flow.email;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;


import com.android.capturescreenlibrary.utilities.Logger;

import java.util.ArrayList;
import java.util.List;

public final class EmailCapabilitiesProvider {

    private static final String[] DUMMY_EMAIL_ADDRESSES = new String[] {"someone@example.com"};
    private static final String DUMMY_EMAIL_SUBJECT_LINE = "Any Subject Line";
    private static final String DUMMY_EMAIL_BODY = "Any Body";
    private static final Uri DUMMY_EMAIL_URI = Uri.EMPTY;

    @NonNull
    private final PackageManager packageManager;

    @NonNull
    private final GenericEmailIntentProvider genericEmailIntentProvider;

    @NonNull
    private final Logger logger;

    public EmailCapabilitiesProvider(
            @NonNull final PackageManager packageManager,
            @NonNull final GenericEmailIntentProvider genericEmailIntentProvider,
            @NonNull final Logger logger) {

        this.packageManager = packageManager;
        this.genericEmailIntentProvider = genericEmailIntentProvider;
        this.logger = logger;
    }

    public boolean canSendEmails() {
        logger.d("Checking for email apps...");

        final List<ResolveInfo> emailAppInfoList = getEmailAppList();

        if (emailAppInfoList.isEmpty()) {
            logger.d("No email apps found.");
            return false;
        }

        logEmailAppNames("Available email apps: ", emailAppInfoList);
        return true;
    }

    /* default */ boolean canSendEmailsWithAttachments() {
        logger.d("Checking for email apps that can send attachments...");

        final List<ResolveInfo> emailAppInfoList = getEmailWithAttachmentAppList();

        if (emailAppInfoList.isEmpty()) {
            logger.d("No email apps can send attachments.");
            return false;
        }

        logEmailAppNames("Available email apps that can send attachments: ", emailAppInfoList);
        return true;
    }

    @NonNull
    private List<ResolveInfo> getEmailAppList() {
        final Intent queryIntent = genericEmailIntentProvider.getEmailIntent(
                DUMMY_EMAIL_ADDRESSES, DUMMY_EMAIL_SUBJECT_LINE, DUMMY_EMAIL_BODY);

        return packageManager.queryIntentActivities(
                queryIntent, PackageManager.MATCH_DEFAULT_ONLY);
    }

    @NonNull
    private List<ResolveInfo> getEmailWithAttachmentAppList() {
        final Intent queryIntent = genericEmailIntentProvider.getEmailWithAttachmentIntent(
                DUMMY_EMAIL_ADDRESSES, DUMMY_EMAIL_SUBJECT_LINE, DUMMY_EMAIL_BODY, DUMMY_EMAIL_URI);

        return packageManager.queryIntentActivities(
                queryIntent, PackageManager.MATCH_DEFAULT_ONLY);
    }

    private void logEmailAppNames(
            @NonNull final String prefix,
            @NonNull final List<ResolveInfo> emailAppInfoList) {

        final List<CharSequence> emailAppNames = new ArrayList<>();
        for (final ResolveInfo emailAppInfo: emailAppInfoList) {
            emailAppNames.add(emailAppInfo.loadLabel(packageManager));
        }

        final String emailAppInfoString = TextUtils.join(", ", emailAppNames);
        logger.d(prefix + emailAppInfoString);
    }

}
