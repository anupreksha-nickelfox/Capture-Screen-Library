package com.android.capturescreenlibrary.flow.email;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

public final class GenericEmailIntentProvider {

    @NonNull
    /* default */ Intent getEmailIntent(
            @NonNull final String[] emailAddresses,
            @NonNull final String emailSubjectLine,
            @NonNull final String emailBody) {

        final Intent result = new Intent(Intent.ACTION_SENDTO);
        result.setData(Uri.parse("mailto:"));
        result.putExtra(Intent.EXTRA_EMAIL, emailAddresses);
        result.putExtra(Intent.EXTRA_SUBJECT, emailSubjectLine);
        result.putExtra(Intent.EXTRA_TEXT, emailBody);
        return result;
    }

    @NonNull
    /* default */ Intent getEmailWithAttachmentIntent(
        @NonNull final String[] emailAddresses,
        @NonNull final String emailSubjectLine,
        @NonNull final String emailBody,
        @NonNull final Uri attachmentUri) {

        final Intent result = getEmailIntent(emailAddresses, emailSubjectLine, emailBody);

        result.putExtra(Intent.EXTRA_STREAM, attachmentUri);

        return result;
    }

}
