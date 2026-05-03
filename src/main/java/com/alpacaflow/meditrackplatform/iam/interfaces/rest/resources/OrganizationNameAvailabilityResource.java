package com.alpacaflow.meditrackplatform.iam.interfaces.rest.resources;

/**
 * Response for public organization name availability checks during sign-up.
 *
 * @param available true if the name is not yet taken (ignoring case)
 */
public record OrganizationNameAvailabilityResource(boolean available) {
}
