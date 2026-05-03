package com.alpacaflow.meditrackplatform.iam.infrastructure.authorization.sfs.services;

import com.alpacaflow.meditrackplatform.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.alpacaflow.meditrackplatform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service(value = "defaultUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (email == null || email.isBlank()) {
            throw new UsernameNotFoundException("User not found");
        }
        var normalized = email.trim().toLowerCase(Locale.ROOT);
        var user = userRepository.findByNormalizedEmail(normalized)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + normalized));
        return UserDetailsImpl.build(user);
    }
}

