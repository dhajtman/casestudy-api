package com.casestudy.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component("scheduledTasks")
public class OrderedScheduler {

    Logger logger = LoggerFactory.getLogger(OrderedScheduler.class);

    @Scheduled(cron = "*/5 * * * * *")
    public void heartbeat1() {
        long now = System.currentTimeMillis() / 1000;
        logger.info("heartbeat1 - {}", now);
    }

    @Scheduled(cron = "*/5 * * * * *")
    public void heartbeat2() {
        long now = System.currentTimeMillis() / 1000;
        logger.info("heartbeat2 - {}", now);
    }

    @Scheduled(cron = "*/5 * * * * *")
    public void heartbeat3() {
        long now = System.currentTimeMillis() / 1000;
        logger.info("heartbeat3 - {}", now);
    }
}
