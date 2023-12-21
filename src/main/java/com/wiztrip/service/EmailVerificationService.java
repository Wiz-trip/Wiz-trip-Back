package com.wiztrip.service;


import com.wiztrip.exception.CustomException;
import com.wiztrip.exception.ErrorCode;
import com.wiztrip.repository.UserRepository;
import com.wiztrip.tool.email.EmailTool;
import com.wiztrip.tool.redis.RedisTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final UserRepository userRepository;

    private final RedisTool redisTool;

    private final EmailTool emailTool;
    private static final String AUTH_CODE_PREFIX = "AuthCode ";

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    public void sendCodeToEmail(String toEmail) {
        //이메일 중복 검사
        if(userRepository.existsByEmail(toEmail)) throw new CustomException(ErrorCode.DUPLICATE_EMAIL);

        //인증코드 생성, 저장 및 이메일 전송
        String title = "WIZ-TRIP 이메일 인증 번호";
        String authCode = this.createCode();
        // 이메일 인증 요청 시 인증 번호 Redis에 저장
        redisTool.setValues(AUTH_CODE_PREFIX + toEmail,
                authCode, Duration.ofMillis(authCodeExpirationMillis));
        emailTool.sendEmail(toEmail, title, authCode);
    }


    public boolean verifyCode(String email, String authCode) {
        String redisAuthCode = redisTool.getValues(AUTH_CODE_PREFIX + email);

        return redisTool.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode);
    }

    private String createCode() {
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new CustomException(ErrorCode.SERVER_ERROR);
        }
    }
}
