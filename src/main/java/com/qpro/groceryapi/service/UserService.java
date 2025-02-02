package com.qpro.groceryapi.service;

import com.qpro.groceryapi.model.GroceryItem;
import com.qpro.groceryapi.model.GroceryOrder;
import com.qpro.groceryapi.model.Inventory;
import com.qpro.groceryapi.repository.GroceryItemRepository;
import com.qpro.groceryapi.repository.GroceryOrderRepository;
import com.qpro.groceryapi.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    GroceryItemRepository groceryItemRepository;

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    GroceryOrderRepository orderRepository;

    @Autowired
    GroceryOrderService service;

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

    public long bookGroceriesOrder(List<GroceryItem> items) {
        GroceryOrder groceryOrder = service.createOrder(items);;
        return groceryOrder.getOrderId();
    }

    public void deleteOrderById(long id) {
        service.removeOrder(id);
    }

    public GroceryOrder updateOrder(long id, List<GroceryItem> updatedList) {
        return service.updateOrder(id, updatedList);
    }
}
