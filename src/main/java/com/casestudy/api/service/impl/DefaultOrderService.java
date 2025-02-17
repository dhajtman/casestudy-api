package com.casestudy.api.service.impl;

import com.casestudy.api.exception.BadRequestException;
import com.casestudy.api.model.Ordered;
import com.casestudy.api.repository.OrderRepository;
import com.casestudy.api.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DefaultOrderService implements OrderService {
  private final OrderRepository orderRepository;

  @Autowired
  DefaultOrderService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @Override
  public List<Ordered> getAllOrder() {
    return new ArrayList<>(orderRepository.findAll());
  }


  @Override
  @Transactional(readOnly = false, propagation= Propagation.REQUIRED)
  public Ordered createNewOrder(Ordered ordered) {
    if (ordered.getProduct() == null) {
      throw new BadRequestException("The Product must be provided when creating a new Order");
    }

    return orderRepository.save(ordered);
  }

  @Override
  public Optional<Ordered> getOrderById(Long id) {
    return orderRepository.findById(id);
  }

  @Override
  @Transactional(readOnly = false, propagation= Propagation.REQUIRED)
  public void deleteById(Long id) {
    orderRepository.deleteById(id);
  }
}
