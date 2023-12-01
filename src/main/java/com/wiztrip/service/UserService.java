package com.wiztrip.service;

import com.wiztrip.dto.UserDto;
import com.wiztrip.domain.UserEntity;
import com.wiztrip.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

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

}
