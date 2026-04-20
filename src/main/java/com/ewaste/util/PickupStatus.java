package com.ewaste.util;

/**
 * Central constants for pickup status values.
 * Used across Service and Controller layers to avoid hardcoded strings.
 */
public final class PickupStatus {

    private PickupStatus() {}

    public static final String SCHEDULED         = "Scheduled";
    public static final String ASSIGNED          = "Assigned";
    public static final String COLLECTED         = "Collected";
    public static final String SORTING           = "Sorting";
    public static final String SHREDDING         = "Shredding";
    public static final String COMPLETED         = "Completed";
    public static final String CERTIFICATE_ISSUED = "Certificate Issued";

    /** Returns true if the pickup is in an active processing state */
    public static boolean isProcessing(String status) {
        return ASSIGNED.equals(status) || COLLECTED.equals(status) ||
               SORTING.equals(status) || SHREDDING.equals(status) ||
               COMPLETED.equals(status);
    }
}
