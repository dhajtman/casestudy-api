package com.casestudy.api;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class OrderApplication implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(OrderApplication.class);

    @Getter
    @Setter
    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(OrderApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        context = application.run(args);
    }

    @Override
    public void run(String... args) {
        logger.info("{} started with args: {}", OrderApplication.class.getName(), args);
    }
}