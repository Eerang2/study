package com.jw.study.mail;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;


@Service
public class EmailServiceMock implements EmailService {
    @Override
    public void sendEmail(EmailMessage emailMessage) {
        System.out.println("이메일 발송했지롱 ");
    }
}
