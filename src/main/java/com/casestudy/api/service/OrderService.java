package com.casestudy.api.service;

import com.casestudy.api.model.Ordered;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface OrderService {
    List<Ordered> getAllOrder();

    void createNewOrder(Ordered ordered) throws InterruptedException;

    Optional<Ordered> getOrderById(Long id);

    void deleteById(Long id) throws InterruptedException;
}
