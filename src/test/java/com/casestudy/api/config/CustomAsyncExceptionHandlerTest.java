package com.casestudy.api.config;

import com.casestudy.api.exception.RestartRequiredException;
import com.casestudy.api.model.Ordered;
import com.casestudy.api.service.DatabaseService;
import com.casestudy.api.service.NotifyService;
import com.casestudy.api.service.OrderService;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CustomAsyncExceptionHandlerTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private DatabaseService databaseService;

    @Autowired
    private ApplicationAvailability applicationAvailability;

    @Test
    @DirtiesContext
    public void testAsyncCreate() throws InterruptedException, ExecutionException {
        Ordered order = Ordered.builder().product("Java").build();

        when(databaseService.createNewOrder(order)).thenThrow(RestartRequiredException.class);

        orderService.createNewOrder(order);

        Thread.sleep(2000);

        Assertions.assertEquals(LivenessState.BROKEN, applicationAvailability.getLivenessState());
        Assertions.assertEquals(ReadinessState.REFUSING_TRAFFIC, applicationAvailability.getReadinessState());
    }
}
