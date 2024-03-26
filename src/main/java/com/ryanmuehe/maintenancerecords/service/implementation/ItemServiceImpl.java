package com.ryanmuehe.maintenancerecords.service.implementation;

import com.ryanmuehe.maintenancerecords.model.Item;
import com.ryanmuehe.maintenancerecords.model.dto.AddItemDTO;
import com.ryanmuehe.maintenancerecords.model.repository.ItemRepository;
import com.ryanmuehe.maintenancerecords.service.ItemService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Spring service component, eligible for dependency injection and transaction management
// handles business logic of Items
public class ItemServiceImpl implements ItemService {

    // immutable repository that governs Item entity data access operations
    private final ItemRepository itemRepository;

    // Constructor dependency injection for ItemRepository
    @Autowired
    public ItemServiceImpl (ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
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

    // deletes Item completely from the database
    @Override
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
}
