package com.alpacaflow.meditrackplatform.organization.domain.exceptions;

/**
 * Thrown when an organization name is already registered (case-insensitive).
 * <p>The {@link #getCode()} value is stable for clients (i18n / UI).</p>
 */
public class OrganizationDuplicateNameException extends IllegalArgumentException {

    public static final String CODE_DUPLICATE_NAME = "MEDITRACK_ORGANIZATION_DUPLICATE_NAME";

    private final String code;

    public OrganizationDuplicateNameException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
