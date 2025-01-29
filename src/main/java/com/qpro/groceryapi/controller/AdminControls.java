package com.qpro.groceryapi.controller;

import com.qpro.groceryapi.model.GroceryItem;
import com.qpro.groceryapi.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminControls {
    @Autowired
    AdminService adminService;

    // view all
    @GetMapping("/items")
    public List<GroceryItem> viewAllItems() {
        return adminService.getAllGroceryItems();
    }

    // add
    @PostMapping("items")
    public GroceryItem addItem(@RequestBody GroceryItem groceryItem) {
        return adminService.addGroceryItem(groceryItem);
    }

    // remove
    @DeleteMapping("/items/{id}")
    public void deleteItem(@PathVariable Long id) {
        adminService.removeGroceryItem(id);
    }

    // update
    @PutMapping("items/{id}")
    public GroceryItem updateItem(@PathVariable Long id,
                                  @RequestBody GroceryItem groceryItem) {
        return adminService.updateGroceryItem(id, groceryItem);
    }

    // manage inventory
    @PatchMapping("items/{id}/inventory")
    public void manageInventory(@PathVariable Long id, @RequestParam int inventory) {
        adminService.manageInventory(id, inventory);
    }
}
