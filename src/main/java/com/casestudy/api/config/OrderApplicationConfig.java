package com.casestudy.api.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@EnableRetry
@EnableTransactionManagement
public class OrderApplicationConfig implements AsyncConfigurer {

    final private CustomAsyncExceptionHandler asyncExceptionHandler;

    @Autowired
    public OrderApplicationConfig(CustomAsyncExceptionHandler asyncExceptionHandler) {
        this.asyncExceptionHandler = asyncExceptionHandler;
    }

    @Override
    @Bean(name = "asyncExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3); // Set the core pool size
        executor.setMaxPoolSize(10); // Set the maximum pool size
        executor.setQueueCapacity(25); // Set the capacity of the task queue
        executor.setThreadNamePrefix("custom-async-"); // Set the thread name prefix
        executor.initialize();
        return executor;
    }

    @Bean(name = "taskScheduler")
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(3);
        threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        return threadPoolTaskScheduler;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return asyncExceptionHandler;
    }
}
