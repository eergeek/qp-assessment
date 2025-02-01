package com.qpro.groceryapi.controller;

import com.qpro.groceryapi.model.Inventory;
import com.qpro.groceryapi.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminControls {
    @Autowired
    AdminService adminService;

    // view all inventory
    @GetMapping("/inventory")
    public ResponseEntity<List<Inventory>> viewAllItems() {
        return ResponseEntity.ok(adminService.getAllGroceryInventory());
    }

    // add
//    @PostMapping("items")
//    public ResponseEntity<GroceryItem> addItem(@RequestBody GroceryItem groceryItem) {
//
//        GroceryItem toAdd = new GroceryItem();
//        toAdd.setName(groceryItem.getName());
//        toAdd.setQuantity(groceryItem.getQuantity());
//        toAdd.setPrice(groceryItem.getPrice());
//
//        Inventory inventory = new Inventory();
//        inventory.setIdOfItem(toAdd.getGroceryId());
//        toAdd.setInventory(inventory);
//
//        GroceryItem addedItem = adminService.addGroceryItem(toAdd);
//        return ResponseEntity.created(URI.create("/items")).body(addedItem);
//    }

    // add item to inventory
    @PostMapping("/inventory")
    public ResponseEntity<Inventory> addInventory(@RequestBody Inventory inventory) {
        return ResponseEntity
                .created(URI.create("/inventory"))
                .body(adminService.addInventory(inventory));
    }

    // add multiple items to inventory
    @PostMapping("/inventories")
    public ResponseEntity<List<Inventory>> addInventories(@RequestBody List<Inventory> inventories) {
        return ResponseEntity
                .created(URI.create("/inventory"))
                .body(adminService.addMultipleInventory(inventories));
    }

    // remove item from inventory
    @DeleteMapping("/inventory/{id}")
    public void deleteItem(@PathVariable Long id) {
        adminService.removeGroceryItem(id);
    }

    // update
    @PutMapping("inventory/{id}")
    public Inventory updateItem(@PathVariable Long id,
                                  @RequestBody Inventory groceryItem) {
        return adminService.updateGroceryItemInventory(id, groceryItem);
    }

    // manage inventory
    @PatchMapping("inventory/{name}")
    public void getInventory(@PathVariable String name) {
        adminService.getInventory(name);
    }
}
