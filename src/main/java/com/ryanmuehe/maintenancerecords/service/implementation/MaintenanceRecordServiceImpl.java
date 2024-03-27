package com.ryanmuehe.maintenancerecords.service.implementation;

import com.ryanmuehe.maintenancerecords.model.Item;
import com.ryanmuehe.maintenancerecords.model.MaintenanceRecord;
import com.ryanmuehe.maintenancerecords.model.dto.MaintenanceRecordDTO;
import com.ryanmuehe.maintenancerecords.model.dto.MaintenanceRecordViewDTO;
import com.ryanmuehe.maintenancerecords.model.repository.MaintenanceRecordRepository;
import com.ryanmuehe.maintenancerecords.service.ItemService;
import com.ryanmuehe.maintenancerecords.service.MaintenanceRecordService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service// Spring service component, eligible for dependency injection and transaction management
// handles business logic of MaintenanceRecords
public class MaintenanceRecordServiceImpl implements MaintenanceRecordService {

    // immutable repository that governs MaintenanceRecord entity data access operations
    private final MaintenanceRecordRepository maintenanceRecordRepository;
    private final ItemService itemService;

    // Constructor dependency injection for MaintenanceRecordRepository
    @Autowired
    public MaintenanceRecordServiceImpl (
            MaintenanceRecordRepository maintenanceRecordRepository,
            ItemService itemService) {
        this.maintenanceRecordRepository = maintenanceRecordRepository;
        this.itemService = itemService;


    }

    // persists a MaintenanceRecord to the database
    @Override
    @Transactional
//    public void addMaintenanceRecord(MaintenanceRecordDTO maintenanceRecordDTO, Item item) {
    public void addMaintenanceRecord(MaintenanceRecordDTO maintenanceRecordDTO) {

        MaintenanceRecord record;
        // if the DTO is for updating and has a Maintenance Record id, then ...
        if (maintenanceRecordDTO.getId() != null) {
            record = maintenanceRecordRepository.findById(maintenanceRecordDTO.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Maintenance Record not found"));
            record.setLastUpdated(LocalDateTime.now());
        } else { // if the DTO is new
            record = new MaintenanceRecord();
            Item item = itemService.findById(maintenanceRecordDTO.getItemId());
            record.setItem(item);
        }
        record.setDescription(maintenanceRecordDTO.getDescription());

        maintenanceRecordRepository.save(record);
    }


    /**
     * Converts a list of MaintenanceRecord entities to a list of MaintenanceRecordViewDTO objects.
     * Iterates through each MaintenanceRecord in the input list, converts it to MaintenanceRecordViewDTO,
     * and adds it to the output list
     *
     * @param itemId the item identifier to retrieve List<MaintenanceRecord> of the item to convert
     * @return a list of MaintenanceRecordViewDTO objects
     */
    public List<MaintenanceRecordViewDTO> findMaintenanceRecordsForViewByItemId(Long itemId) {
        // Get all the records associated with the Item
        List<MaintenanceRecord> records = maintenanceRecordRepository.findByItemId(itemId);
        // Construct empty list of records AS Data Transfer Objects
        List<MaintenanceRecordViewDTO> viewDTOs = new ArrayList<>();
        // for each record in the list
        for (MaintenanceRecord record : records) {
            // convert the record to a Data Transfer Object
            MaintenanceRecordViewDTO dto = convertToViewDTO(record);
            // and add it to the new list of DTOs
            viewDTOs.add(dto);
        }
        // return the list of all the Item's Maintenance Records as DTOs
        return viewDTOs;
    }

    // retrieves Maintenance Record or throws exception provided an Id
    @Override
    public MaintenanceRecord findById(Long id) {
        // default repo method provided by JPA Repository for Maintenance Records
        return maintenanceRecordRepository.findById(id)
                // since it's an Optional object, must account for case where there's no record
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + id));
    }

    /**
     * Converts a MaintenanceRecord entity to a simplified MaintenanceRecordViewDTO object,
     * a Data Transfer Object used to display MaintenanceRecords on pages
     *
     * @param record the MaintenanceRecord entity to convert
     * @return a populated MaintenanceRecordViewDTO object
     */
    private MaintenanceRecordViewDTO convertToViewDTO(MaintenanceRecord record) {
        // stage a new Data Transfer Object
        MaintenanceRecordViewDTO dto = new MaintenanceRecordViewDTO();

        // Manually set the id, description, lastUpdated properties of the DTO
        // taking properties from the supplied Maintenance Record
        dto.setId(record.getId());
        dto.setDescription(record.getDescription());
        dto.setLastUpdated(record.getLastUpdated());

        return dto;
    }
//    @Override
//    public List<MaintenanceRecordViewDTO> findMaintenanceRecordsForViewByItemId(Long itemId) {
//        List<MaintenanceRecord> records = maintenanceRecordRepository.findByItemId(itemId);
//        return records.stream().map(this::convertToViewDTO).collect(Collectors.toList());
//    }

    @Override
    public MaintenanceRecordDTO convertToMRecordDTO(MaintenanceRecord record) {
        // stage a new Data Transfer Object
        MaintenanceRecordDTO dto = new MaintenanceRecordDTO();

        // Manually set the item_id & description properties of the DTO,
        // taking properties from the supplied Maintenance Record
        dto.setItemId(record.getItem().getId());
        dto.setDescription(record.getDescription());

        // when updating, the DTO needs an existing Maintenance Record id
        if(record.getId() != null) {
            dto.setId(record.getId());
        }

        return dto;
    }

    @Override
    public void deleteMaintenanceRecord(Long id) {
        maintenanceRecordRepository.deleteById(id);
    }


}
