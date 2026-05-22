package com.lr.entos.identity.securityConfig;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SecurityService {
    public boolean isOwner(UUID targetGuid) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails user) {
            // Memory-only check: No DB calls!
            return user.guid().equals(targetGuid);
        }
        return false;
    }

    public boolean isOwnerOrAdmin(UUID targetGuid) {
        return isOwner(targetGuid) || hasRole("ADMIN");
    }

    private boolean hasRole(String role) {
        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(role)); // No "ROLE_" prefix if you don't use it
    }
}
