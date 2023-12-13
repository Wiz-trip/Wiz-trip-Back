package com.wiztrip.controller;

import com.wiztrip.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/email-verification")
@RequiredArgsConstructor
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @PostMapping
    public ResponseEntity<?> sendCode(@RequestParam("email") String email) {
        emailVerificationService.sendCodeToEmail(email);
        return ResponseEntity.ok().body("발송완료");
    }

    @GetMapping
    public ResponseEntity<?> verifyCode(@RequestParam("email") String email, @RequestParam("code") String code) {
        return ResponseEntity.ok().body(emailVerificationService.verifyCode(email, code));
    }
}