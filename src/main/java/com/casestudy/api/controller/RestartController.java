package com.casestudy.api.controller;

import com.casestudy.api.OrderApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestartController {

    @PostMapping("/restart")
    public void restart() {
        OrderApplication.restart();
    }
}
