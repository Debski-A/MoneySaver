package com.debski.accountservice.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AccountControllerIntegrationTest {

    @Autowired
    private AccountController accountController;

    @Autowired
    private AccountExceptionHandler accountExceptionHandler;

    private MockMvc mockMvc;

    private static final String ACCOUNT_JSON = "{\"username\":\"user\",\"password\":\"Password1\",\"email\":\"xyz@gmail.com\"}";

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).setControllerAdvice(accountExceptionHandler).build();
    }

    @Test
    public void shouldReturnStatus200WithAccountDtoInBody() throws Exception {
        //when
        mockMvc.perform(post("/create").contentType(MediaType.APPLICATION_JSON).content(ACCOUNT_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.username").value("user"))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.email").value("xyz@gmail.com"));
    }

    @Test
    public void shouldReturnAccountErrorDtoWhenAccountExceptionWasThrown() throws Exception {
        //when
        mockMvc.perform(post("/create").contentType(MediaType.APPLICATION_JSON).content(ACCOUNT_JSON.replace("Password1", "weakpassword")))
                //then
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }
}
