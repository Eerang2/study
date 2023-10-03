package com.jw.study.settings;

import com.jw.study.WithAccount;
import com.jw.study.account.domain.Account;
import com.jw.study.account.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class SettingsControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired AccountRepository accountRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }


    @WithAccount("jinu")
    @DisplayName("프로필 수정 폼")
    @Test
    void updateProfileForm() throws Exception {
        mockMvc.perform(get(SettingsController.SETTINGS_PROFILE_URL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"));

    }


    @WithAccount("jinu")
    @DisplayName("프로필 수정하기 -  정상")
    @Test
    void updateProfile() throws Exception {
        String bio = "짧은 소개를 수정하는 경우";
        mockMvc.perform(post( SettingsController.SETTINGS_PROFILE_URL)
                .param("bio", bio)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SettingsController.SETTINGS_PROFILE_URL))
                .andExpect(flash().attributeExists("message"));

        Account jinu = accountRepository.findByNickname("jinu");
        assertEquals(bio, jinu.getBio());
    }

    @WithAccount("jinu")
    @DisplayName("프로필 수정하기 -  에러")
    @Test
    void updateProfile_error() throws Exception {
        String bio = "너어어어어어어어엉어ㅓ어어무 기이이이이이이이이이이이이이이이이이이이이이이이이이잉이이일게 소개를 수정하는 경우";
        mockMvc.perform(post( SettingsController.SETTINGS_PROFILE_URL)
                        .param("bio", bio)
                        .with(csrf()))
                        .andExpect(status().isOk())
                        .andExpect(view().name(SettingsController.SETTINGS_PROFILE_VIEW_NAME))
                        .andExpect(model().attributeExists("account"))
                        .andExpect(model().attributeExists("profile"))
                        .andExpect(model().hasErrors());

        Account jinu = accountRepository.findByNickname("jinu");
        assertNull(jinu.getBio());
    }

    @WithAccount("jinu")
    @DisplayName("패스워드 수정 폼")
    @Test
    void updatePasswordForm() throws Exception {
        mockMvc.perform(get(SettingsController.SETTINGS_PASSWORD_URL))
                        .andExpect(status().isOk())
                        .andExpect(model().attributeExists("account"))
                        .andExpect(model().attributeExists("passwordForm"));

    }

    @WithAccount("jinu")
    @DisplayName("패스워드 수정하기 - 입력값 정상")
    @Test
    void updatePassword() throws Exception {
        mockMvc.perform(post(SettingsController.SETTINGS_PASSWORD_URL)
                        .param("newPassword", "12345678")
                        .param("newPasswordConfirm", "12345678")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SettingsController.SETTINGS_PASSWORD_URL))
                .andExpect(flash().attributeExists("message"));

        Account jinu = accountRepository.findByNickname("jinu");
        assertTrue(passwordEncoder.matches("12345678", jinu.getPassword()));
    }


    @WithAccount("jinu")
    @DisplayName("패스워드 수정하기 - 입력값 불일치 - 에러")
    @Test
    void updatePassword_fail() throws Exception {
        mockMvc.perform(post(SettingsController.SETTINGS_PASSWORD_URL)
                        .param("newPassword", "12345678")
                        .param("newPasswordConfirm", "87654321")
                        .with(csrf()))
                        .andExpect(status().isOk())
                        .andExpect(view().name(SettingsController.SETTINGS_PASSWORD_VIEW_NAME))
                        .andExpect(model().hasErrors())
                        .andExpect(model().attributeExists("passwordForm"))
                        .andExpect(model().attributeExists("account"));
    }

    @WithAccount("jinu")
    @DisplayName("닉네임 수정 폼")
    @Test
    void updateAccountForm() throws Exception {
        mockMvc.perform(get(SettingsController.SETTINGS_ACCOUNT_URL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("nicknameForm"));

    }

    @WithAccount("jinu")
    @DisplayName("패스워드 수정하기 - 입력값 정상")
    @Test
    void updateAccount() throws Exception {
        String newNickname = "gimin";
        mockMvc.perform(post(SettingsController.SETTINGS_ACCOUNT_URL)
                        .param("nickname", newNickname)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SettingsController.SETTINGS_ACCOUNT_URL))
                .andExpect(flash().attributeExists("message"));

        assertNotNull(accountRepository.findByNickname("gimin"));
    }

    @WithAccount("jinu")
    @DisplayName("닉네임 수정하기 -  에러")
    @Test
    void updateAccount_error() throws Exception {
        String newUsername = "|-|=[[^8^//";
        mockMvc.perform(post( SettingsController.SETTINGS_ACCOUNT_URL)
                        .param("nickname", newUsername)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SettingsController.SETTINGS_ACCOUNT_VIEW_NAME))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("nicknameForm"));


        Account jinu = accountRepository.findByNickname("jinu");
        assertNull(jinu.getBio());
    }


}