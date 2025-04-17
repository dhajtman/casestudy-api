package com.casestudy.api.controller;

import com.casestudy.api.model.Ordered;
import com.casestudy.api.http.NotifyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * For simulating purpose
 */
@RestController
@RequestMapping("/notify")
public class NotifyController {

    Logger logger = LoggerFactory.getLogger(NotifyController.class);

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<NotifyResponse> notify(@RequestBody Ordered ordered) {
        logger.info("Notified of {}", ordered.getProduct());

        return new ResponseEntity<>(new NotifyResponse("Notified: " + ordered.getProduct()), HttpStatus.OK);
    }

    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<NotifyResponse> test() {
        logger.info("Test");

        return new ResponseEntity<>(new NotifyResponse("Test"), HttpStatus.OK);
    }
}
