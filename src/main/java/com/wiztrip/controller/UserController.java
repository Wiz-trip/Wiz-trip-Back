package com.wiztrip.controller;

import com.wiztrip.Dto.UserDto;
import com.wiztrip.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // 회원정보 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long userId) {
        UserDto userDto = userService.getUserById(userId);
        return ResponseEntity.ok(userDto);
    }

    // 회원정보 수정
    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(userId, userDto);
        return ResponseEntity.ok(updatedUser);
    }

    // 회원 탈퇴
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}