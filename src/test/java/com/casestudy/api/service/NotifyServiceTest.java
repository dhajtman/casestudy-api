package com.casestudy.api.service;

import com.casestudy.api.CommonBaseTest;
import com.casestudy.api.exception.NotifyServiceTimeoutException;
import com.casestudy.api.exception.NotifyServiceUnreachableException;
import com.casestudy.api.model.Ordered;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
public class NotifyServiceTest extends CommonBaseTest {

    @Mock
    private Environment environment;

    @Mock
    private DatabaseService databaseService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RestClient restClient;

    @InjectMocks
    private NotifyService notifyService;


    @Test
    void orderNotify_shouldNotifyOrders() {
        Ordered order = Ordered.builder().id(1L).product("Java").build();
        List<Ordered> orders = Collections.singletonList(order);

        when(databaseService.getUnnoticedOrders()).thenReturn(orders);
        when(environment.getProperty("notify-service.url", "http://localhost:8000/notify"))
                .thenReturn("http://localhost:8000/notify");
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok().build());

        ResponseEntity<String> response = notifyService.orderNotify();

        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        verify(databaseService).updateOrderNotified(order);
    }

    @Test
    void orderNotify_shouldNotifyOrdersNew() {
        Ordered order = Ordered.builder().id(1L).product("Java").build();
        List<Ordered> orders = Collections.singletonList(order);

        when(databaseService.getUnnoticedOrders()).thenReturn(orders);
        when(environment.getProperty("notify-service.url", "http://localhost:8000/notify"))
                .thenReturn("http://localhost:8000/notify");

        RestClient.RequestBodyUriSpec requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
        RestClient.RequestBodySpec requestBodySpec = mock(RestClient.RequestBodySpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(Ordered.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(String.class)).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<String> response = notifyService.orderNotifyNew();

        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
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