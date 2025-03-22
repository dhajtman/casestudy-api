package com.casestudy.api.service;

import com.casestudy.api.exception.NotifyServiceTimeoutException;
import com.casestudy.api.exception.NotifyServiceUnreachableException;
import com.casestudy.api.model.Ordered;
import com.casestudy.api.service.impl.DefaultNotifyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class DefaultNotifyServiceTest {

    @Mock
    private Environment environment;

    @Mock
    private DatabaseService databaseService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private DefaultNotifyService notifyService;


    @Test
    void orderNotify_shouldNotifyOrders() {
        Ordered order = Ordered.builder().id(1L).product("Java").build();
        List<Ordered> orders = Collections.singletonList(order);

        when(databaseService.getUnnoticedOrders()).thenReturn(orders);
        when(environment.getProperty("notify-service.url", "http://localhost:8000/notify"))
                .thenReturn("http://localhost:8000/notify");
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok().build());

        notifyService.orderNotify();

        verify(databaseService).updateOrderNotified(order);
    }

    @Test
    void orderNotify_shouldHandleNotifyServiceUnreachableException() {
        Ordered order = Ordered.builder().id(1L).product("Java").build();
        List<Ordered> orders = Collections.singletonList(order);

        when(databaseService.getUnnoticedOrders()).thenReturn(orders);
        when(environment.getProperty("notify-service.url", "http://localhost:8000/notify"))
                .thenReturn("http://localhost:8000/notify");
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new NotifyServiceUnreachableException(HttpStatus.SERVICE_UNAVAILABLE));

        Assertions.assertThrows(NotifyServiceUnreachableException.class, () -> {
            notifyService.orderNotify();
        });

        verify(databaseService, never()).updateOrderNotified(order);
    }

    @Test
    void orderNotify_shouldHandleNotifyServiceTimeoutException() {
        Ordered order = Ordered.builder().id(1L).product("Java").build();
        List<Ordered> orders = Collections.singletonList(order);

        when(databaseService.getUnnoticedOrders()).thenReturn(orders);
        when(environment.getProperty("notify-service.url", "http://localhost:8000/notify"))
                .thenReturn("http://localhost:8000/notify");
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new NotifyServiceTimeoutException(HttpStatus.REQUEST_TIMEOUT));

        Assertions.assertThrows(NotifyServiceTimeoutException.class, () -> {
            notifyService.orderNotify();
        });

        verify(databaseService, never()).updateOrderNotified(order);
    }
}