package com.casestudy.api.jms;

import com.casestudy.api.model.Ordered;
import com.casestudy.api.service.impl.DefaultNotifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class JmsProducer {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    Logger logger = LoggerFactory.getLogger(JmsProducer.class);

    public void sendOrder(Ordered ordered) {
        logger.info("Sending order: {}", ordered);

        this.jmsMessagingTemplate.convertAndSend("test.queue", ordered);
    }
}
