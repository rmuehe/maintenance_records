package com.ryanmuehe.maintenancerecords.model.repository;

import com.ryanmuehe.maintenancerecords.model.ItemUsage;
import com.ryanmuehe.maintenancerecords.model.dto.ItemUsageDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Manages operations for ItemUsage entities.
 */
public interface ItemUsageRepository extends JpaRepository<ItemUsage, Long> {
    // Inherits CRUD methods like save(), findById(), findAll(), deleteById()

    // Custom query auto-generated using findBy convention
    // retrieves list of all Item Usages per item
    List<ItemUsage> findByItemId(Long id);

}
