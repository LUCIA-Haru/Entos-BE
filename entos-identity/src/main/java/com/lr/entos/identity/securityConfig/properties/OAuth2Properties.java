package com.lr.entos.identity.securityConfig.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "entos.oauth2")
public record OAuth2Properties(
        String authorizedRedirectUri
) {
}
