package com.casestudy.api.controller;

import com.casestudy.api.service.RestartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secured")
public class RestartController {

    Logger logger = LoggerFactory.getLogger(RestartController.class);

    @Autowired
    private RestartService restartService;

    @GetMapping("/restart")
    public void restart1() {
        logger.info("Order app going to restart");
        restartService.restart();
    }

    @GetMapping("/test")
    public String test() {
        logger.info("Test ping ok");
        return "test ok";
    }
}
