package com.casestudy.api.controller;

import com.casestudy.api.model.Ordered;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Test
    @DirtiesContext
    public void testTestCallable() throws Exception {
        Object result = mockMvc.perform(get("/order/async/testCallable"))
                .andExpect(request().asyncStarted())
                .andReturn().getAsyncResult();

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Test...", result);
    }

    @Test
    @DirtiesContext
    public void testTestDeferredResult1() throws Exception {
        Object result = mockMvc.perform(get("/order/async/testDeferredResult1"))
                .andExpect(request().asyncStarted())
                .andReturn().getAsyncResult();

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Test2...3", result);
    }

    @Test
    @DirtiesContext
    public void testTestDeferredResult2() throws Exception {
        Object result = mockMvc.perform(get("/order/async/testDeferredResult2"))
                .andExpect(request().asyncStarted())
                .andReturn().getAsyncResult();

        Assertions.assertNotNull(result);

        ResponseEntity<?> response = (ResponseEntity<?>) result;

        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals("Order creation queued: JavaTest", response.getBody());
    }

    @Test
    @DirtiesContext
    public void testTestResponseBodyEmitter() throws Exception {
        MvcResult result = mockMvc.perform(get("/order/async/testResponseBodyEmitter"))
                .andExpect(request().asyncStarted())
                .andReturn();

        Assertions.assertNotNull(result);

        result = mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(content().string("Test3...0Test3...1Test3...2Test3...3Test3...4Test3...5Test3...6Test3...7Test3...8Test3...9"))
                .andReturn();

        Assertions.assertNotNull(result);

        String content = result.getResponse().getContentAsString();

        Assertions.assertEquals("Test3...0Test3...1Test3...2Test3...3Test3...4Test3...5Test3...6Test3...7Test3...8Test3...9", content);
    }

    @Test
    @DirtiesContext
    public void testTestStreamingResponseBody1() throws Exception {
        MvcResult result = mockMvc.perform(get("/order/async/testStreamingResponseBody1"))
                .andExpect(request().asyncStarted())
                .andReturn();

        Assertions.assertNotNull(result);

        result = mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertNotNull(result);

        String content = result.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("/srb @ "));
    }

    @Test
    @DirtiesContext
    public void testTestStreamingResponseBody2() throws Exception {
        MvcResult result = mockMvc.perform(get("/order/async/testStreamingResponseBody2"))
                .andExpect(request().asyncStarted())
                .andReturn();

        Assertions.assertNotNull(result);

        result = mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertNotNull(result);

        String content = result.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("Data stream line srb"));
    }

    @Test
    @DirtiesContext
    public void testTestFlux() throws Exception {
        MvcResult result = mockMvc.perform(get("/order/async/testFlux"))
                .andExpect(request().asyncStarted())
                .andReturn();

        Assertions.assertNotNull(result);

        result = mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertNotNull(result);

        String content = result.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("Data stream line Flux"));
    }
}