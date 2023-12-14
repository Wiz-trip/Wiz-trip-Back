package com.wiztrip.tool.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class EmailTool {

    private final JavaMailSender javaMailSender;

    //무분별한 스레드 생성을 막기 위한 스레드 풀
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public void sendEmail(String email, String title, String text) {
        SimpleMailMessage emailForm = createEmailForm(email, title, text);
        executorService.submit(() -> {
            try {
                javaMailSender.send(emailForm);
                log.info("이메일");
            } catch (Exception e) {
                log.error("이메일 발송 오류");
            }
        });
    }

    // 발신할 이메일 데이터 세팅
    private SimpleMailMessage createEmailForm(String toEmail, String title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);

        return message;
    }
}
