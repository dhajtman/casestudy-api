package com.casestudy.api.service;

import com.casestudy.api.model.Ordered;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
public class DefaultDatabaseServiceTest {

    @Autowired
    private DatabaseService databaseService;

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
    public void testUpdate() {
        Optional<Ordered> orderHolder = databaseService.getOrderById(1L);
        orderHolder.ifPresent(order -> {
            order.setProduct("JavaNew");
            Ordered ordered = databaseService.updateOrder(order);
            Assertions.assertEquals("JavaNew", ordered.getProduct());
        });

        orderHolder = databaseService.getOrderById(1L);
        orderHolder.ifPresent(order -> {
            Assertions.assertEquals("JavaNew", order.getProduct());
        });
    }

    @Test
    @DirtiesContext
    public void testGetById() {
        Optional<Ordered> order = databaseService.getOrderById(1L);

        Assertions.assertTrue(order.isPresent());
        Assertions.assertEquals(1, order.get().getId());
    }
}
