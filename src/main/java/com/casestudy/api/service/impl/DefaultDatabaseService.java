package com.casestudy.api.service.impl;

import com.casestudy.api.model.Ordered;
import com.casestudy.api.repository.OrderRepository;
import com.casestudy.api.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class DefaultDatabaseService implements DatabaseService {
  private final OrderRepository orderRepository;

  @Autowired
  DefaultDatabaseService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @Override
  public List<Ordered> getAllOrder() {
    return new ArrayList<>(orderRepository.findAll());
  }


  @Override
  @Transactional(readOnly = false, propagation= Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
  public Ordered createNewOrder(Ordered ordered) {
      return orderRepository.save(ordered);
  }

  @Override
  public Optional<Ordered> getOrderById(Long id) {
    return orderRepository.findById(id);
  }

  @Override
  @Transactional(readOnly = false, propagation= Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
  public void deleteById(Long id) {
    orderRepository.deleteById(id);
  }
}
