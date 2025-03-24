package com.casestudy.api.controller;

import com.casestudy.api.exception.BadRequestException;
import com.casestudy.api.exception.OrderNotFoundException;
import com.casestudy.api.model.Ordered;
import com.casestudy.api.rest.OrderResponse;
import com.casestudy.api.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/order/async")
public class AsyncOrderController {
  @Autowired
  private OrderService orderService;

  Logger logger = LoggerFactory.getLogger(AsyncOrderController.class);

  @GetMapping()
  @Async("asyncExecutor")
  @ResponseStatus(HttpStatus.OK)
  public CompletableFuture<List<Ordered>> getAllOrderAsync() {
    return CompletableFuture.completedFuture(orderService.getAllOrder());
  }

  @GetMapping("/{id}")
  @Async("asyncExecutor")
  @ResponseStatus(HttpStatus.OK)
  public CompletableFuture<Ordered> getOrder(@PathVariable("id") long id) {
    Optional<Ordered> order =  orderService.getOrderById(id);
    if (order.isPresent())
      return CompletableFuture.completedFuture(order.get());

    throw new OrderNotFoundException("not found");
  }
}
