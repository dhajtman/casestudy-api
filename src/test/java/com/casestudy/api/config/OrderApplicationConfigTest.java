package com.casestudy.api.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class OrderApplicationConfigTest {
    @Autowired
    ApplicationContext context;

    @Test
    public void taskSchedulerBeanPresence() {
        Object o1 = context.getBean(TaskScheduler.class);
        Object o2 = context.getBean("taskScheduler");

        Assertions.assertNotNull(o1);
        Assertions.assertNotNull(o2);
        Assertions.assertInstanceOf(ThreadPoolTaskScheduler.class, o1);
        Assertions.assertInstanceOf(ThreadPoolTaskScheduler.class, o2);
    }

    @Test
    public void asyncExecutorBeanPresence() {
        Object o1 = context.getBean(ThreadPoolTaskExecutor.class);
        Object o2 = context.getBean("asyncExecutor");

        Assertions.assertNotNull(o1);
        Assertions.assertNotNull(o2);
        Assertions.assertInstanceOf(ThreadPoolTaskExecutor.class, o1);
        Assertions.assertInstanceOf(ThreadPoolTaskExecutor.class, o2);
    }
}
