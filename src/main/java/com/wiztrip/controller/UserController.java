package com.wiztrip.controller;

import com.wiztrip.dto.UserDto;
import com.wiztrip.dto.UserRegisterDto;
import com.wiztrip.service.ImageService;
import com.wiztrip.service.UserService;
import com.wiztrip.tool.file.Base64Dto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final ImageService imageService;

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


    // 프로필 사진 업데이트
    @Operation(summary = "user 프로필 사진 생성",description =
            """
            * userid , image(file) 로 값 전달
            * body -> 
            file : 이미지 파일 이름
            content : Base64 형식으로 인코딩된 실제 이미지 데이터
            """)
    @PostMapping("/{userId}/uploadprofilePicture")
    public ResponseEntity<Base64Dto> uploadprofilePicture(@PathVariable Long userId,
                                                          @RequestParam("image")MultipartFile image) {
        Base64Dto uploadImage = imageService.uploadProfilePicture(userId,image);
        return ResponseEntity.ok(uploadImage);
    }

    @Operation(summary = "프로필 사진 삭제",description = "userId 를 사용하여 프로필 사진을 삭제")
    @DeleteMapping("/{userId}/profileImageDelete")
    public ResponseEntity<?> deleteProfilePicture(@PathVariable Long userId) {
        imageService.deleteProfilePicture(userId);
        return ResponseEntity.ok().build();
    }


    // 닉네임 중복
    @Operation(summary = "닉네임 중복 처리 확인",
            description =
                    """
                    닉네임 중복을 확인 (String 값)
                    - 반환값 true,false
                    * 중복된 닉네임이 없으면 -> true
                    * 중복된 닉네임이 있다면 -> false
                    """)
    @GetMapping("/{nickname}/exist")
    public ResponseEntity<Boolean> isNicknameExist(@RequestParam String nickname) {
        return ResponseEntity.ok(userService.isNicknameExist(nickname));
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
            <회원가입>
            * username
            * email
            * password
            * confirmpassword
            * nickname 입력
            """)
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterDto registrationDto) {
        userService.createUser(registrationDto);
        return ResponseEntity.ok("회원가입이 성공적으로 완료");
    }


}