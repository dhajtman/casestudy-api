package com.casestudy.api.config;

import com.casestudy.api.CommonBaseTest;
import com.casestudy.api.exception.RestartRequiredException;
import com.casestudy.api.model.Ordered;
import com.casestudy.api.service.DatabaseService;
import com.casestudy.api.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
public class CustomAsyncExceptionHandlerTest extends CommonBaseTest {

    @Autowired
    private OrderService orderService;

    @MockitoBean
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
