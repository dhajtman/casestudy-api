package com.casestudy.api.service;

import com.casestudy.api.exception.OrderNotFoundException;
import com.casestudy.api.model.Ordered;
import com.casestudy.api.repository.OrderHQLRepository;
import com.casestudy.api.repository.OrderJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DatabaseService {
    @Autowired
    private OrderJPARepository orderJPARepository;

    @Autowired
    private OrderHQLRepository orderHQLRepository;

    public List<Ordered> getAllOrders() {
        return new ArrayList<>(orderHQLRepository.getOrders());
    }

    public Ordered getOrderedById(long orderId) {
        return orderHQLRepository.getOrderById(orderId);
    }

    public List<Ordered> getAllOrder() {
        return new ArrayList<>(orderJPARepository.findAll());
    }

    public List<Ordered> getUnnoticedOrders() {
        return new ArrayList<>(orderJPARepository.findByNotifiedFalse());
    }

    public List<Ordered> findByProduct(String product) {
        return new ArrayList<>(orderJPARepository.findByProduct(product));
    }

    public List<Ordered> findByMinQuantity(int quantity) {
        return new ArrayList<>(orderJPARepository.findByMinQuantity(quantity));
    }

    public List<Ordered> findByMaxQuantity(int quantity) {
        return new ArrayList<>(orderJPARepository.findByMaxQuantity(quantity));
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public Ordered updateOrderNotified(Ordered ordered) {
        ordered.setNotified(true);
        return orderJPARepository.save(ordered);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public Ordered createNewOrder(Ordered ordered) {
        return orderJPARepository.save(ordered);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public Ordered updateOrder(Ordered ordered) {
        return orderJPARepository.save(ordered);
    }

    public Ordered getOrderById(Long id) {
        return orderJPARepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public void deleteById(Long id) {
        orderJPARepository.deleteById(id);
    }
}
