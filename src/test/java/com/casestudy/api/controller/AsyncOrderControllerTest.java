package com.casestudy.api.controller;

import com.casestudy.api.model.Ordered;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@DirtiesContext
class AsyncOrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    ObjectMapper om = new ObjectMapper();

    @Test
    @DirtiesContext
    public void testGet() throws Exception {
        Object result = mockMvc.perform(get("/order/async/1"))
                .andExpect(request().asyncStarted())
                .andReturn().getAsyncResult();

        Assertions.assertNotNull(result);
        Assertions.assertInstanceOf(Ordered.class, result);
        Assertions.assertEquals(1L, ((Ordered) result).getId());
    }

    @Test
    @DirtiesContext
    public void testGetAll() throws Exception {
        Object result = mockMvc.perform(get("/order/async"))
                .andExpect(request().asyncStarted())
                .andReturn().getAsyncResult();

        Assertions.assertNotNull(result);
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, ((List<?>) result).size());
    }
}