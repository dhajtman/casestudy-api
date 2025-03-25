package com.casestudy.api.service;

import com.casestudy.api.OrderApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class RestartService {
    private static ConfigurableApplicationContext context;

    Logger logger = LoggerFactory.getLogger(RestartService.class);


    public void restart() {
        logger.info("App is restarting");

        context = OrderApplication.getContext();

        ApplicationArguments args = context.getBean(ApplicationArguments.class);

        Thread thread = new Thread(() -> {
            context.close();
            context = SpringApplication.run(OrderApplication.class, args.getSourceArgs());
        });

        thread.setDaemon(false);
        thread.start();
    }
}
