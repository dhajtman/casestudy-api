package com.casestudy.api.controller;

import com.casestudy.api.model.Ordered;
import com.casestudy.api.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/order/async")
public class AsyncOrderController {
    @Autowired
    private OrderService orderService;

    Logger logger = LoggerFactory.getLogger(AsyncOrderController.class);

    @GetMapping()
    @Async("mvcTaskExecutor")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<List<Ordered>> getAllOrderAsync() {
        return CompletableFuture.completedFuture(orderService.getAllOrder());
    }

    @GetMapping("/{id}")
    @Async("mvcTaskExecutor")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<Ordered> getOrder(@PathVariable("id") long id) {
        Ordered order = orderService.getOrderById(id);
        return CompletableFuture.completedFuture(order);
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
                        Thread.sleep(500);
                    }
                    emitter.complete();
                } catch (Exception e) {
                    emitter.completeWithError(e);
                }
            }
        }.start();

        return emitter;
    }

    @GetMapping("/testStreamingResponseBody1")
    public StreamingResponseBody testStreamingResponseBody1() {
        StreamingResponseBody stream = out -> {
            String msg = "/srb" + " @ " + new Date();
            out.write(msg.getBytes());
        };

        return stream;
    }

    @GetMapping("/testStreamingResponseBody2")
    public ResponseEntity<StreamingResponseBody> testStreamingResponseBody2() {
        StreamingResponseBody responseBody = response -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    Thread.sleep(500);
                    response.write(("Data stream line srb - " + i + "\n").getBytes());
                    response.flush();
                } catch (InterruptedException e) {
                    logger.error("Error occurred: " + e);
                }
            }
        };

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(responseBody);
    }

    @GetMapping(value = "/testFlux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Object> testFlux() {
        return Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1))
                .map(i -> "Data stream line Flux - " + i);
    }
}
