package com.qpro.groceryapi.service;

import com.qpro.groceryapi.model.GroceryItem;
import com.qpro.groceryapi.model.GroceryOrder;
import com.qpro.groceryapi.model.Inventory;
import com.qpro.groceryapi.model.User;
import com.qpro.groceryapi.repository.InventoryRepository;
import com.qpro.groceryapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    GroceryOrderService service;

    @Autowired
    UserRepository userRepository;

    public List<GroceryItem> getAvailableGroceryItems() {
        List<Inventory> list = inventoryRepository.findAll();

        // convert Inventory Items to Grocery Items
        Function<Inventory, GroceryItem> mapToGroceryItem = (inventory) -> {
            String name = inventory.getItemName();
            double price = inventory.getPricePerItem();

            GroceryItem item = new GroceryItem();
            item.setName(name);
            item.setPrice(price);

            return item;
        };

        return list.stream()
                .map(mapToGroceryItem).collect(Collectors.toList());
    }

    @Transactional
    public long bookGroceriesOrder(List<GroceryItem> items, String userName) {
        GroceryOrder groceryOrder = service.createOrder(items, userName);

        return groceryOrder.getOrderId();
    }

    public void deleteOrderById(long id) {
        service.removeOrder(id);
    }

    public GroceryOrder updateOrder(long id, List<GroceryItem> updatedList) {
        return service.updateOrder(id, updatedList);
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public List<User> addUsers(List<User> users) {
        return userRepository.saveAll(users);
    }
}
