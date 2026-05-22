package com.lr.entos.infra.utils.properties;

import io.jsonwebtoken.Jwt;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "entos")
public record EntosProperties(
        Jwt jwt,
        Oauth2 oauth2
) {
    public record Jwt(String secret, long expirationMs) {}
    public record Oauth2(String authorizedRedirectUri) {}
}
