package com.lr.entos.identity.securityConfig;

import com.lr.entos.identity.repository.UserRepository;
import com.lr.entos.shared.exception.UserNotFoundException;
import com.lr.entos.shared.utils.message.ErrorsMessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email){
        return userRepository.findByEmail(email)
                .map(CustomUserDetails::build)
                .orElseThrow(() -> new UserNotFoundException(ErrorsMessageUtils.NOT_FOUND.formatted("User")));
    }
}
