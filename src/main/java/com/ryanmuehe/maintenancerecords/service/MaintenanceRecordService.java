package com.ryanmuehe.maintenancerecords.service;

import com.ryanmuehe.maintenancerecords.model.MaintenanceRecord;
import com.ryanmuehe.maintenancerecords.model.dto.MaintenanceRecordDTO;
import com.ryanmuehe.maintenancerecords.model.dto.MaintenanceRecordViewDTO;

import java.util.List;

// see MaintenanceRecordServiceImpl for descriptions
// this interface is a contract for MaintenanceRecordServiceImpl
public interface MaintenanceRecordService {
    // persists a MaintenanceRecord to the database
    void addMaintenanceRecord(MaintenanceRecordDTO maintenanceRecordDTO);

//    List<MaintenanceRecordDTO> findMaintenanceRecordsByItemId(Long itemId);

    List<MaintenanceRecordViewDTO> findMaintenanceRecordsForViewByItemId(Long itemId);
    MaintenanceRecord findById(Long id);

    MaintenanceRecordDTO convertToMRecordDTO(MaintenanceRecord maintenanceRecord);

    void deleteMaintenanceRecord(Long id);
}
