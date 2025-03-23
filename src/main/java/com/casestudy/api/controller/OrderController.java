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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/order")
public class OrderController {
  @Autowired
  private OrderService orderService;

  Logger logger = LoggerFactory.getLogger(OrderController.class);

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<Ordered> getAllOrder() {
    return orderService.getAllOrder();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.ACCEPTED)
  public ResponseEntity<OrderResponse> createOrder(@RequestBody Ordered ordered) throws InterruptedException, ExecutionException {
    if (ordered.getProduct() == null) {
      throw new BadRequestException("The Product must be provided when creating a new Order");
    }

    logger.info("Creating new order for {}", ordered.getProduct());
    orderService.createNewOrder(ordered);

    return new ResponseEntity<>(new OrderResponse("Order creation queued: " + ordered.getProduct()), HttpStatus.ACCEPTED);
  }

  @GetMapping("/notify")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<OrderResponse> orderNotify() {
    logger.info("Processing unnoticed orders with restTemplate");
    orderService.orderNotify();

    return new ResponseEntity<>(new OrderResponse("Unnoticed orders processed"), HttpStatus.OK);
  }

  @GetMapping("/notifyNew")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<OrderResponse> orderNotifyNew() {
    logger.info("Processing unnoticed orders with restClient");
    orderService.orderNotifyNew();

    return new ResponseEntity<>(new OrderResponse("Unnoticed orders processed"), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Ordered getOrder(@PathVariable("id") long id) {
    Optional<Ordered> order =  orderService.getOrderById(id);
    if (order.isPresent())
      return order.get();

    throw new OrderNotFoundException("not found");
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public ResponseEntity<OrderResponse> deleteOrder(@PathVariable("id") long id) throws InterruptedException {
    Optional<Ordered> order =  orderService.getOrderById(id);
    if (!order.isPresent())
      throw new OrderNotFoundException("not found");

    logger.info("Deleting order with {}", id);
    orderService.deleteById(id);

    return new ResponseEntity<>(new OrderResponse("Order deletion queued: " + id), HttpStatus.ACCEPTED);
  }
}
