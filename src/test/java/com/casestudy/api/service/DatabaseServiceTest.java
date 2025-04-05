package com.casestudy.api.service;

import com.casestudy.api.CommonBaseTest;
import com.casestudy.api.model.Ordered;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
@Testcontainers
public class DatabaseServiceTest extends CommonBaseTest {

    @Autowired
    private DatabaseService databaseService;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:17.4"
    );

    @Test
    @DirtiesContext
    public void testCreate() {
        Ordered order = Ordered.builder().product("Java").build();
        databaseService.createNewOrder(order);
        List<Ordered> orderedList = databaseService.getAllOrder();
        Assertions.assertEquals(4, orderedList.size());
    }

    @Test
    @DirtiesContext
    public void testGetAll() {
        List<Ordered> orderedList = databaseService.getAllOrder();
        Assertions.assertEquals(3, orderedList.size());
    }

    @Test
    @DirtiesContext
    public void testGetAllOrders() {
        List<Ordered> orderedList = databaseService.getAllOrders();
        Assertions.assertEquals(3, orderedList.size());
    }

    @Test
    @DirtiesContext
    public void testGetOrderedById() {
        Ordered ordered = databaseService.getOrderedById(1L);
        Assertions.assertNotNull(ordered);
        Assertions.assertEquals(1L, ordered.getId());
    }

    @Test
    @DirtiesContext
    public void testUpdate() {
        Ordered order = databaseService.getOrderById(1L);
        order.setProduct("JavaNew");
        Ordered ordered = databaseService.updateOrder(order);
        Assertions.assertEquals("JavaNew", ordered.getProduct());

        order = databaseService.getOrderById(1L);
        Assertions.assertEquals("JavaNew", order.getProduct());
    }

    @Test
    @DirtiesContext
    public void testGetById() {
        Ordered order = databaseService.getOrderById(1L);

        Assertions.assertEquals(1, order.getId());
    }

    @Test
    @DirtiesContext
    public void testGetUnnoticedOrders() {
        List<Ordered> orderedList = databaseService.getUnnoticedOrders();

        Assertions.assertEquals(3, orderedList.size());
    }

    @Test
    @DirtiesContext
    public void testFindByMinQuantity() {
        List<Ordered> orderedList = databaseService.findByMinQuantity(0);
        Assertions.assertEquals(3, orderedList.size());

        orderedList = databaseService.findByMinQuantity(11);
        Assertions.assertEquals(2, orderedList.size());

        orderedList = databaseService.findByMinQuantity(21);
        Assertions.assertEquals(1, orderedList.size());

        orderedList = databaseService.findByMinQuantity(31);
        Assertions.assertEquals(0, orderedList.size());
    }

    @Test
    @DirtiesContext
    public void testFindByMaxQuantity() {
        List<Ordered> orderedList = databaseService.findByMaxQuantity(0);
        Assertions.assertEquals(0, orderedList.size());

        orderedList = databaseService.findByMaxQuantity(11);
        Assertions.assertEquals(1, orderedList.size());

        orderedList = databaseService.findByMaxQuantity(21);
        Assertions.assertEquals(2, orderedList.size());

        orderedList = databaseService.findByMaxQuantity(31);
        Assertions.assertEquals(3, orderedList.size());
    }
}
