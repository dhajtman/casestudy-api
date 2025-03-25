package com.casestudy.api.service;

import com.casestudy.api.model.Ordered;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderService {

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private NotifyService notifyService;

    public List<Ordered> getAllOrder() {
        return databaseService.getAllOrder();
    }

    @Async
    public void createNewOrder(Ordered ordered) throws InterruptedException {
        Thread.sleep(1000); // simulating long term operation
        Ordered created = databaseService.createNewOrder(ordered);
        ResponseEntity<String> response = notifyService.orderNotify();
    }

    public CompletableFuture<String> createNewOrder2(Ordered ordered) throws InterruptedException {
        Thread.sleep(1000); // simulating long term operation
        Ordered created = databaseService.createNewOrder(ordered);
        ResponseEntity<String> response = notifyService.orderNotify();
        return CompletableFuture.completedFuture("Order creation queued: " + ordered.getProduct());
    }

    public void orderNotifyNew() {
        ResponseEntity<String> response = notifyService.orderNotifyNew();
    }

    public void orderNotify() {
        ResponseEntity<String> response = notifyService.orderNotify();
    }

    public void orderNotifyJMS() {
        ResponseEntity<String> response = notifyService.orderNotifyJMS();
    }

    public Optional<Ordered> getOrderById(Long id) {
        return databaseService.getOrderById(id);
    }

    @Async
    public void deleteById(Long id) throws InterruptedException {
        Thread.sleep(2000); // simulating long term operation
        databaseService.deleteById(id);
    }
}
