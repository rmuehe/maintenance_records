package com.ryanmuehe.maintenancerecords.service.implementation;

import com.ryanmuehe.maintenancerecords.model.Item;
import com.ryanmuehe.maintenancerecords.model.ItemUsage;
import com.ryanmuehe.maintenancerecords.model.repository.ItemRepository;
import com.ryanmuehe.maintenancerecords.model.repository.ItemUsageRepository;
import com.ryanmuehe.maintenancerecords.service.ItemUsageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({ItemUsageServiceImpl.class, ItemServiceImpl.class})
public class ItemUsageServiceImplTest {

    @Autowired
    private ItemUsageService itemUsageService;

    @Autowired
    private ItemUsageRepository itemUsageRepository;

    private ItemUsage savedItemUsage;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
    //Test data
        Item item = new Item();
        item.setName("Alright");
        item.setDateCreated(LocalDateTime.now());
        item.setLastUpdated(LocalDateTime.now());
        item = itemRepository.save(item); // Save the valid item first


        // Make a valid ItemUsage Object
        ItemUsage itemUsage = new ItemUsage();
        itemUsage.setUsedBy("Some Dude");
        itemUsage.setDateCreated(LocalDateTime.now());
        itemUsage.setLastUpdated(LocalDateTime.now());
        itemUsage.setItem(item);

        this.savedItemUsage = itemUsageRepository.save(itemUsage);
    }

    @Test
    @DisplayName("Delete ItemUsage by ID")
    // Deletes the test data Item Usage
    void deleteItemUsage() {
        assertNotNull(itemUsageRepository.findById(savedItemUsage.getId()).orElse(null));

        // Perform deletion
        itemUsageService.deleteItemUsage(savedItemUsage.getId());

        // Verify deletion
        assertFalse(itemUsageRepository.findById(savedItemUsage.getId()).isPresent());
    }
}
