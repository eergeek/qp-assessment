package com.qpro.groceryapi;

import com.qpro.groceryapi.model.GroceryItem;
import com.qpro.groceryapi.model.GroceryOrder;
import com.qpro.groceryapi.model.Inventory;
import com.qpro.groceryapi.model.User;
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
public class UserActionsTest {
    @Autowired
    TestRestTemplate restTemplate;
    List<Inventory> inventories;
    HttpHeaders headers;

    // View the list of available grocery items
    @Test
    public void userViewAvailableGroceryItemsTest() {
        headers.setBasicAuth("ross", "ross");

        ResponseEntity<List<GroceryItem>> items = restTemplate.exchange("/user/items",
                HttpMethod.GET,
                new HttpEntity<>(headers),
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

        // make order of these
        headers.setBasicAuth("john", "john");
        List<GroceryItem> items = List.of(tomato, potato);

        // send order
        ResponseEntity<Long> orderId = restTemplate.exchange("/user/order/john",
                HttpMethod.POST,
                new HttpEntity<>(items, headers),
                Long.class);

        // get ack
        assert orderId.getBody() != null;
        // can view order items
    }

    @Test
    public void userCanDeleteGroceryOrderTest() {
        headers.setBasicAuth("ross", "ross");

        ResponseEntity<Void> response = restTemplate.exchange("/user/rmorder/1",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class);
        assert response.getStatusCode().is4xxClientError();
    }

    @Test
    public void userCanUpdateGroceryOrder() {
        // make list of items want to book
        GroceryItem tomato = new GroceryItem();
        tomato.setName("tomato");
        tomato.setQuantity(10);

        GroceryItem potato = new GroceryItem();
        potato.setName("potato");
        potato.setQuantity(20);
        // make order of them
        List<GroceryItem> items = new ArrayList<>(List.of(potato, tomato));
        headers.setBasicAuth("ross", "ross");

        // send order (i want this order of items)
        ResponseEntity<Long> orderId = restTemplate.exchange("/user/order",
                HttpMethod.POST,
                new HttpEntity<>(items, headers),
                Long.class);
        // get ack
        assert orderId.getBody() != null;

        // remove potato
        long id = orderId.getBody();
        items.remove(1);
        items.get(0).setQuantity(25);

        ResponseEntity<GroceryOrder> updated = restTemplate.exchange("/user/orderupdate/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(items, headers),
                GroceryOrder.class);

        assert updated.getBody() != null;
        assert updated.getBody().getItems().get(0).getQuantity() == 25;
    }

    @BeforeAll
    public void addInventoriesAndUsers() {
        headers = new HttpHeaders();
        headers.setBasicAuth("admin", "admin");
        inventories = AdminActionsTest.createInventory();
        // post all inventories
        ResponseEntity<ListWrapper> inventory = restTemplate.exchange(
                "/admin/add_inventories",
                HttpMethod.POST,
                new HttpEntity<>(inventories, headers),
                ListWrapper.class
        );
        assert Objects.requireNonNull(inventory.getBody().getWrappedList()).size() == 3;

        // add users
        List<User> dummyUser = createDummyUsers();
        ListWrapper<User> wrapper = new ListWrapper<>();
        wrapper.setWrappedList(dummyUser);

        ResponseEntity<ListWrapper> users = restTemplate.exchange(
                "/admin/addusers",
                HttpMethod.POST,
                new HttpEntity<>(wrapper, headers),
                ListWrapper.class
        );

        assert users.getBody() != null;
        List<User> usersList = users.getBody().wrappedList;
        assert usersList.size() == 2;
    }

    @AfterAll
    public void cleanInventories() {
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

    public static List<User> createDummyUsers() {
        User john = new User();
        john.setUsername("john");

        User ross = new User();
        ross.setUsername("ross");

        List<User> usersList = new ArrayList<>(List.of(john, ross));
        return usersList;
    }
}
