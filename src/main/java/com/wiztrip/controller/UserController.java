package com.wiztrip.controller;

import com.wiztrip.dto.UserDto;
import com.wiztrip.dto.UserRegisterDto;
import com.wiztrip.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    // 회원정보 조회
    @Operation(summary = "회원정보 조회",description = "회원정보를 조회합니다.")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto.UserResponseDto> getUser(@RequestParam @NotNull Long userId) {
        return ResponseEntity.ok().body(userService.getUserById(userId));
    }

    // 회원정보 수정
    @Operation(summary = "회원정보 수정",description = "회원정보를 수정합니다.")
    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto.UserResponseDto> updateUser(@RequestBody UserDto.UserPatchDto userPatchDto) {
        return ResponseEntity.ok().body(userService.updateUser(userPatchDto));
    }


    // 회원 탈퇴
    @Operation(summary = "회원정보 삭제",description = "회원정보를 삭제합니다.")
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@RequestParam @NotNull Long userId) {
        return ResponseEntity.ok().body(userService.deleteUser(userId));
    }


    // 회원가입 처리
    @Operation(summary = "회원 가입",description = "회원가입을 처리합니다.")
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterDto registrationDto) {
        userService.createUser(registrationDto);
        return ResponseEntity.ok("회원가입이 성공적으로 완료");
    }

    // 카카오 로그인
    @Operation(summary = "카카오 로그인",description = "카카오 로그인을 처리합니다.")
    @GetMapping("login/kakao")
    public ResponseEntity<?> kakaoLogin(@AuthenticationPrincipal OAuth2User oauth2User) {
        if (oauth2User == null) {
            // 사용자가 인증되지 않은 경우의 처리
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        // Kakao 사용자 정보 추출
        Map<String, Object> kakaoAttributes = oauth2User.getAttributes();
        // 필요한 사용자 정보를 사용하여 등록 또는 업데이트 처리
        UserDto.UserResponseDto userDto = userService.registerOrUpdateUser(kakaoAttributes);
        // 처리 결과 반환
        return ResponseEntity.ok().body(userDto);
    }


}