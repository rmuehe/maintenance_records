package com.ryanmuehe.maintenancerecords.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // Allows JPA to audit:
// can now automatically update timestamps with
// @EntityListeners(AuditingEntityListener.class)
public class TimeConfig {
}
