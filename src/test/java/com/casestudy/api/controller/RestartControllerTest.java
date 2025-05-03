package com.casestudy.api.controller;

import com.casestudy.api.service.RestartService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class RestartControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestRestTemplate template;

    @MockitoBean
    private RestartService restartService;

    @Test
    public void testRestart() throws Exception {
        doNothing().when(restartService).restart();
        ResponseEntity<String> result = template.withBasicAuth("user1", "user1Pass")
                .getForEntity("/secured/restart", String.class);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testTest() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/secured/test")
                        .with(user("user1").password("user1Pass").roles("USER")))
                .andDo(print())
                .andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void givenAuthRequestOnPrivateService_shouldSucceedWith200() throws Exception {
        ResponseEntity<String> result = template.withBasicAuth("user1", "user1Pass")
                .getForEntity("/secured/test", String.class);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void givenInvalidAuthRequestOnPrivateService_shouldSucceedWith401() throws Exception {
        ResponseEntity<String> result = template.withBasicAuth("spring", "wrong")
                .getForEntity("/secured/test", String.class);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
    }
}