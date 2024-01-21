package com.wiztrip.service;

import com.wiztrip.domain.UserImageEntity;
import com.wiztrip.dto.UserDto;
import com.wiztrip.domain.UserEntity;
import com.wiztrip.dto.UserRegisterDto;
import com.wiztrip.repository.UserImageRepository;
import com.wiztrip.repository.UserRepository;
import com.wiztrip.tool.file.Base64Dto;
import com.wiztrip.tool.file.Base64Service;
import com.wiztrip.tool.file.FtpTool;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FtpTool ftpTool;


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

        UserDto.UserResponseDto userResponseDto = new UserDto.UserResponseDto();
        userResponseDto.setId(userEntity.getId());
        userResponseDto.setUsername(userEntity.getUsername());
        userResponseDto.setEmail(userEntity.getEmail());
        userResponseDto.setNickname(userEntity.getNickname());

        UserImageEntity imageEntity = userEntity.getImage();
        if(imageEntity != null) {
            String imageUrl = constructImageUrl(imageEntity.getImagePath());
            Base64Dto imageDto = new Base64Dto();
            imageDto.setFileName(imageEntity.getImageName());
            imageDto.setContent(imageUrl);
         //   imageDto.setContent(imageEntity.getImagePath());
            userResponseDto.setImage(imageDto);
        } else {
            userResponseDto.setImage(null);
        }

        return userResponseDto;
    }
    private String constructImageUrl(String imagePath) {
        String baseImageUrl = "https://wiztrip.o-r.kr/";
        return baseImageUrl + imagePath;
    }


    // 닉네임 중복처리
    public boolean isNicknameExist(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    // 회원가입 처리
    @Transactional
    public UserEntity createUser(UserRegisterDto registrationDto) {


        // check PWD
        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // nickname 중복 처리
        if(isNicknameExist(registrationDto.getNickname())) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }

        // email 중복 처리
        userRepository.findByEmail(registrationDto.getEmail())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
                });

        // username 중복 처리
        userRepository.findByUsername(registrationDto.getUsername())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("이미 존재하는 유저네임입니다.");
                });

        UserEntity newUser = new UserEntity();
        newUser.setUsername(registrationDto.getUsername());
        newUser.setEmail(registrationDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        newUser.setNickname(registrationDto.getNickname());

        return userRepository.save(newUser);
    }

}


