package com.casestudy.api;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.Collections;
import java.util.Properties;

@EmbeddedKafka(partitions = 1,
        brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" },
        topics = {"${kafka.topic-name-string}", "${kafka.topic-name-object}", "${kafka.stream-input-topic}"
                , "${kafka.stream-output-topic}", "${kafka.stream-filtered-topic}"})
public class KafkaCommonBaseTest {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @BeforeEach
    public void cleanUpKafkaTopics() {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        try (AdminClient adminClient = AdminClient.create(props)) {
            DeleteTopicsResult deleteTopicsResult = adminClient.deleteTopics(Collections.singletonList("my-topic-string"));
            deleteTopicsResult.all().get(); // Wait for the deletion to complete
        } catch (Exception e) {
            // Log or handle the exception if needed
            System.out.println("Topic cleanup failed: " + e.getMessage());
        }
    }
}
