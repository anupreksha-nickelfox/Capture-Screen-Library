package com.android.capturescreenlibrary.flow.email;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public abstract class InstallSource {

    /**
     * Package name for the Amazon App Store.
     */
    private static final String AMAZON_APP_STORE_PACKAGE_NAME = "com.amazon.venezia";

    /**
     * Package name for Amazon Underground.
     */
    private static final String AMAZON_UNDERGROUND_PACKAGE_NAME = "com.amazon.mshop.android";

    /**
     * Package name for the Google Play Store. Value can be verified here:
     * https://developers.google.com/android/reference/com/google/android/gms/common/GooglePlayServicesUtil.html#GOOGLE_PLAY_STORE_PACKAGE
     */
    private static final String GOOGLE_PLAY_STORE_PACKAGE_NAME = "com.android.vending";

    /**
     * Package name for Google's Package Installer. My guess is that apps installed using the
     * <a href="https://developer.android.com/reference/android/content/pm/PackageInstaller.html">PackageInstaller</a>
     * APIs will report as having been installed from this source. Since this installer package name result hides the
     * originating app package name from us, a consuming application that *really* needs to know how an app was
     * installed will need to fall back on inspecting the apps currently installed on the device and making an educated
     * guess.
     */
    private static final String PACKAGE_INSTALLER_PACKAGE_NAME = "com.google.android.packageinstaller";

    @NonNull
    /* default */ static InstallSource fromInstallerPackageName(@Nullable final String installerPackageName) {
        if (GOOGLE_PLAY_STORE_PACKAGE_NAME.equalsIgnoreCase(installerPackageName)) {
            return new GooglePlayStoreInstallSource();
        } else if (AMAZON_APP_STORE_PACKAGE_NAME.equalsIgnoreCase(installerPackageName)) {
            return new AmazonAppStoreInstallSource();
        } else if (AMAZON_UNDERGROUND_PACKAGE_NAME.equalsIgnoreCase(installerPackageName)) {
            return new AmazonUndergroundInstallSource();
        } else if (PACKAGE_INSTALLER_PACKAGE_NAME.equalsIgnoreCase(installerPackageName)) {
            return new PackageInstallerInstallSource();
        } else if (installerPackageName != null) {
            return new UnrecognizedInstallSource(installerPackageName); // NOPMD: required as part of algebraic type.
        } else {
            return new UnknownInstallSource();
        }
    }

    private InstallSource() {
        // This constructor intentionally left blank.
    }

    public static final class GooglePlayStoreInstallSource extends InstallSource {
        @Override
        public String toString() {
            return "Google Play Store";
        }
    }

    public static final class AmazonAppStoreInstallSource extends InstallSource {
        @Override
        public String toString() {
            return "Amazon Appstore";
        }
    }

    public static final class AmazonUndergroundInstallSource extends InstallSource {
        @Override
        public String toString() {
            return "Amazon Underground";
        }
    }

    public static final class PackageInstallerInstallSource extends InstallSource {
        @Override
        public String toString() {
            return "Package Installer";
        }
    }

    public static final class UnknownInstallSource extends InstallSource {
        @Override
        public String toString() {
            return "Unknown";
        }
    }

    public static final class UnrecognizedInstallSource extends InstallSource {

        @NonNull
        private final String installerPackageName;

        @SuppressWarnings("PMD.CallSuperInConstructor") // Super does nothing.
        private UnrecognizedInstallSource(@NonNull final String installerPackageName) {
            this.installerPackageName = installerPackageName;
        }

        @Override
        public String toString() {
            return installerPackageName;
        }

    }

}
