package com.casestudy.api.service.impl;

import com.casestudy.api.exception.NotifyServiceTimeoutException;
import com.casestudy.api.exception.NotifyServiceUnreachableException;
import com.casestudy.api.model.Ordered;
import com.casestudy.api.rest.RestTemplateResponseErrorHandler;
import com.casestudy.api.service.DatabaseService;
import com.casestudy.api.service.NotifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class DefaultNotifyService implements NotifyService {

    @Autowired
    private Environment environment;

    @Autowired
    private DatabaseService databaseService;

    private final RestTemplate restTemplate;

    Logger logger = LoggerFactory.getLogger(DefaultNotifyService.class);

    @Autowired
    public DefaultNotifyService(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }

    @Override
    @Retryable(retryFor = {NotifyServiceUnreachableException.class, NotifyServiceTimeoutException.class})
    public ResponseEntity<String> orderNotify() {
        List<Ordered> unnoticedOrders = databaseService.getUnnoticedOrders();

        for (Ordered ordered: unnoticedOrders) {
            HttpEntity<Ordered> request = new HttpEntity<>(ordered);
            String url = environment.getProperty("notify-service.url", "http://localhost:8000/notify");

            restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            databaseService.updateOrderNotified(ordered);
        }
        return ResponseEntity.ok().build();
    }

    @Recover
    public ResponseEntity<String> notifyFailed(Ordered ordered) {
        logger.error("Notify failed");
        return ResponseEntity.unprocessableEntity().build();
    }
}
