package com.qpro.groceryapi.model;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "g_inventory")
public class Inventory {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long idOfItem;

    private String itemName;
    private int totalQnty;
    private int availableQnty;
    private double pricePerItem;
}
