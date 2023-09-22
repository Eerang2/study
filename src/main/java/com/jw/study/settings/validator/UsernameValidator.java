package com.jw.study.settings.validator;


import com.jw.study.account.Account;
import com.jw.study.account.AccountRepository;
import com.jw.study.settings.form.UsernameForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class UsernameValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernameForm.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UsernameForm usernameForm = new UsernameForm();
        Account byUsername = accountRepository.findByUsername(usernameForm.getUsername());
        if (byUsername != null) {
            errors.rejectValue("username", "wrong.value", "입력하신 이름은 사용하실 수 없습니다.");
        }
    }
}
