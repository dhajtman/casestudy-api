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

import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@DirtiesContext
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    ObjectMapper om = new ObjectMapper();

    @Test
    @DirtiesContext
    public void testPost() throws Exception {
        Ordered expectedRecord = Ordered.builder().product("Java").build();
        String response = mockMvc.perform(post("/order")
                        .contentType("application/json")
                        .content(om.writeValueAsString(expectedRecord)))
                .andDo(print())
                .andExpect(status().isAccepted()).andReturn().getResponse().getContentAsString();

        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.contains("Java"));
    }

    @Test
    @DirtiesContext
    public void testGet() throws Exception {
        Ordered actualRecord = om.readValue(mockMvc.perform(get("/order/1")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andReturn().getResponse().getContentAsString(), Ordered.class);

        Assertions.assertEquals(1, actualRecord.getId());
    }

    @Test
    @DirtiesContext
    public void testGetAll() throws Exception {
        List<Ordered> actualRecord = om.readValue(mockMvc.perform(get("/order")
                        .contentType("application/json"))
                .andDo(print())
                .andReturn().getResponse().getContentAsString(), om.getTypeFactory().constructCollectionType(List.class, Ordered.class));
        Assertions.assertEquals(3, actualRecord.size());
    }

    @Test
    @DirtiesContext
    public void testNotify() throws Exception {
        String response = mockMvc.perform(get("/order/notify")
                        .contentType("application/json"))
                .andDo(print()).andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertNotNull(response);
    }

    @Test
    @DirtiesContext
    public void testNotifyNew() throws Exception {
        String response = mockMvc.perform(get("/order/notifyNew")
                        .contentType("application/json"))
                .andDo(print()).andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertNotNull(response);
    }

    @Test
    @DirtiesContext
    public void testNotifyJMS() throws Exception {
        String response = mockMvc.perform(get("/order/notifyJMS")
                        .contentType("application/json"))
                .andDo(print()).andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertNotNull(response);
    }
}