package com.casestudy.api.controller;

import com.casestudy.api.exception.OrderNotFoundException;
import com.casestudy.api.exception.ServiceBTimeoutException;
import com.casestudy.api.model.Ordered;
import com.casestudy.api.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {
  private final OrderService orderService;

  @Autowired
  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<Ordered> getAllOrder() {
    return orderService.getAllOrder();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Ordered createOrder(@RequestBody Ordered ordered) {
    return orderService.createNewOrder(ordered);
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
  @ResponseStatus(HttpStatus.OK)
  public void deleteOrder(@PathVariable("id") long id) {
    Optional<Ordered> order =  orderService.getOrderById(id);
    if (!order.isPresent())
      throw new OrderNotFoundException("not found");

    orderService.deleteById(id);
  }
}
