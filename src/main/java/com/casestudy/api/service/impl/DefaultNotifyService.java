package com.casestudy.api.service.impl;

import com.casestudy.api.exception.BadRequestException;
import com.casestudy.api.model.Ordered;
import com.casestudy.api.repository.OrderRepository;
import com.casestudy.api.rest.RestTemplateResponseErrorHandler;
import com.casestudy.api.service.NotifyService;
import com.casestudy.api.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DefaultNotifyService implements NotifyService {

    @Autowired
    private Environment environment;

    private RestTemplate restTemplate;

    @Autowired
    public DefaultNotifyService(RestTemplateBuilder restTemplateBuilder) {
        RestTemplate restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }

    @Override
    public ResponseEntity<String> notify(Ordered ordered) {
        HttpEntity<Ordered> request = new HttpEntity<>(ordered);

        String url = environment.getProperty("notify-service.url", "http://localhost:8700/notify");

        return restTemplate
                .exchange(url, HttpMethod.POST, request, String.class);
    }
}
