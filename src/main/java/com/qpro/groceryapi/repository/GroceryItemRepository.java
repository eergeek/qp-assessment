package com.qpro.groceryapi.repository;

import com.qpro.groceryapi.model.GroceryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroceryItemRepository extends JpaRepository<GroceryItem, Long> {
    public List<GroceryItem> findByGroceryOrder_orderId(Long id);
}
