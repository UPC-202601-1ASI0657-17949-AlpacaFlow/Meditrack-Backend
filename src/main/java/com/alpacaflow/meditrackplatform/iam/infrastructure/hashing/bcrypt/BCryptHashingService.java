package com.alpacaflow.meditrackplatform.iam.infrastructure.hashing.bcrypt;

import com.alpacaflow.meditrackplatform.iam.application.internal.outboundservices.hashing.HashingService;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface BCryptHashingService extends HashingService, PasswordEncoder {
}

