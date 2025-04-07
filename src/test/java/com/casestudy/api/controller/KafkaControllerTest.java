package com.casestudy.api.controller;

import com.casestudy.api.CommonBaseTest;
import com.casestudy.api.model.OrderedKafka;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@DirtiesContext
public class KafkaControllerTest extends CommonBaseTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @Qualifier("stringKafkaTemplate")
    private KafkaTemplate<String, Object> stringKafkaTemplate;

    @Autowired
    @Qualifier("objectKafkaTemplate")
    private KafkaTemplate<String, Object> objectKafkaTemplate;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Test
    @DirtiesContext
    public void testTestString() throws Exception {
        String response = mockMvc.perform(get("/kafka/testString")
                        .contentType("application/json"))
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.contains("Test String Done"));
    }

    @Test
    @DirtiesContext
    public void testTestObject() throws Exception {
        String response = mockMvc.perform(get("/kafka/testObject")
                        .contentType("application/json"))
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.contains("Test Object Done"));
    }

    @Test
    @DirtiesContext
    void testListenAsString() {
        String topic = "my-topic-string";
        String message = "Test String message";
        stringKafkaTemplate.send(topic, "key", message);

        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafkaBroker);
        consumerProps.put("key.deserializer", StringDeserializer.class);
        consumerProps.put("value.deserializer", StringDeserializer.class);
        DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProps);
        Consumer<String, String> consumer = consumerFactory.createConsumer();
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, topic);

        ConsumerRecord<String, String> received = KafkaTestUtils.getSingleRecord(consumer, topic);

        assertThat(received.value()).isEqualTo(message);
    }

    @Test
    @DirtiesContext
    void testListenAsObject() {
        String topic = "my-topic-object";
        OrderedKafka message = new OrderedKafka("Test Object message", 1);
        objectKafkaTemplate.send(topic, "key", message);

        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafkaBroker);
        consumerProps.put("key.deserializer", StringDeserializer.class.getName());
        consumerProps.put("value.deserializer", JsonDeserializer.class.getName());
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        DefaultKafkaConsumerFactory<String, OrderedKafka> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProps);
        Consumer<String, OrderedKafka> consumer = consumerFactory.createConsumer();
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, topic);

        ConsumerRecord<String, OrderedKafka> received = KafkaTestUtils.getSingleRecord(consumer, topic);

        assertThat(received.value()).isEqualTo(message);
    }
}
