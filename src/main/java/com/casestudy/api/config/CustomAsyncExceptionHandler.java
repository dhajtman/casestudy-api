package com.casestudy.api.config;

import com.casestudy.api.exception.RestartRequiredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(CustomAsyncExceptionHandler.class);

    private final ApplicationEventPublisher eventPublisher;

    public CustomAsyncExceptionHandler(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void handleUncaughtException(final Throwable throwable, final Method method, final Object... obj) {
        logger.warn("Exception message - {}", throwable.getMessage());

        for (final Object param : obj) {
            logger.warn("Param - {}", param);
        }

        if (throwable instanceof RestartRequiredException) {
            AvailabilityChangeEvent.publish(this.eventPublisher, throwable, LivenessState.BROKEN);
            AvailabilityChangeEvent.publish(this.eventPublisher, throwable, ReadinessState.REFUSING_TRAFFIC);
        }
    }
}
