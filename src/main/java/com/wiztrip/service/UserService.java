package com.wiztrip.service;

import com.wiztrip.domain.LandmarkEntity;
import com.wiztrip.dto.UserDto;
import com.wiztrip.domain.UserEntity;
import com.wiztrip.dto.UserRegisterDto;
import com.wiztrip.repository.LandmarkRepository;
import com.wiztrip.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final LandmarkRepository landmarkRepository;
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
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userPatchDto.getId()));

        // UserPatchDto의 정보를 UserEntity에 적용
        userEntity.setUsername(userPatchDto.getUsername());
        userEntity.setEmail(userPatchDto.getEmail());
        // 필요에 따라 다른 필드도 업데이트

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



    // 회원가입 처리
    @Transactional
    public UserEntity createUser(UserRegisterDto registrationDto) {
        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        UserEntity newUser = new UserEntity();
        newUser.setUsername(registrationDto.getUsername());
        newUser.setEmail(registrationDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        newUser.setNickname(registrationDto.getNickname());

        return userRepository.save(newUser);
    }


    // 랜드마크 북마크
    @Transactional
    public void bookmarkLandmark(Long userId, Long landmarkId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        LandmarkEntity landmark = landmarkRepository.findById(landmarkId)
                .orElseThrow(() -> new EntityNotFoundException("Landmark 를 찾을 수 없습니다."));

        user.getBookmarkedLandmarks().add(landmark);
        userRepository.save(user);
    }

    @Transactional
    public void removeBookmark(Long userId, Long landmarkId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        LandmarkEntity landmark = landmarkRepository.findById(landmarkId)
                .orElseThrow(() -> new EntityNotFoundException("Landmark 를 찾을 수 없습니다."));

        user.getBookmarkedLandmarks().remove(landmark);
        userRepository.save(user);
    }



}


