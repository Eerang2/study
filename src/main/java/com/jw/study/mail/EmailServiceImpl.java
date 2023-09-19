package com.jw.study.mail;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Override
    public void sendEmail(EmailMessage emailMessage) {
        System.out.println("내가진짜 이메일 발송 기능이다.");

    }
}
