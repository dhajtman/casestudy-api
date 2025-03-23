package com.casestudy.api.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class JmsConsumer {

    Logger logger = LoggerFactory.getLogger(JmsConsumer.class);

    @JmsListener(destination = "test.queue")
    public void receiveMessage(String message) {
        logger.info("Received message: {}", message);
    }
}
