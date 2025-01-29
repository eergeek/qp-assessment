package com.qpro.groceryapi.service;

import com.qpro.groceryapi.model.GroceryItem;
import com.qpro.groceryapi.repository.GroceryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private GroceryRepository groceryItemRepository;

    public GroceryItem addGroceryItem(GroceryItem item) {
        return groceryItemRepository.save(item);
    }

    public List<GroceryItem> getAllGroceryItems() {
        return groceryItemRepository.findAll();
    }

    public void removeGroceryItem(Long id) {
        groceryItemRepository.deleteById(id);
    }

    public GroceryItem updateGroceryItem(Long id, GroceryItem updatedItem) {
        updatedItem.setId(id);
        return groceryItemRepository.save(updatedItem);
    }

    public GroceryItem manageInventory(Long id, int inventory) {
        GroceryItem item = groceryItemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
        item.setInventory(inventory);
        return groceryItemRepository.save(item);
    }
}
