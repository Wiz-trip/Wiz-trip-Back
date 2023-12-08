package com.wiztrip.controller;

import com.wiztrip.dto.UserDto;
import com.wiztrip.dto.UserRegisterDto;
import com.wiztrip.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    // 회원정보 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto.UserResponseDto> getUser(@RequestParam @NotNull Long userId) {
        return ResponseEntity.ok().body(userService.getUserById(userId));
    }

    // 회원정보 수정
    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto.UserResponseDto> updateUser(@RequestBody UserDto.UserPatchDto userPatchDto) {
        return ResponseEntity.ok().body(userService.updateUser(userPatchDto));
    }


    // 회원 탈퇴
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@RequestParam @NotNull Long userId) {
        return ResponseEntity.ok().body(userService.deleteUser(userId));
    }


    // 회원가입 처리
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterDto registrationDto) {
        userService.createUser(registrationDto);
        return ResponseEntity.ok("회원가입이 성공적으로 완료");
    }


}