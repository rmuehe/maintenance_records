package com.ryanmuehe.maintenancerecords.service.implementation;

import com.ryanmuehe.maintenancerecords.model.Item;
import com.ryanmuehe.maintenancerecords.model.ItemUsage;
import com.ryanmuehe.maintenancerecords.model.MaintenanceRecord;
import com.ryanmuehe.maintenancerecords.model.dto.AddItemDTO;
import com.ryanmuehe.maintenancerecords.model.repository.ItemRepository;
import com.ryanmuehe.maintenancerecords.model.repository.ItemUsageRepository;
import com.ryanmuehe.maintenancerecords.model.repository.MaintenanceRecordRepository;
import com.ryanmuehe.maintenancerecords.service.ItemService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // Spring service component, eligible for dependency injection and transaction management
// handles business logic of Items
public class ItemServiceImpl implements ItemService {

    // immutable repository that governs Item entity data access operations
    private final ItemRepository itemRepository;
    private final ItemUsageRepository itemUsageRepository;
    private final MaintenanceRecordRepository maintenanceRecordRepository;
    private static final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);



    // Constructor dependency injection for ItemRepository
    @Autowired
    public ItemServiceImpl (ItemRepository itemRepository, ItemUsageRepository itemUsageRepository, MaintenanceRecordRepository maintenanceRecordRepository) {
        this.itemRepository = itemRepository;
        this.itemUsageRepository = itemUsageRepository;
        this.maintenanceRecordRepository = maintenanceRecordRepository;
    }

    // persists new Item to database
    @Override
    public void addItem(Item item) {
        itemRepository.save(item);
    };

    // custom query returns all Items associated with a User
    @Override
    public List<Item> findItemsByUserId(Long id) {
        return itemRepository.findByOwnerId(id);
    };

    // fetches Item by if it exists
    @Override
    public Item findById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + id));
    };

//    // deletes Item completely from the database
//    @Override
//    public void deleteItem(Long id) {
//        itemRepository.deleteById(id);
//    }

    // TODO: add disabling logic and reserve deletion for Admin only
    @Override
    @Transactional
    public void deleteItem(Long itemId) {
        // First, delete associated ItemUsages
        List<ItemUsage> itemUsages = itemUsageRepository.findByItemId(itemId);
        Item item = itemRepository.findById(itemId).orElseThrow(RuntimeException::new);
        logger.info("Deleted ItemUsages for Item ID: {}, Item: {}", itemId, item.getName());
        logger.info("Owned by: {}", item.getOwner().getUsername());

        for (ItemUsage itemUsage : itemUsages) {
            logger.info("Item Usage Deleted: id = {}, usedBy = {}", itemUsage.getId(), itemUsage.getUsedBy());
        }
        itemUsageRepository.deleteAll(itemUsages);

        // then delete maintenance records
        List<MaintenanceRecord> maintenanceRecords = maintenanceRecordRepository.findByItemId(itemId);
        logger.info("Deleted MaintenanceRecords for Item ID: {}, Item: {}", itemId, item.getName());
        logger.info("Owned by: {}", item.getOwner().getUsername());
        for (MaintenanceRecord maintenanceRecord : maintenanceRecords) {
            System.out.println("Maintenance Record Deleted: id = " + maintenanceRecord.getId() +
                    ", usedBy = " + maintenanceRecord.getDescription());
        }
        maintenanceRecordRepository.deleteAll(maintenanceRecords);

        // Then, delete the item itself
        itemRepository.deleteById(itemId);
        logger.info("Deleted Item with ID: {}, Item: {}", itemId, item.getName());
    }

}
