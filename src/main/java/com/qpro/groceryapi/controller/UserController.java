package com.qpro.groceryapi.controller;

import com.qpro.groceryapi.model.GroceryItem;
import com.qpro.groceryapi.model.GroceryOrder;
import com.qpro.groceryapi.service.UserService;
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

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    // view all
    @GetMapping("/items")
    public ResponseEntity<List<GroceryItem>> groceryItemsAvailable() {
        List<GroceryItem> response = userService.getAvailableGroceryItems();
        if (response.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(response);
    }

    // book order
    @PostMapping("/order/{username}")
    public ResponseEntity<Long> bookOrder(@RequestBody List<GroceryItem> items, @PathVariable String username) {
        Long id = userService.bookGroceriesOrder(items, username);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/rmorder/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable long id) {
        try {
            userService.deleteOrderById(id);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/orderupdate/{id}")
    public ResponseEntity<GroceryOrder> updateOrder(@PathVariable long id,
                                            @RequestBody List<GroceryItem> updatedList) {
        GroceryOrder order = userService.updateOrder(id, updatedList);
        if (order != null) {
            return ResponseEntity.accepted().body(order);
        }

        return ResponseEntity.notFound().build();
    }
}
