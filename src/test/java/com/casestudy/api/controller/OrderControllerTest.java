package com.casestudy.api.controller;

import com.casestudy.api.model.Ordered;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.casestudy.api.repository.OrderRepository;

import static org.hamcrest.Matchers.greaterThan;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
//@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:clean.sql")
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    ObjectMapper om = new ObjectMapper();

    @Autowired
    private OrderRepository repository;

    @Test
    @DirtiesContext
    public void testCreation() throws Exception {
        Ordered expectedRecord = Ordered.builder().product("Java").build();
        Ordered actualRecord = om.readValue(mockMvc.perform(post("/order")
                        .contentType("application/json")
                        .content(om.writeValueAsString(expectedRecord)))
                .andDo(print())
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), Ordered.class);

        Assertions.assertEquals(expectedRecord.getProduct(), actualRecord.getProduct());
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
}