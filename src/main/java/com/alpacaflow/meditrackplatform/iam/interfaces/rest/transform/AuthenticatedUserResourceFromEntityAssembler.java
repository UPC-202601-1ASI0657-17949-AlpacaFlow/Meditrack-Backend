package com.alpacaflow.meditrackplatform.iam.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.iam.domain.model.aggregates.User;
import com.alpacaflow.meditrackplatform.iam.interfaces.rest.resources.AuthenticatedUserResource;

public class AuthenticatedUserResourceFromEntityAssembler {
    public static AuthenticatedUserResource toResourceFromEntity(User user, String token) {
        return new AuthenticatedUserResource(user.getId(), user.getEmail(), user.getRole(), token);
    }
}

