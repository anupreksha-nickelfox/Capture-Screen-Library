package com.android.capturescreenlibrary.flow.email.screenshot.maps;

/**
 * Exception thrown to indicate that our attempt to capture a snapshot of an GoogleMap failed.
 */
public final class MapSnapshotFailedException extends Exception {

    private static final String DETAIL_MESSAGE = "GoogleMap snapshot capture failed.";

    MapSnapshotFailedException() {
        super(DETAIL_MESSAGE);
    }

}
