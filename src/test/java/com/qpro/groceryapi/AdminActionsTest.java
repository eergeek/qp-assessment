package com.qpro.groceryapi;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdminActionsTest {
    @Autowired
    TestRestTemplate restTemplate;
    List<Inventory> inventories;
    String url = "/admin";

    /**
     * - Add new grocery items to the system
     *   - View existing grocery items
     *   - Remove grocery items from the system
     *   - Update details (e.g., name, price) of existing grocery items
     *   - Manage inventory levels of grocery items
     */

    @BeforeAll
    public void addInventories() {
        inventories = createInventory();
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

    @Test
    public void getInventories() {
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<List<Inventory>> response = restTemplate.exchange(
                "/admin/inventories",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        List<Inventory> inventoryList = response.getBody();
        assert !Objects.requireNonNull(inventoryList).isEmpty();
    }

    @Test
    public void updateInventory() {
        Inventory update = new Inventory();
        update.setPricePerItem(222);
        update.setAvailableQnty(10);
        update.setTotalQnty(1000);

        restTemplate.put("/admin/put_inventory/1", update);

        Inventory updated = restTemplate.exchange("/admin/inventorybyid/1",
                HttpMethod.GET,
                null,
                Inventory.class).getBody();

        assert Objects.requireNonNull(updated).getTotalQnty() == update.getTotalQnty();
    }

    @Test
    public void deleteInventory() {
        restTemplate.exchange(url + "del_inventory/1",
                HttpMethod.DELETE,
                new HttpEntity<>(null),
                Void.class);

        // try to get deleted item
        ResponseEntity<Inventory> deletedItem = restTemplate.exchange(url + "/inventory/1",
                HttpMethod.GET,
                null,
                Inventory.class);

        assert deletedItem.getStatusCode().is4xxClientError();
    }


    // create dummy data for inventory
    public static List<Inventory> createInventory() {
        Inventory tomato = new Inventory();
        tomato.setItemName("tomato");
        tomato.setTotalQnty(110);
        tomato.setAvailableQnty(110);
        tomato.setPricePerItem(12);

        Inventory potato = new Inventory();
        potato.setItemName("potato");
        potato.setTotalQnty(200);
        potato.setAvailableQnty(200);
        potato.setPricePerItem(20);

        Inventory chilli = new Inventory();
        chilli.setItemName("chilli");
        chilli.setTotalQnty(50);
        chilli.setAvailableQnty(50);
        chilli.setPricePerItem(10);

        return new ArrayList<>(List.of(tomato, potato, chilli));
    }

}
