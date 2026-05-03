package com.alpacaflow.meditrackplatform.organization.domain.exceptions;

/**
 * Thrown when an admin tries to register a caregiver that duplicates another caregiver
 * in the same organization (same institutional email or same full name).
 * <p>The {@link #getCode()} value is stable for clients (i18n keys).</p>
 */
public class CaregiverDuplicateRegistrationException extends IllegalArgumentException {

    public static final String CODE_DUPLICATE_EMAIL = "MEDITRACK_CAREGIVER_DUPLICATE_EMAIL";
    public static final String CODE_DUPLICATE_FULL_NAME = "MEDITRACK_CAREGIVER_DUPLICATE_FULL_NAME";

    private final String code;

    public CaregiverDuplicateRegistrationException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
