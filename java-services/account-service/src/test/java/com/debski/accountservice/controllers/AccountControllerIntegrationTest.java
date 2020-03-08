package com.debski.accountservice.controllers;

import com.debski.accountservice.configurations.WithMockOAuth2Scope;
import com.debski.accountservice.entities.Account;
import com.debski.accountservice.enums.Role;
import com.debski.accountservice.repositories.AccountRepository;
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

import java.security.Principal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AccountControllerIntegrationTest {

    private static final String ACCOUNT_JSON = "{\"username\":\"user\",\"password\":\"Password1\",\"email\":\"xyz@gmail.com\"}";
    private static final String INCOME_JSON = "{\"frequency\":0,\"amount\":1500,\"currency\":0,\"dateOfIncome\":\"2020-01-01\",\"incomeCategory\":0,\"note\":\"elo elo dupa\"}";
    private static final String INCOME_JSON_ENUMERATED_STRING = "{\"incomes\":[{\"frequency\":0,\"amount\":1500,\"currency\":0,\"dateOfIncome\":\"2020-01-01\",\"incomeCategory\":0,\"note\":\"elo elo dupa\"}]}";

    @Autowired
    private AccountController accountController;
    @Autowired
    private AccountExceptionHandler accountExceptionHandler;
    @Autowired
    private AccountRepository accountRepository;

    private MockMvc mockMvc;
    private Principal userPrincipal;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(accountController).setControllerAdvice(accountExceptionHandler)
                .build();
        this.userPrincipal = new Principal() {
            @Override
            public String getName() {
                return "user";
            }
        };
    }

    @Test
    public void shouldReturnStatus200WithAccountDtoInBody() throws Exception {
        //when
        mockMvc.perform(post("/create").contentType(MediaType.APPLICATION_JSON).content(ACCOUNT_JSON))
                //then
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnAccountErrorDtoWhenAccountExceptionWasThrown() throws Exception {
        //when
        mockMvc.perform(post("/create").contentType(MediaType.APPLICATION_JSON).content(ACCOUNT_JSON.replace("Password1", "weakpassword")))
                //then
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @WithMockOAuth2Scope(scope = "ui")
    @Test
    public void shouldReturnErrorWhenUpdatedUserIsNotFoundInDatabase() throws Exception {
        //when
        mockMvc.perform(put("/current/add/outcome")
                .principal(userPrincipal)
                .contentType(MediaType.APPLICATION_JSON)
                .content(INCOME_JSON))
                //then
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorMessage").value("Current user not found"))
                .andExpect(jsonPath("$.source").value("account-service"));
    }

    @WithMockOAuth2Scope(scope = "ui")
    @Test
    public void shouldUpdateUserIncomes() throws Exception {
        //given
        Account user = Account.builder()
                            .username("user")
                            .enabled(true)
                            .password("Password1")
                            .role(Role.USER)
                            .email("user@gmail.com")
                            .build();
        accountRepository.save(user);
        //when
        mockMvc.perform(put("/current/add/income")
                .principal(userPrincipal)
                .contentType(MediaType.APPLICATION_JSON)
                .content(INCOME_JSON))
                //then
                .andExpect(status().isOk());
    }
}
