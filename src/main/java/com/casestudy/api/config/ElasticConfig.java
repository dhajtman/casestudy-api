package com.casestudy.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.support.HttpHeaders;

@Configuration
public class ElasticConfig extends ElasticsearchConfiguration {

    @Autowired
    private Environment environment;

    @Override
    public ClientConfiguration clientConfiguration() {
        String url = environment.getProperty("elasticsearch.url", "localhost:9200");
        String apiKey = environment.getProperty("elasticsearch.api-key", "");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "ApiKey " + apiKey);
        return ClientConfiguration.builder()
                .connectedTo(url).withDefaultHeaders(httpHeaders).build();
    }
}

