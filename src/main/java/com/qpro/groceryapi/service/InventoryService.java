package com.qpro.groceryapi.service;

import com.qpro.groceryapi.model.Inventory;
import com.qpro.groceryapi.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {
    @Autowired
    InventoryRepository inventoryRepository;

    @Transactional
    public void addInventory(Inventory toAdd) {
        inventoryRepository.save(toAdd);
    }
}
