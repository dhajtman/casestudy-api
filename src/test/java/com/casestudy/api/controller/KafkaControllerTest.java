package com.casestudy.api.controller;

import com.casestudy.api.CommonBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@DirtiesContext
public class KafkaControllerTest extends CommonBaseTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testTest() throws Exception {
        String response = mockMvc.perform(get("/kafka/test")
                        .contentType("application/json"))
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.contains("Test Done"));
    }
}
