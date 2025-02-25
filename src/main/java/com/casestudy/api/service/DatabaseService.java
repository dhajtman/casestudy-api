package com.casestudy.api.service;

import com.casestudy.api.model.Ordered;

import java.util.List;
import java.util.Optional;

public interface DatabaseService {

  List<Ordered> getAllOrder();

  List<Ordered> getUnnoticedOrders();

  void updateOrderNotified(Ordered ordered);

  Ordered createNewOrder(Ordered ordered);

  Optional<Ordered> getOrderById(Long id);

  void deleteById(Long id);
}
