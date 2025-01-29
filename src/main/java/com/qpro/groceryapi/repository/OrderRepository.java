package com.qpro.groceryapi.repository;

import com.qpro.groceryapi.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
