package com.qpro.groceryapi.service;

import com.qpro.groceryapi.model.GroceryItem;
import com.qpro.groceryapi.model.Inventory;
import com.qpro.groceryapi.repository.GroceryItemRepository;
import com.qpro.groceryapi.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private GroceryItemRepository groceryItemRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Transactional
    public GroceryItem addGroceryItem(GroceryItem item) {
        return groceryItemRepository.save(item);
    }

    public List<GroceryItem> getAllGroceryItems() {
        return groceryItemRepository.findAll();
    }

    @Transactional
    public void removeGroceryItem(Long id) {
        inventoryRepository.deleteById(id);
    }

    @Transactional
    public Inventory updateGroceryItemInventory(Long id, Inventory updatedItem) {
        updatedItem.setIdOfItem(id);
        return inventoryRepository.save(updatedItem);
    }

    public Inventory getInventory(String itemName) {
        Inventory inventory = inventoryRepository.findByItemName(itemName);
        return inventory;
    }

    public Inventory addInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    public List<Inventory> addMultipleInventory(List<Inventory> inventories) {
        return inventoryRepository.saveAll(inventories);
    }

    public List<Inventory> getAllGroceryInventory() {
        return inventoryRepository.findAll();
    }
}
