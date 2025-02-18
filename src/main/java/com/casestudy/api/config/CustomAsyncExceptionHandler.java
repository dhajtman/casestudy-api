package com.casestudy.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    Logger logger = LoggerFactory.getLogger(CustomAsyncExceptionHandler.class);

    @Override
    public void handleUncaughtException(final Throwable throwable, final Method method, final Object... obj) {
        logger.warn("Exception message - {}", throwable.getMessage());
        for (final Object param : obj) {
            logger.warn("Param - {}", param);
        }
    }
}
