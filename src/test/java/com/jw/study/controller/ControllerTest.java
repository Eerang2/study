package com.jw.study.controller;


import com.jw.study.account.domain.Account;
import com.jw.study.account.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
//@SpringBootTest
@AutoConfigureMockMvc
@MockMvcTest
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @MockBean
    JavaMailSender javaMailSender;


    @DisplayName("회원가입 테스트")
    @Test
    void signUpSubmit_with_wrong_input()throws Exception {

        mockMvc.perform(post("/sign-up")
                        .param("username", "jinu")
                        .param("email", "email@gmail.com")
                        .param("password", "12345678")
                        .with(csrf()))
                        .andExpect(status().isOk())
                        .andExpect(view().name("/sign-up"));

    }

    @DisplayName("인증메일 확인 - 입력값 오류")
    @Test
    void checkEMailToken_with_wrong_input() throws Exception {
        mockMvc.perform(get("/check-email-token")
                .param("token", "sdsdsds")
                .param("email", "email@email.com"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("/account/checked-email"))
                .andExpect(unauthenticated());

    }

    @DisplayName("인증메일 확인 - 입력값 정상")
    @Test
    void checkEMailToken() throws Exception {
        Account account = Account.builder()
                .email("test@email.com")
                .password("12345678")
                .username("jinu")
                .build();
        Account newAcoount = accountRepository.save(account);
        newAcoount.generateEmailCheckToken();

        mockMvc.perform(get("/check-email-token")
                        .param("token", newAcoount.getEmailCheckToken())
                        .param("email", newAcoount.getEmail()))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("numberOfUser"))
                .andExpect(view().name("/account/checked-email"))
                .andExpect(authenticated());

    }

    @DisplayName("회원가입 처리 - 정상")
    @Test
    void signUpSubmit_with_correct_input() throws Exception {
        mockMvc.perform(post("/sign-up")
                        .param("username", "elephant")
                        .param("email", "email@email.com")
                        .param("password", "12345678")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
//                .andExpect(unauthenticated().withUsername("elephant"))

        Account account = accountRepository.findByEmail("email@email.com");
        assertNotNull(account);
        assertNotEquals(account.getPassword(), "12345678");
        assertNotNull(account.getEmailCheckToken());
        then(javaMailSender).should().send(any(SimpleMailMessage.class));

    }
}
