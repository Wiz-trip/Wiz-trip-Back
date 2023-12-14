package com.wiztrip.controller;

import com.wiztrip.service.EmailVerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "인증번호 발송",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "발송 성공 여부와는 관계 없이 200임.",
                    content = {
                            @Content(mediaType = "*/*",
                                    schema = @Schema(example = "발송완료"))})})
    @PostMapping
    public ResponseEntity<?> sendCode(
            @Schema(description = "인증번호를 보낼 email 주소", example = "example@example.com")
            @RequestParam("email")
            String email) {
        emailVerificationService.sendCodeToEmail(email);
        return ResponseEntity.ok().body("발송완료");
    }

    @Operation(summary = "인증번호 인증",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "인증번호가 올바르면 true, 아니면 false 리턴",
                    content = {
                            @Content(mediaType = "*/*",
                                    schema = @Schema(example = "true"))})})
    @GetMapping
    public ResponseEntity<?> verifyCode(
            @Schema(description = "인증번호를 보낸 email 주소", example = "example@example.com")
            @RequestParam("email")
            String email,
            @Schema(description = "받은 인증번호", example = "123456")
            @RequestParam("code")
            String code) {
        return ResponseEntity.ok().body(emailVerificationService.verifyCode(email, code));
    }
}