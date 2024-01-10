package com.wiztrip.service;

import com.wiztrip.dto.UserDto;
import com.wiztrip.domain.UserEntity;
import com.wiztrip.dto.UserRegisterDto;
import com.wiztrip.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // 사용자 조회
    public UserDto.UserResponseDto getUserById(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        return convertToUserResponseDto(userEntity);
    }

    // 사용자 정보 수정
    @Transactional
    public UserDto.UserResponseDto updateUser(UserDto.UserPatchDto userPatchDto) {
        UserEntity userEntity = userRepository.findById(userPatchDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("회원 ID 를 찾을 수 없습니다 : " + userPatchDto.getId()));

        // UserPatchDto의 정보를 UserEntity에 적용
        userEntity.setUsername(userPatchDto.getUsername());
        userEntity.setEmail(userPatchDto.getEmail());
        userEntity.setNickname(userPatchDto.getNickname());

        return convertToUserResponseDto(userRepository.save(userEntity));
    }


    // 사용자 삭제
    @Transactional
    public String deleteUser(Long userId) {
        userRepository.deleteById(userId);
        return "User deleted successfully";
    }

    // UserEntity를 UserResponseDto로 변환
    private UserDto.UserResponseDto convertToUserResponseDto(UserEntity userEntity) {
        return UserDto.UserResponseDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .image(userEntity.getImage()) // 프로필 사진
                .nickname(userEntity.getNickname()) // 회원 닉네임
                .build();
    }


    // 닉네임 중복처리
    public boolean isNicknameExist(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    // 회원가입 처리
    @Transactional
    public UserEntity createUser(UserRegisterDto registrationDto) {

        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 닉네임 중복 처리
        if(isNicknameExist(registrationDto.getNickname())) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }

        UserEntity newUser = new UserEntity();
        newUser.setUsername(registrationDto.getUsername());
        newUser.setEmail(registrationDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        newUser.setNickname(registrationDto.getNickname());

        return userRepository.save(newUser);
    }

}


