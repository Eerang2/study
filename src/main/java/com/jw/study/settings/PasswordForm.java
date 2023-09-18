package com.jw.study.settings;


import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class PasswordForm {

    @Length(min = 50)
    private String newPassword;

    @Length(min = 50)
    private String newPasswordConfirm;
}
