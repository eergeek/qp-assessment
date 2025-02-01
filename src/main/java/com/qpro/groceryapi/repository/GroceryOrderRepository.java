package com.qpro.groceryapi.repository;

import com.qpro.groceryapi.model.GroceryOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroceryOrderRepository extends JpaRepository<GroceryOrder, Long> {
}
