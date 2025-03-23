package com.casestudy.api.jms;

import com.casestudy.api.model.Ordered;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class JmsConsumer {

    Logger logger = LoggerFactory.getLogger(JmsConsumer.class);

    @JmsListener(destination = "test.queue", containerFactory = "myFactory")
    public void receiveOrder(Ordered ordered) {
        logger.info("Received order: {}", ordered);
    }
}
