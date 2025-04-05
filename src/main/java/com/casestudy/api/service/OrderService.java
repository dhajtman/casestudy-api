package com.casestudy.api.service;

import com.casestudy.api.exception.BadRequestException;
import com.casestudy.api.model.Ordered;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderService {

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private NotifyService notifyService;

    Logger logger = LoggerFactory.getLogger(OrderService.class);

    public List<Ordered> getAllOrder() {
        return databaseService.getAllOrder();
    }

    @Async("asyncExecutor")
    public void createNewOrder(Ordered ordered) throws InterruptedException {
        logger.info("Order creation started: " + ordered.getProduct());
        Thread.sleep(1000); // simulating long term operation
        Ordered created = databaseService.createNewOrder(ordered);
        ResponseEntity<String> response = notifyService.orderNotify();
    }

    @Async("asyncExecutor")
    public CompletableFuture<String> createNewOrder2(Ordered ordered) throws InterruptedException {
        logger.info("Order2 creation started: " + ordered.getProduct());
        Thread.sleep(3000); // simulating long term operation
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

    public Ordered getOrderById(Long id) {
        return databaseService.getOrderById(id);
    }

    @Async("asyncExecutor")
    public void deleteById(Long id) throws InterruptedException {
        Thread.sleep(2000); // simulating long term operation
        databaseService.deleteById(id);
    }
}
