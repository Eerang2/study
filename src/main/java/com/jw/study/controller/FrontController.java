package com.jw.study.controller;

import com.jw.study.account.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class FrontController {

    private final SignUpFormValidator signUpFormValidator;
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    @InitBinder("signUpForm")
    public void innitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

//    @GetMapping("/login")
//    public String login() {
//        return "/login";
//    }


    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute(new SignUpForm());
        return "/account/sign-up";
    }


    @PostMapping("/sign-up")
    public String signUPSubmit(@Valid SignUpForm signUpForm, Errors errors) {
        if (errors.hasErrors()) {
            return "account/sign-up";
        }

        Account account = accountService.processNewAccount(signUpForm);
        accountService.login(account);
        //return "redirect:/";
        return "redirect:/";
    }

    @GetMapping("/check-email-token")
    public String checkEmailToken(String token, String email, Model model) {
        Account account = accountRepository.findByEmail(email);
        String view = "/account/checked-email";
        if (account == null) {
            model.addAttribute("error", "wrong.email");
            return view;

        }
        if (!account.isValidToken(token)) {
            model.addAttribute("error", "wrong-email");
            return view;
        }

        account.completeSignUp();
        accountService.completeSignUp(account);
        model.addAttribute("numberOfUser", accountRepository.count());
        model.addAttribute("username", account.getUsername());
        return view;
     }

     @GetMapping("/check-email")
    public String checkEmail(@CurrentUser Account account, Model model) {
        model.addAttribute("email", account.getEmail());
        return "/account/check-email";

     }

    @GetMapping("/resend-confirm-email")
    public String resendConfirmEmail(@CurrentUser Account account, Model model) {
         if (!account.canSendConfirmEmail()) {
             model.addAttribute("error", "인증메일은 1시간에 한번씩 보낼 수 있습니다.");
             model.addAttribute(account);
             return "/account/check-email";
         }

         accountService.sendSignUpConfirmEmail(account);
         return "redirect:/";
     }

     @GetMapping("/profile/{username}")
     public String viewProfile(@PathVariable String username, Model model, @CurrentUser Account account) {
         Account byUsername = accountRepository.findByUsername(username);
         if (username == null) {
             throw new IllegalArgumentException(username +" 에 해당하는 사용자가 없습니다.");
         }

         model.addAttribute(byUsername);
         model.addAttribute("isOwner", byUsername.equals(account));
         return "/settings/profile";

    }






}
