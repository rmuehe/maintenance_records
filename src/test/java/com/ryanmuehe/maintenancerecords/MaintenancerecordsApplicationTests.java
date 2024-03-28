package com.ryanmuehe.maintenancerecords;

import com.ryanmuehe.maintenancerecords.configuration.TestJpaAuditingConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootTest
@ComponentScan(excludeFilters =
	@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = TestJpaAuditingConfig.class))
class MaintenancerecordsApplicationTests {

	@Test
	void contextLoads() {
	}

}
