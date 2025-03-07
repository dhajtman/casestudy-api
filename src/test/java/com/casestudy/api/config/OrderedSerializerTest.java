package com.casestudy.api.config;

import com.casestudy.api.model.OrderedNew;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

@JsonTest
public class OrderedSerializerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSerialization() throws JsonProcessingException {
        OrderedNew ordered = OrderedNew.builder().product("Java").build();
        String json = objectMapper.writeValueAsString(ordered);

        Assertions.assertEquals("{\"productSerialized\":\"Java\"}", json);
    }

    @Test
    public void testDeserialization() throws JsonProcessingException {
        String json = "{\"productSerialized\":\"Java\"}";
        OrderedNew ordered = objectMapper.readValue(json, OrderedNew.class);

        Assertions.assertEquals("Java", ordered.getProduct());
    }
}
