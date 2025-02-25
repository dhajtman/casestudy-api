package com.casestudy.api;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {
  private static ConfigurableApplicationContext context;

  public static void main(String[] args) {
    SpringApplication application = new SpringApplication(Application.class);
    application.setBannerMode(Banner.Mode.OFF);
    context = application.run(args);
  }

  public static void restart() {
    ApplicationArguments args = context.getBean(ApplicationArguments.class);

    Thread thread = new Thread(() -> {
      context.close();
      context = SpringApplication.run(Application.class, args.getSourceArgs());
    });

    thread.setDaemon(false);
    thread.start();
  }

  public static ApplicationContext getContext() {
    return context;
  }
}
