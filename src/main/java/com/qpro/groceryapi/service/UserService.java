package com.qpro.groceryapi.service;

import com.qpro.groceryapi.model.GroceryItem;
import com.qpro.groceryapi.model.Order;
import com.qpro.groceryapi.repository.GroceryRepository;
import com.qpro.groceryapi.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    GroceryRepository groceryRepository;

    @Autowired
    OrderRepository orderRepository;

    public List<GroceryItem> getAvailableGroceryItems() {
        return groceryRepository.findAll();
    }

    public Order bookGroceriesOrder(List<GroceryItem> items) {
        Order order = new Order();
        order.setGroceryItems(items);
        return orderRepository.save(order);
    }
}
