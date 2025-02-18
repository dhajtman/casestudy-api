package com.casestudy.api.controller;

import com.casestudy.api.model.Ordered;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
  public String notify(@RequestBody Ordered ordered) {

    logger.info("Notified of {}", ordered.getProduct());

    return "Notified";
  }
}
