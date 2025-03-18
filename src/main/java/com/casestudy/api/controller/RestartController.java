package com.casestudy.api.controller;

import com.casestudy.api.service.impl.DefaultRestartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestartController {

    Logger logger = LoggerFactory.getLogger(RestartController.class);

    @Autowired
    private DefaultRestartService restartService;

    @GetMapping("/secured/restart")
    public void restart1() {
        logger.info("Order app going to restart");
        restartService.restart();
    }

    @GetMapping("/secured/test")
    public String test() {
        logger.info("Test ping ok");
        return "test ok";
    }
}
