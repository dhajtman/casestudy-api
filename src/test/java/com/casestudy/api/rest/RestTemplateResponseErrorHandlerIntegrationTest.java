package com.casestudy.api.rest;

import com.casestudy.api.exception.NotifyServiceTimeoutException;
import com.casestudy.api.exception.NotifyServiceUnreachableException;
import com.casestudy.api.model.Ordered;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@RestClientTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RestTemplateResponseErrorHandlerIntegrationTest {

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private RestTemplateBuilder builder;

    private RestTemplate restTemplate;

    @BeforeAll
    void init() {
        restTemplate = this.builder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }

    @Test
    public void givenRemoteApiCall_when408Error_thenThrowTimeout() {
        Assertions.assertNotNull(this.builder);
        Assertions.assertNotNull(this.server);

        this.server
                .expect(ExpectedCount.once(), requestTo("/notify"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.REQUEST_TIMEOUT));

        HttpEntity<Ordered> request = new HttpEntity<>(new Ordered());

        Assertions.assertThrows(NotifyServiceTimeoutException.class, () -> {
            ResponseEntity<String> response = restTemplate.exchange("/notify", HttpMethod.POST, request, String.class);
        });
    }

    @Test
    public void givenRemoteApiCall_when503Error_thenThrowUnreachable() {
        Assertions.assertNotNull(this.builder);
        Assertions.assertNotNull(this.server);

        this.server
                .expect(ExpectedCount.once(), requestTo("/notify"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.SERVICE_UNAVAILABLE));

        HttpEntity<Ordered> request = new HttpEntity<>(new Ordered());

        Assertions.assertThrows(NotifyServiceUnreachableException.class, () -> {
            ResponseEntity<String> response = restTemplate.exchange("/notify", HttpMethod.POST, request, String.class);
        });
    }

    @Test
    public void givenRemoteApiCall_success() {
        Assertions.assertNotNull(this.builder);
        Assertions.assertNotNull(this.server);

        this.server
                .expect(ExpectedCount.once(), requestTo("/notify"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK));

        HttpEntity<Ordered> request = new HttpEntity<>(new Ordered());

        ResponseEntity<String> response = restTemplate.exchange("/notify", HttpMethod.POST, request, String.class);
        Assertions.assertNotNull(response);
    }
}
