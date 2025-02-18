package com.casestudy.api.service.impl;

import com.casestudy.api.model.Ordered;
import com.casestudy.api.rest.RestTemplateResponseErrorHandler;
import com.casestudy.api.service.NotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DefaultNotifyService implements NotifyService {

    @Autowired
    private Environment environment;

    private final RestTemplate restTemplate;

    @Autowired
    public DefaultNotifyService(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }

    @Override
    public ResponseEntity<String> notify(Ordered ordered) {
        HttpEntity<Ordered> request = new HttpEntity<>(ordered);

        String url = environment.getProperty("notify-service.url", "http://localhost:8000/notify");

        return restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }
}
