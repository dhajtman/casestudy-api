package com.casestudy.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class OrderApplication implements CommandLineRunner {
  Logger logger = LoggerFactory.getLogger(OrderApplication.class);

  private static ConfigurableApplicationContext context;

  public static void main(String[] args) {
    SpringApplication application = new SpringApplication(OrderApplication.class);
    application.setBannerMode(Banner.Mode.OFF);
    context = application.run(args);
  }

  public static void restart() {
    ApplicationArguments args = context.getBean(ApplicationArguments.class);

    Thread thread = new Thread(() -> {
      context.close();
      context = SpringApplication.run(OrderApplication.class, args.getSourceArgs());
    });

    thread.setDaemon(false);
    thread.start();
  }

  public static ApplicationContext getContext() {
    return context;
  }

  @Override
  public void run(String... args) throws Exception {
      logger.info("{} started with args: {}", OrderApplication.class.getName(), args);
  }
}
