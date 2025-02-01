package com.qpro.groceryapi.service;

import com.qpro.groceryapi.model.GroceryItem;
import com.qpro.groceryapi.model.Inventory;
import com.qpro.groceryapi.repository.GroceryItemRepository;
import com.qpro.groceryapi.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        Optional<Inventory> toUpdate = inventoryRepository.findById(id);

        if (toUpdate.isPresent()) {
            Inventory tmp = toUpdate.get();

            tmp.setTotalQnty(updatedItem.getTotalQnty());
            tmp.setAvailableQnty(updatedItem.getAvailableQnty());
            tmp.setPricePerItem(updatedItem.getPricePerItem());

            return inventoryRepository.save(tmp);
        }

        return null;
    }

    public Inventory getInventory(String itemName) {
        Inventory inventory = inventoryRepository.findByItemName(itemName);
        return inventory;
    }

    public Inventory getById(Long id) {
        Optional<Inventory> result = inventoryRepository.findById(id);

        return result.orElse(null);
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

    public void removeAll() {
        inventoryRepository.deleteAll();
    }
}
