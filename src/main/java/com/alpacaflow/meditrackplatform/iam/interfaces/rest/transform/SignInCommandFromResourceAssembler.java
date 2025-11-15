package com.alpacaflow.meditrackplatform.iam.interfaces.rest.transform;

import com.alpacaflow.meditrackplatform.iam.domain.model.commands.SignInCommand;
import com.alpacaflow.meditrackplatform.iam.interfaces.rest.resources.SignInResource;

public class SignInCommandFromResourceAssembler {
    public static SignInCommand toCommandFromResource(SignInResource signInResource) {
        return new SignInCommand(signInResource.email(), signInResource.password());
    }
}

