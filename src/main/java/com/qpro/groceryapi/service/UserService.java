package com.qpro.groceryapi.service;

import com.qpro.groceryapi.model.GroceryItem;
import com.qpro.groceryapi.model.GroceryOrder;
import com.qpro.groceryapi.repository.GroceryItemRepository;
import com.qpro.groceryapi.repository.GroceryOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    GroceryItemRepository groceryItemRepository;

    @Autowired
    GroceryOrderRepository orderRepository;

    public List<GroceryItem> getAvailableGroceryItems() {
        return groceryItemRepository.findAll();
    }

    public GroceryOrder bookGroceriesOrder(List<GroceryItem> items) {
        GroceryOrder groceryOrder = new GroceryOrder();
        groceryOrder.setItems(items);
        return orderRepository.save(groceryOrder);
    }
}
