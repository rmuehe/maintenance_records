package com.ryanmuehe.maintenancerecords.service;

import com.ryanmuehe.maintenancerecords.model.ItemUsage;
import com.ryanmuehe.maintenancerecords.model.dto.ItemUsageDTO;

import java.util.List;

// see ItemUsageServiceImpl for descriptions
// this interface is a contract for ItemUsageServiceImpl
public interface ItemUsageService {
    void addItemUsage(ItemUsageDTO itemUsageDTO);

    ItemUsage findById(Long id);

    void updateItemUsage(Long id, ItemUsageDTO itemUsageDTO);

    List<ItemUsageDTO> findItemUsagesByItemId(Long id);

    ItemUsageDTO convertToDTO(ItemUsage usage);

    void deleteItemUsage(Long id);
}
