package com.ryanmuehe.maintenancerecords.service.implementation;

import com.ryanmuehe.maintenancerecords.model.Item;
import com.ryanmuehe.maintenancerecords.model.ItemUsage;
import com.ryanmuehe.maintenancerecords.model.dto.ItemUsageDTO;
import com.ryanmuehe.maintenancerecords.model.repository.ItemUsageRepository;
import com.ryanmuehe.maintenancerecords.service.ItemService;
import com.ryanmuehe.maintenancerecords.service.ItemUsageService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemUsageServiceImpl implements ItemUsageService {

    @Autowired
    private ItemUsageRepository itemUsageRepository;
    @Autowired
    private ItemService itemService;

    @Override
    @Transactional
    public void addItemUsage(ItemUsageDTO itemUsageDTO) {
        ItemUsage itemUsage = new ItemUsage();

        if (itemUsageDTO.getUsageStart() != null) {
            itemUsage.setUsageStart(itemUsageDTO.getUsageStart());
        }
        if (itemUsageDTO.getUsageEnd() != null) {
            itemUsage.setUsageEnd(itemUsageDTO.getUsageEnd());
        }

        itemUsage.setUsedBy(itemUsageDTO.getUsedBy());

        Item item = itemService.findById(itemUsageDTO.getItemId());
        itemUsage.setItem(item);

        itemUsageRepository.save(itemUsage);
    }

    @Override
    public ItemUsage findById(Long id) {
        return itemUsageRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Item Usage Not Found " + id));
    }

    @Override
    @Transactional
    public void updateItemUsage(Long id, ItemUsageDTO itemUsageDTO) {
        ItemUsage itemUsage = itemUsageRepository.findById(id)
                .orElseThrow(()->  new EntityNotFoundException("Item Usage Not Found " + id ));
        itemUsage.setUsedBy(itemUsageDTO.getUsedBy());
        itemUsage.setUsageStart(itemUsageDTO.getUsageStart());
        itemUsage.setUsageEnd(itemUsageDTO.getUsageEnd());
        itemUsageRepository.save(itemUsage);
    }

    @Override
    public List<ItemUsageDTO> findItemUsagesByItemId(Long id) {
        // get a List of all Item Usages by Item id
        List<ItemUsage> usages = itemUsageRepository.findByItemId(id);

        // initiate an empty list of Item Usage DTOs
        ArrayList<ItemUsageDTO> usageDTOs = new ArrayList<>();

        // convert each Item Usage into a Data Transfer Object
        for (ItemUsage usage: usages) {
            ItemUsageDTO usageDTO = new ItemUsageDTO();

            usageDTO.setId(usage.getId());
            usageDTO.setUsedBy(usage.getUsedBy());
            usageDTO.setUsageStart(usage.getUsageStart());
            usageDTO.setUsageEnd(usage.getUsageEnd());
            usageDTO.setItemId(usage.getItem().getId());

            usageDTOs.add(usageDTO);
        }

        // return a list of those converted DTOs
        return usageDTOs;
    }

    // Converts an Item Usage to an Item Usage DTO
    @Override
    public ItemUsageDTO convertToDTO(ItemUsage usage) {

        if (usage == null) {
            throw new IllegalArgumentException("Cannot convert a null ItemUsage to a DTO.");
        }

        ItemUsageDTO usageDTO = new ItemUsageDTO();
        usageDTO.setId(usage.getId());
        usageDTO.setUsedBy(usage.getUsedBy());
        usageDTO.setUsageStart(usage.getUsageStart());
        usageDTO.setUsageEnd(usage.getUsageEnd());
        usageDTO.setItemId(usage.getItem().getId());

        return usageDTO;
    }


    // Deletes an Item Usage from the database
    @Override
    public void deleteItemUsage(Long id) {
        itemUsageRepository.deleteById(id);
    };

}
