package com.qpro.groceryapi.controller;

import com.qpro.groceryapi.model.GroceryItem;
import com.qpro.groceryapi.model.GroceryOrder;
import com.qpro.groceryapi.service.GroceryOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class GroceryOrderController {

    @Autowired
    private GroceryOrderService groceryOrderService;

    @PostMapping
    public GroceryOrder createOrder(@RequestBody List<GroceryItem> items, String userName) {
        return groceryOrderService.createOrder(items, userName);
    }

    @GetMapping("/{id}")
    public GroceryOrder getOrder(@PathVariable Long id) {
        return groceryOrderService.getOrderById(id);
    }
}
