package com.qpro.groceryapi;

import com.qpro.groceryapi.model.GroceryItem;
import com.qpro.groceryapi.model.Inventory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserActionsTest {
    @Autowired
    TestRestTemplate restTemplate;

    List<Inventory> inventories;

    // View the list of available grocery items
    @Test
    public void userViewAvailableGroceryItemsTest() {
        ResponseEntity<List<GroceryItem>> items = restTemplate.exchange("/user/items",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        List<GroceryItem> list = items.getBody();

        assert !Objects.requireNonNull(list).isEmpty();
        assert items.getStatusCode().is2xxSuccessful();
    }

    @Test
    public void userCanBookGroceryOrderTest() {
        // make list of items want to book
        GroceryItem tomato = new GroceryItem();
        tomato.setName("tomato");
        tomato.setQuantity(10);

        GroceryItem potato = new GroceryItem();
        potato.setName("potato");
        potato.setQuantity(20);

        // make order of them
        List<GroceryItem> items = List.of(tomato, potato);

        // send order (i want this order of items)
        ResponseEntity<Long> orderId = restTemplate.exchange("/user/order",
                HttpMethod.POST,
                new HttpEntity<>(items),
                Long.class);

        // get ack
        assert orderId.getBody() != null;
        // can view order items
    }

    @BeforeAll
    public void addInventories() {
        inventories = AdminActionsTest.createInventory();
        // post all inventories
        ResponseEntity<List<Inventory>> inventory = restTemplate.exchange(
                "/admin/add_inventories",
                HttpMethod.POST,
                new HttpEntity<>(inventories),
                new ParameterizedTypeReference<>() {
                }
        );
        assert Objects.requireNonNull(inventory.getBody()).size() == 3;
    }

    @AfterAll
    public void cleanInventories() {
        restTemplate.delete("/admin/cleanup");

        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<List<Inventory>> response = restTemplate.exchange(
                "/admin/inventories",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                }
        );
        List<Inventory> inventoryList = response.getBody();
        assert Objects.requireNonNull(inventoryList).isEmpty();
    }
}
