package com.lr.entos.identity.securityConfig.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class IdentityConfig {
    // This allows Spring to bind the YAML values to the Record
}
