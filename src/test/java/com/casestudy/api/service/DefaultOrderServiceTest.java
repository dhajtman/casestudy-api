package com.casestudy.api.service;

import com.casestudy.api.model.Ordered;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DefaultOrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private NotifyService notifyService;

    @Test
    @DirtiesContext
    public void testAsyncCreate() throws InterruptedException, ExecutionException {
        when(notifyService.notify(any(Ordered.class))).thenReturn(ResponseEntity.ok().build());

        Ordered order = Ordered.builder().product("Java").build();
        CompletableFuture<Ordered> completableFuture = orderService.createNewOrder(order);

        while (true) {
            if (completableFuture.isDone()) {
                Assertions.assertNotNull(completableFuture.get());
                break;
            }
            Thread.sleep(1000);
        }
    }

    @Test
    @DirtiesContext
    public void testGetAll() {
        List<Ordered> orderedList = orderService.getAllOrder();
        Assertions.assertEquals(3, orderedList.size());
    }
}
