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
    HttpHeaders headers;
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
        headers = new HttpHeaders();
        headers.setBasicAuth("admin", "admin");
        // post all inventories
        ResponseEntity<ListWrapper> inventory = restTemplate.exchange(
                "/admin/add_inventories",
                HttpMethod.POST,
                new HttpEntity<>(inventories, headers),
                ListWrapper.class
        );

        assert Objects.requireNonNull(inventory.getBody().getWrappedList()).size() == 3;
    }

    @AfterAll
    public void cleanInventories() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "admin");

        restTemplate.exchange("/admin/cleanup",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class);

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
        headers.setBasicAuth("admin", "admin");
        ResponseEntity<List<Inventory>> response = restTemplate.exchange(
                "/admin/inventories",
                HttpMethod.GET,
                new HttpEntity<>(headers),
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

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "admin");

        restTemplate.put("/admin/put_inventory/1", new HttpEntity<>(update, headers));

        Inventory updated = restTemplate.exchange("/admin/inventorybyid/1",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Inventory.class).getBody();

        assert Objects.requireNonNull(updated).getTotalQnty() == update.getTotalQnty();
    }

    @Test
    public void deleteInventory() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "admin");
        restTemplate.exchange("/admin/del_inventory/2",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class);

        // try to get deleted item
        ResponseEntity<Inventory> deletedItem = restTemplate.exchange("/admin/inventory/1",
                HttpMethod.GET,
                new HttpEntity<>(headers),
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
