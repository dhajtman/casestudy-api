package com.casestudy.api.service;

import com.casestudy.api.model.OrderedElastic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

@Service
public class ElasticService {
    @Autowired
    ElasticsearchOperations operations;

    public OrderedElastic save(OrderedElastic orderedElastic) {
        OrderedElastic orderedElastic1 = operations.save(orderedElastic);
        return orderedElastic1;
    }

    public String delete(String id) {
        String response = operations.delete(id, OrderedElastic.class);
        return response;
    }

    public boolean deleteIndex() {
        boolean result = operations.indexOps(OrderedElastic.class).delete();
        return result;
    }

    public boolean existsIndex() {
        boolean result = operations.indexOps(OrderedElastic.class).exists();
        return result;
    }

    public OrderedElastic get(String id) {
        OrderedElastic orderedElastic = operations.get(id, OrderedElastic.class);
        return orderedElastic;
    }

}
