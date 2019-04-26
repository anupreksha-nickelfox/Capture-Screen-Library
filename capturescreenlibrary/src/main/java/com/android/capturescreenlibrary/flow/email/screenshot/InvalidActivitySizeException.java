package com.android.capturescreenlibrary.flow.email.screenshot;

/**
 * Exception thrown to indicate that we attempted to capture a screenshot of an Activity
 * whose width and/or height is 0.
 */
final class InvalidActivitySizeException extends Exception {

    InvalidActivitySizeException(final Throwable throwable) {
        super(throwable);
    }

}
