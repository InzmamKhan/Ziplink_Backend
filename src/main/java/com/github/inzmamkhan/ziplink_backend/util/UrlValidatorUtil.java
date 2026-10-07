package com.github.inzmamkhan.ziplink_backend.util;

import org.apache.commons.validator.routines.UrlValidator;

public class UrlValidatorUtil {

    private static final String[] ALLOWED_SCHEMES = {"http", "https"};
    private static final UrlValidator URL_VALIDATOR = new UrlValidator(ALLOWED_SCHEMES, UrlValidator.ALLOW_LOCAL_URLS);

    private UrlValidatorUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Validates if the given string is a syntactically valid HTTP or HTTPS URL.
     *
     * @param url The target URL string
     * @return true if valid, false otherwise
     */
    public static boolean isValidUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }

        String sanitizedUrl = url.trim();
        return URL_VALIDATOR.isValid(sanitizedUrl);
    }

    /**
     * Sanitizes and ensures the input URL starts with an explicit HTTP/HTTPS scheme.
     *
     * @param url Raw input string
     * @return Cleaned URL string
     */
    public static String sanitizeUrl(String url) {
        if (url == null) {
            return "";
        }

        String trimmed = url.trim();
        if (!trimmed.startsWith("http://") && !trimmed.startsWith("https://")) {
            return "https://" + trimmed;
        }

        return trimmed;
    }
}