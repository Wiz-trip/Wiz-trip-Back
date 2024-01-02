package com.wiztrip.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wiztrip.config.spring_security.jwt.TokenUtils;
import com.wiztrip.dto.UserDto;
import com.wiztrip.dto.UserRegisterDto;
import com.wiztrip.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final TokenUtils tokenUtils;

    // 회원정보 조회
    @Operation(summary = "회원정보 조회",description = "userId 를 사용하여 회원정보를 조회합니다")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto.UserResponseDto> getUser(@RequestParam @NotNull Long userId) {
        return ResponseEntity.ok().body(userService.getUserById(userId));
    }

    // 회원정보 수정
    @Operation(summary = "회원정보 수정",description = "userId 를 사용하여 회원정보를 수정합니다")
    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto.UserResponseDto> updateUser(@RequestBody UserDto.UserPatchDto userPatchDto) {
        return ResponseEntity.ok().body(userService.updateUser(userPatchDto));
    }


    // 회원 탈퇴
    @Operation(summary = "회원 탈퇴",description = "userId를 사용하여 회원을 탈퇴합니다")
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@RequestParam @NotNull Long userId) {
        return ResponseEntity.ok().body(userService.deleteUser(userId));
    }


    // 회원가입 처리
    @Operation(summary = "회원가입 처리",
            description = """ 
            username
            email
            password
            confirmpassword
            nickname 입력
            """)
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterDto registrationDto) {
        userService.createUser(registrationDto);
        return ResponseEntity.ok("회원가입이 성공적으로 완료");
    }

}