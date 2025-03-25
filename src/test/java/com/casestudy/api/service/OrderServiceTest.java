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
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private NotifyService notifyService;

    @Test
    @DirtiesContext
    public void testAsyncCreate() throws InterruptedException, ExecutionException {
        when(notifyService.orderNotify()).thenReturn(ResponseEntity.ok().build());

        Ordered order = Ordered.builder().product("Java").build();
        orderService.createNewOrder(order);
        Thread.sleep(2000);
        List<Ordered> orderedList = orderService.getAllOrder();
        Assertions.assertEquals(4, orderedList.size());
    }

    @Test
    @DirtiesContext
    public void testGetAll() {
        List<Ordered> orderedList = orderService.getAllOrder();
        Assertions.assertEquals(3, orderedList.size());
    }
}
