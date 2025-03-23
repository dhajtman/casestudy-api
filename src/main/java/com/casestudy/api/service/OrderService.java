package com.casestudy.api.service;

import com.casestudy.api.model.Ordered;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Ordered> getAllOrder();

    void createNewOrder(Ordered ordered) throws InterruptedException;

    void orderNotify();

    void orderNotifyNew();

    Optional<Ordered> getOrderById(Long id);

    void deleteById(Long id) throws InterruptedException;

    void orderNotifyJMS();
}
