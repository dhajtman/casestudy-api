package com.casestudy.api.controller;

import com.casestudy.api.model.OrderedElastic;
import com.casestudy.api.service.ElasticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/elastic")
public class ElasticController {

    Logger logger = LoggerFactory.getLogger(ElasticController.class);

    @Autowired
    private ElasticService elasticService;

    @GetMapping("/save")
    public void save() {
        logger.info("Saving item to elastic");
        OrderedElastic orderedElastic = OrderedElastic.builder().id(1).product("Product").quantity(1).notified(false).build();
        elasticService.save(orderedElastic);
    }

}
