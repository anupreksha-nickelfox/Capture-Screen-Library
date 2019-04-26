package com.android.capturescreenlibrary.flow.email;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class FeedbackEmailIntentProvider {

    private static final String DEFAULT_EMAIL_SUBJECT_LINE_SUFFIX = " Android App Feedback";

    @NonNull
    private final GenericEmailIntentProvider genericEmailIntentProvider;

    @NonNull
    private final App app;

    @NonNull
    private final Environment environment;

    @NonNull
    private final Device device;

    public FeedbackEmailIntentProvider(
            @NonNull final Context context,
            @NonNull final GenericEmailIntentProvider genericEmailIntentProvider) {

        this.genericEmailIntentProvider = genericEmailIntentProvider;
        this.app = new App(context);
        this.environment = new Environment();
        this.device = new Device(context);
    }

    @NonNull
    /* default */ Intent getFeedbackEmailIntent(
            @NonNull final String[] emailAddresses,
            @Nullable final String userProvidedEmailSubjectLine) {

        final String emailSubjectLine = getEmailSubjectLine(userProvidedEmailSubjectLine);
        final String emailBody = getApplicationInfoString(app, environment, device);

        return genericEmailIntentProvider
                .getEmailIntent(emailAddresses, emailSubjectLine, emailBody);
    }

    @NonNull
    /* default */ Intent getFeedbackEmailIntent(
            @NonNull final String[] emailAddresses,
            @Nullable final String userProvidedEmailSubjectLine,
            @NonNull final Uri screenshotUri) {

        final String emailSubjectLine = getEmailSubjectLine(userProvidedEmailSubjectLine);
        final String emailBody = getApplicationInfoString(app, environment, device);

        return genericEmailIntentProvider
                .getEmailWithAttachmentIntent(
                        emailAddresses, emailSubjectLine, emailBody, screenshotUri);
    }

    @NonNull
    private String getApplicationInfoString(
            @NonNull final App app,
            @NonNull final Environment environment,
            @NonNull final Device device) {

        final String androidVersionString = String.format(
                "%s (%s)", environment.getAndroidVersionName(), environment.getAndroidVersionCode());

        final String appVersionString = String.format("%s (%s)", app.getVersionName(), app.getVersionCode());

        // @formatter:off
        return    "Time Stamp: " + getCurrentUtcTimeStringForDate(new Date()) + "\n"
                + "App Version: " + appVersionString + "\n"
                + "Install Source: " + app.getInstallSource() + "\n"
                + "Android Version: " + androidVersionString + "\n"
                + "Device Manufacturer: " + device.getManufacturer() + "\n"
                + "Device Model: " + device.getModel() + "\n"
                + "Display Resolution: " + device.getResolution() + "\n"
                + "Display Density (Actual): " + device.getActualDensity() + "\n"
                + "Display Density (Bucket) " + device.getDensityBucket() + "\n"
                + "---------------------\n\n";
        // @formatter:on
    }

    @NonNull
    private String getEmailSubjectLine(@Nullable final String userProvidedEmailSubjectLine) {
        if (userProvidedEmailSubjectLine != null) {
            return userProvidedEmailSubjectLine;
        }

        return app.getName() + DEFAULT_EMAIL_SUBJECT_LINE_SUFFIX;
    }

    @NonNull
    private String getCurrentUtcTimeStringForDate(final Date date) {
        final SimpleDateFormat simpleDateFormat
                = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss z", Locale.getDefault());

        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        return simpleDateFormat.format(date);
    }

}
