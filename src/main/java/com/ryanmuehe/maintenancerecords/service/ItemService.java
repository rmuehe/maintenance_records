package com.ryanmuehe.maintenancerecords.service;

import com.ryanmuehe.maintenancerecords.model.Item;

import java.util.List;

// see ItemServiceImpl for descriptions
// this interface is a contract for ItemServiceImpl
public interface ItemService {

    void addItem(Item item);

    List<Item> findItemsByUserId(Long id);

    Item findById(Long id);

    void deleteItem(Long id);
}
