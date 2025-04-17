package com.casestudy.api.controller;

import com.casestudy.api.model.OrderedKafka;
import com.casestudy.api.http.OrderResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
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
@Slf4j
public class KafkaController {

    private final KafkaTemplate<String, Object> stringTemplate;
    private final KafkaTemplate<String, Object> objectTemplate;
    private final String topicNameString;
    private final String topicNameObject;
    private final int messagesPerRequest;

    public KafkaController(
            @Qualifier("stringKafkaTemplate") final KafkaTemplate<String, Object> stringTemplate,
            @Qualifier("objectKafkaTemplate") final KafkaTemplate<String, Object> objectTemplate,
            @Value("${kafka.topic-name-string}") final String topicNameString,
            @Value("${kafka.topic-name-object}") final String topicNameObject,
            @Value("${kafka.messages-per-request}") final int messagesPerRequest) {
        this.stringTemplate = stringTemplate;
        this.objectTemplate = objectTemplate;
        this.topicNameString = topicNameString;
        this.topicNameObject = topicNameObject;
        this.messagesPerRequest = messagesPerRequest;
    }

    @GetMapping("/testString")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<OrderResponse> testString() throws Exception {
        IntStream.range(0, messagesPerRequest)
                .forEach(i -> this.stringTemplate.send(topicNameString, String.valueOf(i),
                        "Kafka message " + i)
                );
        return new ResponseEntity<>(new OrderResponse("Test String Done"), HttpStatus.OK);
    }

    @GetMapping("/testObject")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<OrderResponse> testObject() throws Exception {
        IntStream.range(0, messagesPerRequest)
                .forEach(i -> this.objectTemplate.send(topicNameObject, String.valueOf(i),
                        new OrderedKafka("Kafka message " + i, i))
                );
        return new ResponseEntity<>(new OrderResponse("Test Object Done"), HttpStatus.OK);
    }

    @KafkaListener(topics = "${kafka.topic-name-string}", containerFactory = "stringKafkaListenerContainerFactory")
    public void listenAsString(ConsumerRecord<String, String> cr, @Payload String payload) {
        log.info("Received Kafka String message key {}: Payload: {} | Record: {}", cr.key(), payload, cr.toString());
    }

    @KafkaListener(topics = "${kafka.topic-name-object}", containerFactory = "objectKafkaListenerContainerFactory")
    public void listenAsObject(ConsumerRecord<String, OrderedKafka> cr, @Payload OrderedKafka payload) {
        log.info("Received Kafka Object message key {}: Payload: {} | Record: {}", cr.key(), payload, cr.toString());
    }

    @GetMapping("/sendToInputTopic")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<OrderResponse> sendToInputTopic(@Value("${kafka.messages-per-request}") int messagesPerRequest) {
        IntStream.range(0, messagesPerRequest)
                .forEach(i -> this.stringTemplate.send("input-topic", String.valueOf(i), "Input message " + i + " " + System.currentTimeMillis()));
        return new ResponseEntity<>(new OrderResponse("Messages sent to input-topic"), HttpStatus.OK);
    }
}
