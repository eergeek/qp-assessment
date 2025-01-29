package com.qpro.groceryapi.controller;

import com.qpro.groceryapi.model.GroceryItem;
import com.qpro.groceryapi.model.Order;
import com.qpro.groceryapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
    public List<GroceryItem> groceryItemsAvailable() {
        return userService.getAvailableGroceryItems();
    }

    // book order
    @GetMapping("/order")
    public Order bookOrder(@RequestBody List<GroceryItem> items) {
        return userService.bookGroceriesOrder(items);
    }
}
