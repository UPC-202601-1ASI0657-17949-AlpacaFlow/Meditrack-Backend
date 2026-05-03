package com.alpacaflow.meditrackplatform.organization.domain.exceptions;

/**
 * Thrown when creating or updating a senior citizen would violate uniqueness rules:
 * same full name or same DNI in the organization, or a device ID already linked to another senior.
 * <p>The {@link #getCode()} value is stable for clients (i18n keys).</p>
 */
public class SeniorCitizenDuplicateRegistrationException extends IllegalArgumentException {

    public static final String CODE_DUPLICATE_DNI = "MEDITRACK_SENIOR_CITIZEN_DUPLICATE_DNI";
    public static final String CODE_DUPLICATE_FULL_NAME = "MEDITRACK_SENIOR_CITIZEN_DUPLICATE_FULL_NAME";
    public static final String CODE_DEVICE_ALREADY_ASSIGNED = "MEDITRACK_SENIOR_CITIZEN_DEVICE_ALREADY_ASSIGNED";

    private final String code;

    public SeniorCitizenDuplicateRegistrationException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
