package com.alpacaflow.meditrackplatform.organization.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;

/**
 * Value object representing an email address.
 */
@Embeddable
public record Email(String email) {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    public Email {
        if (email != null && !email.isBlank()) {
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                throw new IllegalArgumentException("Email must be a valid email address");
            }
        }
    }

    public Email() {
        this(null);
    }
}

