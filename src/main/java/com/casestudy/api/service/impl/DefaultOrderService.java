package com.casestudy.api.service.impl;

import com.casestudy.api.model.Ordered;
import com.casestudy.api.service.DatabaseService;
import com.casestudy.api.service.NotifyService;
import com.casestudy.api.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultOrderService implements OrderService {

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private NotifyService notifyService;

    @Override
    public List<Ordered> getAllOrder() {
        return databaseService.getAllOrder();
    }

    @Override
    @Async
    public void createNewOrder(Ordered ordered) throws InterruptedException {
        Thread.sleep(1000); // simulating long term operation
        Ordered created = databaseService.createNewOrder(ordered);
        ResponseEntity<String> response = notifyService.orderNotify();
    }

    @Override
    public Optional<Ordered> getOrderById(Long id) {
        return databaseService.getOrderById(id);
    }

    @Override
    @Async
    public void deleteById(Long id) throws InterruptedException {
        Thread.sleep(2000); // simulating long term operation
        databaseService.deleteById(id);
    }
}
