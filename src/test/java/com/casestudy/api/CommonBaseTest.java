package com.casestudy.api;

import org.springframework.kafka.test.context.EmbeddedKafka;

@EmbeddedKafka(partitions = 1,
        brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" },
        topics = {"${kafka.topic-name-string}", "${kafka.topic-name-object}", "${kafka.stream-input-topic}"
                , "${kafka.stream-output-topic}", "${kafka.stream-filtered-topic}"})
public class CommonBaseTest {
}
