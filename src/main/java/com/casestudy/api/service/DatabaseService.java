package com.casestudy.api.service;

import com.casestudy.api.model.Ordered;
import com.casestudy.api.repository.OrderRepository;
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
public class DatabaseService {
    private final OrderRepository orderRepository;

    @Autowired
    DatabaseService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Ordered> getAllOrder() {
        return new ArrayList<>(orderRepository.findAll());
    }

    public List<Ordered> getUnnoticedOrders() {
        return new ArrayList<>(orderRepository.findByNotifiedFalse());
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public Ordered updateOrderNotified(Ordered ordered) {
        ordered.setNotified(true);
        return orderRepository.save(ordered);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public Ordered createNewOrder(Ordered ordered) {
        return orderRepository.save(ordered);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public Ordered updateOrder(Ordered ordered) {
        return orderRepository.save(ordered);
    }

    public Optional<Ordered> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }
}
