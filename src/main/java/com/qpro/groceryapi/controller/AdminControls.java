package com.qpro.groceryapi.controller;

import com.qpro.groceryapi.model.Inventory;
import com.qpro.groceryapi.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminControls {
    @Autowired
    AdminService adminService;

    // view all inventory
    @GetMapping("/inventories")
    public ResponseEntity<List<Inventory>> viewAllItems() {
        return ResponseEntity.ok(adminService.getAllGroceryInventory());
    }

    @GetMapping("/inventorybyname/{name}")
    public ResponseEntity<Inventory> getSingleInventoryByName(@PathVariable String name) {
        Optional<Inventory> inventory = Optional.ofNullable(adminService.getInventory(name));

        if (inventory.isPresent())
            return ResponseEntity.ok(adminService.getInventory(name));

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/inventorybyid/{id}")
    public ResponseEntity<Inventory> getSingleInventoryById(@PathVariable Long id) {
        Optional<Inventory> inventory = Optional.ofNullable(adminService.getById(id));

        if (inventory.isPresent())
            return ResponseEntity.ok(inventory.get());

        return ResponseEntity.notFound().build();
    }

    // add item to inventory
    @PostMapping("/add_inventory")
    public ResponseEntity<Inventory> addInventory(@RequestBody Inventory inventory) {
        return ResponseEntity
                .created(URI.create("/inventory"))
                .body(adminService.addInventory(inventory));
    }

    // add multiple items to inventory
    @PostMapping("/add_inventories")
    public ResponseEntity<List<Inventory>> addInventories(@RequestBody List<Inventory> inventories) {
        return ResponseEntity
                .created(URI.create("/inventory"))
                .body(adminService.addMultipleInventory(inventories));
    }

    // remove item from inventory
    @DeleteMapping("/del_inventory/{id}")
    public void deleteItem(@PathVariable Long id) {
        adminService.removeGroceryItem(id);
    }

    // remove all inventories
    @DeleteMapping("/cleanup")
    public void deleteAll() {
        adminService.removeAll();
    }

    // update
    @PutMapping("/put_inventory/{id}")
    public ResponseEntity<Inventory> updateItem(@PathVariable Long id,
                                  @RequestBody Inventory groceryItem) {
        Inventory updated = adminService.updateGroceryItemInventory(id, groceryItem);
        if (updated != null) {
            return ResponseEntity.accepted().body(updated);
        }

        return ResponseEntity.badRequest().build();
    }
}
