package com.qpro.groceryapi.repository;

import com.qpro.groceryapi.model.GroceryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroceryItemRepository extends JpaRepository<GroceryItem, Long> {
}
