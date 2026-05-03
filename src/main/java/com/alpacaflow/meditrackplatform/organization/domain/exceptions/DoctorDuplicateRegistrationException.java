package com.alpacaflow.meditrackplatform.organization.domain.exceptions;

/**
 * Thrown when an admin tries to register a doctor that duplicates another doctor
 * in the same organization (same institutional email or same full name).
 * <p>The {@link #getCode()} value is stable for clients (i18n keys).</p>
 */
public class DoctorDuplicateRegistrationException extends IllegalArgumentException {

    public static final String CODE_DUPLICATE_EMAIL = "MEDITRACK_DOCTOR_DUPLICATE_EMAIL";
    public static final String CODE_DUPLICATE_FULL_NAME = "MEDITRACK_DOCTOR_DUPLICATE_FULL_NAME";

    private final String code;

    public DoctorDuplicateRegistrationException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
