package com.ryanmuehe.maintenancerecords.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//@Configuration // creates circular dependency issue with MaintenancerecordsApplicationTests.java
@EnableJpaAuditing
public class TestJpaAuditingConfig {
   // This allows test to use JPA Auditing,
    // needed for automatically generating dateTimes when creating/updating entities
    // Can now @Import({TestJpaAuditingConfig.class}) on Test to get these dateTimes to work
}
