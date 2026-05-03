package com.alpacaflow.meditrackplatform.organization.domain.validation;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Rules for senior citizen personal data (create/update). Kept in domain so command layer can reuse.
 */
public final class SeniorCitizenPersonalDataValidation {

    public static final int MIN_AGE_YEARS = 60;
    public static final int MAX_AGE_YEARS = 120;
    public static final double MIN_WEIGHT_KG = 25.0;
    public static final double MAX_WEIGHT_KG = 250.0;
    public static final double MIN_HEIGHT_CM = 120.0;
    public static final double MAX_HEIGHT_CM = 220.0;
    public static final int MAX_DNI_DIGITS = 8;

    private static final Pattern DNI_PATTERN = Pattern.compile("^[0-9]{1,8}$");

    private SeniorCitizenPersonalDataValidation() {
    }

    /**
     * Age in full years, aligned with {@link com.alpacaflow.meditrackplatform.organization.domain.model.aggregates.SeniorCitizen} aggregate logic.
     */
    public static int calculateAgeYears(Date birthDate) {
        Calendar birth = Calendar.getInstance();
        birth.setTime(birthDate);
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
        int monthDiff = today.get(Calendar.MONTH) - birth.get(Calendar.MONTH);
        if (monthDiff < 0 || (monthDiff == 0 && today.get(Calendar.DAY_OF_MONTH) < birth.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }
        return age;
    }

    /**
     * Canonical stored values: {@code Masculino} or {@code Femenino}.
     */
    public static String normalizeGender(String gender) {
        if (gender == null) {
            throw new IllegalArgumentException("Gender is required");
        }
        String g = gender.trim();
        if (g.equalsIgnoreCase("Masculino")) {
            return "Masculino";
        }
        if (g.equalsIgnoreCase("Femenino")) {
            return "Femenino";
        }
        throw new IllegalArgumentException("Gender must be Masculino or Femenino");
    }

    public static void validatePersonalData(Date birthDate, String gender, Double weight, Double height, String dni) {
        if (birthDate == null) {
            throw new IllegalArgumentException("Birth date is required");
        }
        int age = calculateAgeYears(birthDate);
        if (age < MIN_AGE_YEARS || age > MAX_AGE_YEARS) {
            throw new IllegalArgumentException(
                    "Age derived from birth date must be between %d and %d years (inclusive); got %d"
                            .formatted(MIN_AGE_YEARS, MAX_AGE_YEARS, age));
        }
        normalizeGender(gender);
        if (weight == null || weight < MIN_WEIGHT_KG || weight > MAX_WEIGHT_KG) {
            throw new IllegalArgumentException(
                    "Weight must be between %.0f and %.0f kg (inclusive)".formatted(MIN_WEIGHT_KG, MAX_WEIGHT_KG));
        }
        if (height == null || height < MIN_HEIGHT_CM || height > MAX_HEIGHT_CM) {
            throw new IllegalArgumentException(
                    "Height must be between %.0f and %.0f cm (inclusive)".formatted(MIN_HEIGHT_CM, MAX_HEIGHT_CM));
        }
        if (dni == null || dni.isBlank()) {
            throw new IllegalArgumentException("DNI is required");
        }
        String d = dni.trim();
        if (!DNI_PATTERN.matcher(d).matches()) {
            throw new IllegalArgumentException(
                    "DNI must contain only digits and at most %d characters".formatted(MAX_DNI_DIGITS));
        }
    }
}
