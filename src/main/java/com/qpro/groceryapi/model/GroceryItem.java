package com.qpro.groceryapi.model;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "g_item")
public class GroceryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groceryId;
    private String name;
    private double price;
    private int quantity;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "grocery_order_id")
    private GroceryOrder groceryOrder;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;
}
