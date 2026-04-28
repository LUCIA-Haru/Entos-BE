package com.lr.entos.infra.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef =  "auditAwareImpl")
public class JpaAuditConfig {
    // This activates the auditing logic defined in shared/BaseEntity
}
