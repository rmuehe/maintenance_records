package com.ryanmuehe.maintenancerecords.service.implementation;

import com.ryanmuehe.maintenancerecords.model.Item;
import com.ryanmuehe.maintenancerecords.model.ItemUsage;
import com.ryanmuehe.maintenancerecords.model.MaintenanceRecord;
import com.ryanmuehe.maintenancerecords.model.repository.ItemRepository;
import com.ryanmuehe.maintenancerecords.model.repository.MaintenanceRecordRepository;
import com.ryanmuehe.maintenancerecords.service.implementation.MaintenanceRecordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class MaintenanceRecordServiceImplTest {

    @Autowired
    private MaintenanceRecordRepository maintenanceRecordRepository;

    private MaintenanceRecordServiceImpl maintenanceRecordService;
    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        this.maintenanceRecordService = new MaintenanceRecordServiceImpl(maintenanceRecordRepository, null); // Pass null for ItemService since it's not used in delete operation
    }

    @Test
    @DisplayName("deleteMaintenanceRecord removes the specified record")
    public void testDeleteMaintenanceRecord() {
        // Given a MaintenanceRecord in the database
        MaintenanceRecord record = new MaintenanceRecord();
        // Set necessary fields for item and record
        Item item = new Item();
        item.setName("Ok");
        item.setDateCreated(LocalDateTime.now());
        item.setLastUpdated(LocalDateTime.now());
        item = itemRepository.save(item); // Save the valid item first


        // Make a valid Maintenance Record Object
        record.setDescription("Words");
        record.setDateCreated(LocalDateTime.now());
        record.setLastUpdated(LocalDateTime.now());
        record.setItem(item);


        record = maintenanceRecordRepository.save(record);

        // make sure record exists first
        assertTrue(maintenanceRecordRepository.existsById(record.getId()));

        //  delete maintenance record
        maintenanceRecordService.deleteMaintenanceRecord(record.getId());

        // record should not exist
        assertFalse(maintenanceRecordRepository.existsById(record.getId()));
    }
}

