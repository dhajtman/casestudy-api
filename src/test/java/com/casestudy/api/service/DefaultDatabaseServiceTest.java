package com.casestudy.api.service;

import com.casestudy.api.model.Ordered;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.concurrent.ExecutionException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext
public class DefaultDatabaseServiceTest {

    @Autowired
    private DatabaseService databaseService;

    @Test
    @DirtiesContext
    public void testAsyncCreate() throws InterruptedException, ExecutionException {
        Ordered order = Ordered.builder().product("Java").build();
        databaseService.createNewOrder(order);
        Thread.sleep(2000);
        List<Ordered> orderedList = databaseService.getAllOrder();
        Assertions.assertEquals(4, orderedList.size());
    }

    @Test
    @DirtiesContext
    public void testGetAll() {
        List<Ordered> orderedList = databaseService.getAllOrder();
        Assertions.assertEquals(3, orderedList.size());
    }
}
