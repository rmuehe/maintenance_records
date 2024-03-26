package com.ryanmuehe.maintenancerecords;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test verifies the default settings of the DataSource configuration.
 */
@DataJpaTest
@DisplayName("DataSource Configuration and Database Connection Test")
public class DataSourceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("Verify Database Connection and System Tables Accessibility")
    public void testDefaultSettings() {
        // Assert JdbcTemplate exists, confirming that Spring has injected it
        assertThat(jdbcTemplate).isNotNull();

        // Query the count of tables in database information schema
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES", Long.class);

        // Assert that count is not null
        // indicates query is successful and database connection works
        assertThat(count).isNotNull();
    }
}