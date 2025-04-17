package com.casestudy.api.config.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomProducerListener implements ProducerListener<String, Object> {

    @Override
    public void onSuccess(ProducerRecord<String, Object> producerRecord, RecordMetadata recordMetadata) {
        log.info("Message sent successfully to topic: " + producerRecord.topic() +
                " with key: " + producerRecord.key() + " and value: " + producerRecord.value());
    }

    @Override
    public void onError(ProducerRecord<String, Object> producerRecord, RecordMetadata recordMetadata, Exception exception) {
        log.error("Error sending message to topic: " + producerRecord.topic() +
                " with key: " + producerRecord.key() + " and value: " + producerRecord.value());
        log.error("Detailed error: " + exception);
    }
}
