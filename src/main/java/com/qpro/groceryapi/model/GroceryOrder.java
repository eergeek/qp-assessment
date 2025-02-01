package com.qpro.groceryapi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "g_order")
public class GroceryOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @OneToMany(mappedBy = "groceryOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroceryItem> items;
}
