package com.lr.entos.identity.securityConfig.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "entos.jwt")
public record JwtProperties(
        String secret,
        int expirationMs
) {
}
