package com.casestudy.api.service;

import com.casestudy.api.model.OrderedElastic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

@Service
public class ElasticService {
    @Autowired
    ElasticsearchOperations operations;

    public void save(OrderedElastic orderedElastic) {
        operations.save(orderedElastic);
    }

}
