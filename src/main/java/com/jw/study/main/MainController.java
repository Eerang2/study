package com.jw.study.main;

import com.jw.study.account.Account;
import com.jw.study.account.CurrentUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {

    @GetMapping("/")
    public String home(@CurrentUser Account account, Model model) {
        if (account != null) {
            model.addAttribute(account);
        }
        return "index";
    }

//    @GetMapping("/")
//    public String home(Model model) {
//        Account mockAccount = Account.builder()
//                .email("test@test.com")
//                .username("testName")
//                .password("testPassword")
//                .studyCreatedByWeb(true)
//                .studyEnrollmentResultByEmail(true)
//                .studyUpdatedByWeb(true)
//                .emailVerified(true)
//                .studyCreatedByEmail(false)
//                .build();
//
//        boolean hasNotification = true;
//
//        model.addAttribute(mockAccount);
//        model.addAttribute(hasNotification);
//
//        return "index";
//    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
