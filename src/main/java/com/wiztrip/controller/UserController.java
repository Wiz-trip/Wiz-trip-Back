package com.wiztrip.controller;

import com.wiztrip.dto.UserDto;
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


    // 회원가입 폼 페이지 반환
    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("userCreateForm", new UserCreateForm());
        return "signup";
    }


    // 회원가입 처리
    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup"; // 유효성 검사 실패 시, 다시 회원가입 양식 페이지로 이동
        }

        userService.create(userCreateForm.getNickname(), userCreateForm.getEmail(), userCreateForm.getPassword());
        return "redirect:/login"; // 회원가입 성공 시, 로그인 페이지로 리디렉션
    }
    // pull 테스트 중입니다

}