package com.android.capturescreenlibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;

import com.android.capturescreenlibrary.flow.dialog.AlertDialogType;
import com.android.capturescreenlibrary.flow.dialog.AppCompatDialogProvider;
import com.android.capturescreenlibrary.flow.dialog.DialogProvider;
import com.android.capturescreenlibrary.flow.dialog.NativeDialogProvider;
import com.android.capturescreenlibrary.flow.email.EmailCapabilitiesProvider;
import com.android.capturescreenlibrary.flow.email.FeedbackEmailFlowManager;
import com.android.capturescreenlibrary.flow.email.FeedbackEmailIntentProvider;
import com.android.capturescreenlibrary.flow.email.GenericEmailIntentProvider;
import com.android.capturescreenlibrary.flow.email.screenshot.BasicScreenShotProvider;
import com.android.capturescreenlibrary.flow.email.screenshot.ScreenshotProvider;
import com.android.capturescreenlibrary.flow.email.screenshot.maps.MapScreenshotProvider;
import com.android.capturescreenlibrary.utilities.Logger;
import com.android.capturescreenlibrary.utilities.Toaster;
import com.squareup.seismic.ShakeDetector;

import static android.content.Context.SENSOR_SERVICE;

public final class CaptureScreen implements ShakeDetector.Listener {

    private static final String RECONFIGURATION_EXCEPTION_MESSAGE =
            "Configuration must be completed before calling assemble or start";

    @SuppressLint("StaticFieldLeak") // we're holding the application context.
    private static CaptureScreen sharedInstance;

    private final Application application;
    private EmailCapabilitiesProvider emailCapabilitiesProvider;
    private FeedbackEmailFlowManager feedbackEmailFlowManager;
    private Logger logger;

    // Instance configuration:
    private String[] emailAddresses;
    private String emailSubjectLine;
    private AlertDialogType alertDialogType = AlertDialogType.NATIVE;
    private boolean ignoreFlagSecure        = false;
    private boolean loggingEnabled          = false;

    // Instance configuration state:
    private boolean assembled      = false;
    private boolean startAttempted = false;

    private final SimpleActivityLifecycleCallback simpleActivityLifecycleCallback = new SimpleActivityLifecycleCallback() {
        @Override
        public void onActivityResumed(final Activity activity) {
            feedbackEmailFlowManager.onActivityResumed(activity);
        }

        @Override
        public void onActivityStopped(final Activity activity) {
            feedbackEmailFlowManager.onActivityStopped();
        }
    };

    /**
     * @param application the embedding application
     * @return the singleton <code>CaptureScreen</code> instance
     */
    @NonNull
    public static CaptureScreen get(@NonNull final Application application) {
        synchronized (CaptureScreen.class) {
            if (sharedInstance == null) {
                sharedInstance = new CaptureScreen(application);
            }
        }

        return sharedInstance;
    }

    private CaptureScreen(@NonNull final Application application) {
        this.application = application;
    }

    /**
     * (Required) Defines one or more email addresses to send bug reports to. This method MUST be called before calling
     * <code>assemble</code>. This method CANNOT be called after calling <code>assemble</code> or <code>start</code>.
     *
     * @param emailAddresses one or more email addresses
     * @return the current <code>CaptureScreen</code> instance (to allow for method chaining)
     */
    @NonNull
    public CaptureScreen setEmailAddresses(@NonNull final String... emailAddresses) {
        if (assembled || startAttempted) {
            throw new IllegalStateException(
                    "Configuration must be complete before calling assemble or start");
        }

        this.emailAddresses = emailAddresses;
        return this;
    }

    /**
     * (Optional) Defines a custom subject line to use for all bug reports. By default, reports will use the string
     * defined in <code>DEFAULT_SUBJECT_LINE</code>. This method CANNOT be called after calling <code>assemble</code> or
     * <code>start</code>.
     *
     * @param emailSubjectLine a custom email subject line
     * @return the current <code>CaptureScreen</code> instance (to allow for method chaining)
     */
    @NonNull
    public CaptureScreen setEmailSubjectLine(@NonNull final String emailSubjectLine) {
        if (assembled || startAttempted) {
            throw new IllegalStateException(RECONFIGURATION_EXCEPTION_MESSAGE);
        }

        this.emailSubjectLine = emailSubjectLine;
        return this;
    }

    /**
     * (Optional) Defines a dialog type (native/material) to present when a shake is detected. Native dialogs are used
     * by default. This method CANNOT be called after calling <code>assemble</code> or <code>start</code>.
     *
     * @param alertDialogType the dialog type to present
     * @return the current <code>CaptureScreen</code> instance (to allow for method chaining)
     */
    @NonNull
    public CaptureScreen setAlertDialogType(@NonNull final AlertDialogType alertDialogType) {
        if (assembled || startAttempted) {
            throw new IllegalStateException(RECONFIGURATION_EXCEPTION_MESSAGE);
        }

        this.alertDialogType = alertDialogType;
        return this;
    }

    /**
     * (Optional) Enables debug and error log messages. Logging is disabled by default. This method CANNOT be called
     * after calling <code>assemble</code> or <code>start</code>.
     *
     * @param loggingEnabled true if logging should be enabled; false otherwise
     * @return the current <code>CaptureScreen</code> instance (to allow for method chaining)
     */
    @NonNull
    public CaptureScreen setLoggingEnabled(final boolean loggingEnabled) {
        if (assembled || startAttempted) {
            throw new IllegalStateException(RECONFIGURATION_EXCEPTION_MESSAGE);
        }

        this.loggingEnabled = loggingEnabled;
        return this;
    }

    /**
     * (Optional) Choose whether to ignore the <code>FLAG_SECURE</code> <code>Window</code> flag when capturing
     * screenshots. This method CANNOT be called after calling <code>assemble</code> or <code>start</code>.
     *
     * @param ignoreFlagSecure true if screenshots should be allowed even when <code>FLAG_SECURE</code> is set on the
     *                         current <code>Window</code>; false otherwise
     * @return the current <code>CaptureScreen</code> instance (to allow for method chaining)
     */
    @NonNull
    public CaptureScreen setIgnoreFlagSecure(final boolean ignoreFlagSecure) {
        if (assembled || startAttempted) {
            throw new IllegalStateException(RECONFIGURATION_EXCEPTION_MESSAGE);
        }

        this.ignoreFlagSecure = ignoreFlagSecure;
        return this;
    }

    /**
     * (Required) Assembles dependencies based on provided configuration information. This method CANNOT be called more
     * than once. This method CANNOT be called after calling <code>start</code>.
     *
     * @return the current <code>CaptureScreen</code> instance (to allow for method chaining)
     */
    @NonNull
    public CaptureScreen assemble() {
        if (assembled) {
            logger.d("You have already assembled this CaptureScreen instance. Calling assemble again is a no-op.");
            return this;
        }

        if (startAttempted) {
            throw new IllegalStateException("You can only call assemble before calling start.");
        }

        logger = new Logger(loggingEnabled);

        final GenericEmailIntentProvider genericEmailIntentProvider = new GenericEmailIntentProvider();

        emailCapabilitiesProvider = new EmailCapabilitiesProvider(
                application.getPackageManager(),
                genericEmailIntentProvider,
                logger);

        feedbackEmailFlowManager = new FeedbackEmailFlowManager(
                application,
                emailCapabilitiesProvider,
                new Toaster(application),
                new ActivityReferenceManager(),
                new FeedbackEmailIntentProvider(application, genericEmailIntentProvider),
                getScreenshotProvider(),
                getAlertDialogProvider(),
                logger);

        assembled = true;
        return this;
    }

    /**
     * (Required) Start listening for device shaking. You MUST call <code>assemble</code> before calling this method.
     */
    public void start() {
        if (!assembled) {
            throw new IllegalStateException("You MUST call assemble before calling start.");
        }

        if (startAttempted) {
            logger.d("You have already attempted to start this CaptureScreen instance. Calling start "
                    + "again is a no-op.");

            return;
        }

        if (emailCapabilitiesProvider.canSendEmails()) {
            application.registerActivityLifecycleCallbacks(simpleActivityLifecycleCallback);

            final SensorManager sensorManager
                    = (SensorManager) application.getSystemService(SENSOR_SERVICE);
            final ShakeDetector shakeDetector = new ShakeDetector(this);

            final boolean didStart = shakeDetector.start(sensorManager);

            if (didStart) {
                logger.d("Shake detection successfully started!");
            } else {
                logger.e("Error starting shake detection: hardware does not support detection.");
            }
        } else {
            logger.e("Error starting shake detection: device cannot send emails.");
        }

        startAttempted = true;
    }

    @Override
    public void hearShake() {
        logger.d("Shake detected!");

        feedbackEmailFlowManager.startFlowIfNeeded(
                emailAddresses,
                emailSubjectLine,
                ignoreFlagSecure);
    }

    /**
     * @return a MapScreenshotProvider if the embedding application utilizes the Google Maps Android API, and a
     *         BasicScreenshotProvider otherwise
     */
    @NonNull
    private ScreenshotProvider getScreenshotProvider() {
        try {
            Class.forName(
                    "com.google.android.gms.maps.GoogleMap",
                    false,
                    CaptureScreen.class.getClassLoader());

            logger.d("Detected that embedding app includes Google Maps as a dependency.");

            return new MapScreenshotProvider(application, logger);
        } catch (final ClassNotFoundException e) {
            logger.d("Detected that embedding app does not include Google Maps as a dependency.");

            return new BasicScreenShotProvider(application, logger);
        }
    }

    @NonNull
    private DialogProvider getAlertDialogProvider() {
        if (alertDialogType == AlertDialogType.APP_COMPAT) {
            try {
                Class.forName(
                        "android.support.v7.app.AlertDialog",
                        false,
                        CaptureScreen.class.getClassLoader());

                logger.d("Using AppCompat dialogs as requested.");

                return new AppCompatDialogProvider();
            } catch (final ClassNotFoundException e) {
                logger.e("AppCompat dialogs requested, but class not found.");
                logger.e("Falling back to native dialogs.");

                return new NativeDialogProvider();
            }
        } else {
            logger.d("Using native dialogs as requested.");

            return new NativeDialogProvider();
        }
    }

}
