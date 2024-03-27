package com.ryanmuehe.maintenancerecords.model.repository;

import com.ryanmuehe.maintenancerecords.model.MaintenanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Manages operations for Item entities.
 */
public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, Long> {
    // Inherits CRUD methods like save(), findById(), findAll(), deleteById()

    // Custom query auto-generated using findBy convention
    // retrieves list of all maintenance records per item
    List<MaintenanceRecord> findByItemId(Long itemId);

}
