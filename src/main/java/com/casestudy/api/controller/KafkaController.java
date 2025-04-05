package com.casestudy.api.controller;

import com.casestudy.api.rest.OrderResponse;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.IntStream;

@RestController
@RequestMapping("/kafka")
public class KafkaController {
    Logger logger = LoggerFactory.getLogger(KafkaController.class);
    private final KafkaTemplate<String, Object> template;
    private final String topicName;
    private final int messagesPerRequest;

    public KafkaController(
            final KafkaTemplate<String, Object> template,
            @Value("${kafka.topic-name}") final String topicName,
            @Value("${kafka.messages-per-request}") final int messagesPerRequest) {
        this.template = template;
        this.topicName = topicName;
        this.messagesPerRequest = messagesPerRequest;
    }

    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<OrderResponse> test() throws Exception {
        IntStream.range(0, messagesPerRequest)
                .forEach(i -> this.template.send(topicName, String.valueOf(i),
                       "Kafka message " + i)
                );
        return new ResponseEntity<>(new OrderResponse("Test Done"), HttpStatus.OK);
    }

    @KafkaListener(topics = "my-topic", clientIdPrefix = "json",
            containerFactory = "kafkaListenerContainerFactory")
    public void listenAsObject(ConsumerRecord<String, String> cr,
                               @Payload String payload) {
        logger.info("Received Kafka message key {}: Payload: {} | Record: {}", cr.key(), payload, cr.toString());
    }
}
