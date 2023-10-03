package com.jw.study.account;

import com.jw.study.account.domain.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AccountServiceTest {
    @Autowired
    private AccountService accountService;

    @Test
    void diTest() {
        Account mockAccount = Account.builder()
                .email("test@test.com")
                .nickname("testName")
                .password("testPassword")
                .studyCreatedByWeb(true)
                .studyEnrollmentResultByEmail(true)
                .studyUpdatedByWeb(true)
                .build();
        accountService.sendSignUpConfirmEmail(mockAccount);
    }
}