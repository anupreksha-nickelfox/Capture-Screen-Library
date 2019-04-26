package com.android.capturescreenlibrary.utilities;

import java.util.Locale;

/**
 * Utility methods based on Apache Commons Lang WordUtils/StringUtils.
 */
public final class StringUtils {

    public static String capitalizeFully(String str) {
        if (isEmpty(str)) {
            return str;
        }

        str = str.toLowerCase(Locale.getDefault());
        return capitalize(str);
    }

    private static String capitalize(final String str) {
        if (isEmpty(str)) {
            return str;
        }

        final char[] buffer = str.toCharArray();

        boolean capitalizeNext = true;
        for (int characterIndex = 0; characterIndex < buffer.length; characterIndex++) {
            final char character = buffer[characterIndex];

            if (Character.isWhitespace(character)) {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                buffer[characterIndex] = Character.toTitleCase(character);
                capitalizeNext = false;
            }
        }

        return new String(buffer);
    }

    private static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    private StringUtils() {

    }

}
