package com.jw.study.controller;

import com.jw.study.account.AccountRepository;
import com.jw.study.account.AccountService;
import com.jw.study.account.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.ResponseEntity.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;


    @BeforeEach
    void beforeEach() {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setUsername("jinu");
        signUpForm.setEmail("email@gmail.com");
        signUpForm.setPassword("12345678");
        accountService.processNewAccount(signUpForm);

    }

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();

    }

    @Test
    void login_with_email() throws Exception {

        mockMvc.perform(post("/login")
                        .param("username", "email@gmail.com")
                        .param("password", "12345678")
                        .with(csrf()))
                        .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("jinu"));

    }

    @Test
    void login_with_username()throws Exception {

        mockMvc.perform(post("/login")
                        .param("username", "jinu")
                        .param("password", "12345678")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("jinu"));
    }

    @DisplayName("로그인 실패")
    @Test

    void login_fail()throws Exception {

        mockMvc.perform(post("/login")
                        .param("username", "1111")
                        .param("password", "22222")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(authenticated().withUsername("jinu"));
    }

    @DisplayName("로그아웃")
    @Test

    void logout()throws Exception {

        mockMvc.perform(post("/logout")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(unauthenticated());
    }

}
