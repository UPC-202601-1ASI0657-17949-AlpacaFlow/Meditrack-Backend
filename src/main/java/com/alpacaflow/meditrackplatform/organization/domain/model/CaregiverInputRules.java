package com.alpacaflow.meditrackplatform.organization.domain.model;

/**
 * Shared validation rules for caregiver registration (create/update).
 */
public final class CaregiverInputRules {

    public static final int AGE_MIN = 21;
    public static final int AGE_MAX = 65;

    private CaregiverInputRules() {
    }

    public static void assertCaregiverAge(Integer age) {
        if (age == null || age < AGE_MIN || age > AGE_MAX) {
            throw new IllegalArgumentException(
                    "Caregiver age must be between %d and %d (inclusive)".formatted(AGE_MIN, AGE_MAX));
        }
    }

    /**
     * Phone must be non-blank and contain only ASCII digits (no letters, spaces, or symbols).
     */
    public static void assertCaregiverPhoneDigitsOnly(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number cannot be null or blank");
        }
        var trimmed = phoneNumber.trim();
        for (int i = 0; i < trimmed.length(); i++) {
            if (!Character.isDigit(trimmed.charAt(i))) {
                throw new IllegalArgumentException(
                        "Phone number must contain only digits (no letters or special characters)");
            }
        }
    }
}
