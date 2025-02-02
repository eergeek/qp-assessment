package com.qpro.groceryapi.service;

import com.qpro.groceryapi.model.GroceryItem;
import com.qpro.groceryapi.model.GroceryOrder;
import com.qpro.groceryapi.model.Inventory;
import com.qpro.groceryapi.model.User;
import com.qpro.groceryapi.repository.GroceryItemRepository;
import com.qpro.groceryapi.repository.GroceryOrderRepository;
import com.qpro.groceryapi.repository.InventoryRepository;
import com.qpro.groceryapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class GroceryOrderService {

    @Autowired
    private GroceryOrderRepository groceryOrderRepository;

    @Autowired
    private GroceryItemRepository orderItemRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public GroceryOrder createOrder(List<GroceryItem> items, String username) {
        GroceryOrder order = new GroceryOrder();
        order.setItems(items);

        User user = userRepository.findByusername(username);
        order.setUser(user);

        for (GroceryItem item : items) {
            item.setGroceryOrder(order);

            Inventory inventory = inventoryRepository.findByItemName(item.getName());
            int availableQnty = inventory.getAvailableQnty() - item.getQuantity();
            if (availableQnty < 0) {
                throw new RuntimeException("Not enough stock for item: " + inventory.getItemName());
            }
            inventory.setAvailableQnty(availableQnty);
            inventoryRepository.save(inventory);
        }

        return groceryOrderRepository.save(order);
    }

    @Transactional
    public void removeOrder(Long orderId) {
        GroceryOrder order = groceryOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        List<GroceryItem> itemList = orderItemRepository.findByGroceryOrder_orderId(order.getOrderId());
        for (GroceryItem item : itemList) {
            // Update inventory
            Inventory inventory = inventoryRepository.findByItemName(item.getName());
            int availableQnty = inventory.getAvailableQnty() + item.getQuantity();
            inventory.setAvailableQnty(availableQnty);
            inventoryRepository.save(inventory);
        }

//        orderItemRepository.deleteAllById(itemList.stream().map(GroceryItem::getGroceryId).toList());
        groceryOrderRepository.delete(order);
    }

    @Transactional
    public void removeItemFromOrder(Long orderId, GroceryItem item) {
        GroceryOrder order = groceryOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        Optional<GroceryItem> itemInOrder = order.getItems()
                .stream()
                .filter(itm -> itm.getName().equals(item.getName())).findAny();

        // update quantity from inventory when certain no of item qty removed from order
        Consumer<GroceryItem> consumer = groceryItem -> {
            Inventory inventory = itemInOrder.get().getInventory();
            int availableQnty = inventory.getAvailableQnty() + item.getQuantity();
            inventory.setAvailableQnty(availableQnty);
            inventoryRepository.save(inventory);

            // remove from order
            order.getItems()
                    .removeIf(currItem -> currItem.getName().equals(itemInOrder.get().getName()));
        };

        itemInOrder.ifPresent(consumer);
    }

    @Transactional
    public void removeQtyOfItemFromOrder(Long orderId, GroceryItem item, int qtyToRemove) {
        GroceryOrder order = groceryOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        Optional<GroceryItem> itemInOrder = order.getItems()
                .stream()
                .filter(itm -> itm.getName().equals(item.getName())).findAny();

        // update quantity from inventory when certain no of item qty removed from order
        Consumer<GroceryItem> consumer = groceryItem -> {
            Inventory inventory = itemInOrder.get().getInventory();
            int availableQnty = inventory.getAvailableQnty() + qtyToRemove;
            inventory.setAvailableQnty(availableQnty);
            inventoryRepository.save(inventory);

            // remove item qty from order
            int updatedQty = itemInOrder.get().getQuantity() - qtyToRemove;
            itemInOrder.get().setQuantity(updatedQty);

            order.getItems()
                    .stream()
                    .filter(itm -> itm.getName().equals(item.getName()))
                    .forEach(currItm -> currItm.setQuantity(updatedQty));

            groceryOrderRepository.save(order);
        };

        itemInOrder.ifPresent(consumer);
    }

    public GroceryOrder getOrderById(Long id) {
        return groceryOrderRepository.getReferenceById(id);
    }

    public GroceryOrder updateOrder(long id, List<GroceryItem> updatedList) {
        GroceryOrder order = groceryOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.getItems().clear();
        order.getItems().addAll(updatedList);
        return groceryOrderRepository.save(order);
    }
}