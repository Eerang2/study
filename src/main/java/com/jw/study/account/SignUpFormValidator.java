package com.jw.study.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {


    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?>aClass) {
        return aClass.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        SignUpForm signUpForm = (SignUpForm)object;
        if (accountRepository.existsByEmail(signUpForm.getEmail())) {
            errors.rejectValue("email", "invaild.email", new Object[]{signUpForm.getEmail()}, "이미 사용중인 이메일입니다");
        }
        if (accountRepository.existsByUsername(signUpForm.getUsername())) {
            errors.rejectValue("username", "invaild.username", new Object[]{signUpForm.getUsername()},  "이미 사용중인 이름입니다");
        }
    }
}
