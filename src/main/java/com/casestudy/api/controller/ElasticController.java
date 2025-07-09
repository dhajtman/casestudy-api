package com.casestudy.api.controller;

import com.casestudy.api.http.OrderResponse;
import com.casestudy.api.model.OrderedElastic;
import com.casestudy.api.service.ElasticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/elastic")
public class ElasticController {

    Logger logger = LoggerFactory.getLogger(ElasticController.class);

    @Autowired
    private ElasticService elasticService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<OrderResponse> save(@RequestBody OrderedElastic orderedElastic) {
        logger.info("Saving OrderedElastic to elastic: {}", orderedElastic);
        elasticService.save(orderedElastic);
        return new ResponseEntity<>(new OrderResponse("OrderedElastic saved to elastic"), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<OrderResponse> delete(@PathVariable("id") String id) {
        logger.info("Deleting OrderedElastic with id: {} from elastic", id);
        elasticService.delete(id);
        return new ResponseEntity<>(new OrderResponse("OrderedElastic deleted from elastic"), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderedElastic get(@PathVariable("id") String id) {
        logger.info("Getting OrderedElastic with id: {} from elastic", id);
        OrderedElastic orderedElastic =  elasticService.get(id);
        return orderedElastic;
    }

    @PostMapping("/deleteIndex")
    public ResponseEntity<OrderResponse> deleteIndex() {
        logger.info("Deleting OrderedElastic index from elastic");
        elasticService.deleteIndex();
        return new ResponseEntity<>(new OrderResponse("OrderedElastic index deleted from elastic"), HttpStatus.OK);
    }
}
