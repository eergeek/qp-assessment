package com.qpro.groceryapi.repository;

import com.qpro.groceryapi.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Inventory findByItemName(String itemName);
}
