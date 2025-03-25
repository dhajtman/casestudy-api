package com.casestudy.api.controller;

import com.casestudy.api.exception.OrderNotFoundException;
import com.casestudy.api.model.Ordered;
import com.casestudy.api.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/order/async")
public class AsyncOrderController {
    @Autowired
    private OrderService orderService;

    Logger logger = LoggerFactory.getLogger(AsyncOrderController.class);

    @GetMapping()
    @Async("asyncExecutor")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<List<Ordered>> getAllOrderAsync() {
        return CompletableFuture.completedFuture(orderService.getAllOrder());
    }

    @GetMapping("/{id}")
    @Async("asyncExecutor")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<Ordered> getOrder(@PathVariable("id") long id) {
        Optional<Ordered> order = orderService.getOrderById(id);
        if (order.isPresent())
            return CompletableFuture.completedFuture(order.get());

        throw new OrderNotFoundException("not found");
    }

    @GetMapping(value = "/testCallable")
    public Callable<String> testCallable() {
        logger.info("Start thread id: " + Thread.currentThread().getName());
        Callable<String> callable = () -> {
            logger.info("Callable thread id1: " + Thread.currentThread().getName());
            Thread.sleep(3000);
            logger.info("Callable thread id2: " + Thread.currentThread().getName());
            return "Test...";
        };
        logger.info("End thread id: " + Thread.currentThread().getName());
        return callable;
    }

    @GetMapping(value = "/testDeferredResult1")
    public DeferredResult<String> testDeferredResult1() {
        logger.info("Start thread id: " + Thread.currentThread().getName());
        DeferredResult<String> deferredResult = new DeferredResult<>();

        new Thread("SleepingThread") {
            public void run() {
                logger.info("Thread: " + getName() + " running 1");
                deferredResult.setResult("Test2...1");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                deferredResult.setResult("Test2...2");
                logger.info("Thread: " + getName() + " running 2");
            }
        }.start();

        deferredResult.setResult("Test2...3");

        logger.info("End thread id: " + Thread.currentThread().getName());
        return deferredResult;
    }

    @RequestMapping("/testDeferredResult2")
    public DeferredResult<ResponseEntity<?>> testDeferredResult2() throws Exception {

        final DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<ResponseEntity<?>>(5000l);
        deferredResult.onTimeout(new Runnable() {
            @Override
            public void run() { // Retry on timeout
                deferredResult.setErrorResult(ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Request timeout occurred."));
            }
        });

        Ordered order = Ordered.builder().product("JavaTest").build();
        CompletableFuture<String> future = orderService.createNewOrder2(order);
        future.whenComplete((result, exception) -> {
            if (exception != null) {
                deferredResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception));
            } else {
                deferredResult.setResult(ResponseEntity.ok(result));
            }
            logger.info("Order created");
        });
        return deferredResult;
    }

    @GetMapping("/testResponseBodyEmitter")
    public ResponseBodyEmitter testResponseBodyEmitter() {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        new Thread("SleepingThread") {
            public void run() {
                try {
                    for (int i = 0; i < 10; i++) {
                        emitter.send("Test3..." + i);
                        logger.info("Test3..." + i);
                        Thread.sleep(1000);
                    }
                    emitter.complete();
                } catch (Exception e) {
                    emitter.completeWithError(e);
                }
            }
        }.start();
        return emitter;
    }
}
