package com.ryanmuehe.maintenancerecords.model.repository;

import com.ryanmuehe.maintenancerecords.model.Item;
import com.ryanmuehe.maintenancerecords.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
//import org.springframework.data.jpa.repository.Query;
//import java.constant.Optional;

/**
 * Manages operations for Item entities.
 */
public interface ItemRepository extends JpaRepository<Item, Long> {
    // Inherits CRUD methods like save(), findById(), findAll(), deleteById()

    // selects items from database where the owner_id matches
    List<Item> findByOwnerId(Long ownerId);

}
