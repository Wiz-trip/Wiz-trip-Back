package com.wiztrip.controller;

import com.wiztrip.dto.UserDto;
import com.wiztrip.dto.UserRegisterDto;
import com.wiztrip.service.UserService;
import com.wiztrip.tool.file.Base64Dto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

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
    @Operation(summary = "프로필 사진 업데이트", description =
            """
                    userId 를 이용하여 프로필 사진을 추가.
                    *  fileName : 테스트할 이미지파일 이름 -> "example.jpg"
                    *  content :   "example.jpg" 를 Base64 로 인코딩한 문자열  -> 예시 ) "/9j4AAQSkZ...."
                                 
                    * 예시 )
                           {
                             "fileName": "test_image.jpg",
                             "content": "iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg=="
                           }
                                        
                    """
                    )
    @PatchMapping("/{userId}/profileImageUpdate")
    public ResponseEntity<UserDto.UserResponseDto> updateProfilePicture
    (@PathVariable Long userId, @RequestBody Base64Dto base64Dto) {
        UserDto.UserResponseDto updatedUser = userService.updateProfilePicture(userId, base64Dto);
        return ResponseEntity.ok().body(updatedUser);
    }

    // 프로필 사진 삭제
    @Operation(summary = "프로필 사진 삭제",description = "userId 를 사용하여 프로필 사진을 삭제")
    @DeleteMapping("/{userId}/profileImageDelete")
    public ResponseEntity<String> deleteProfilePicture(@PathVariable Long userId){
        return ResponseEntity.ok(userService.deleteProfilePicture(userId));
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