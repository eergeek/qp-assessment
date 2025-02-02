package com.qpro.groceryapi.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "g_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable=false, unique=true)
    private String username;
//
//    @Column(nullable=false)
//    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<GroceryOrder> orders;
}
