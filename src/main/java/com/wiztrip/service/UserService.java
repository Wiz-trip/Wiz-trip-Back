package com.wiztrip.service;

import com.wiztrip.Dto.UserDto;
import com.wiztrip.domain.UserEntity;
import com.wiztrip.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // ID로 사용자 조회
    public UserDto getUserById(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자 ID를 찾을 수 없습니다: " + userId));   // 사용자 찾을 수 없을시 예외발생
        return convertToDto(userEntity);
    }

    // 사용자 정보 업데이트
    public UserDto updateUser(Long userId, UserDto userDto) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자 ID를 찾을 수 없습니다: " + userId));

        // 업데이트할 필드 설정
        userEntity.setUsername(userDto.getUsername());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setImage(userDto.getImage()); // image 필드가 엔티티에 있는 경우
        userEntity.setNickname(userDto.getNickname());

        // 업데이트된 엔티티 저장
        UserEntity updatedUser = userRepository.save(userEntity);
        return convertToDto(updatedUser);
    }

    // 사용자 삭제
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    // UserEntity를 UserDto로 변환
    private UserDto convertToDto(UserEntity userEntity) {
        UserDto userDto = new UserDto();
        userDto.setId(userEntity.getId());
        userDto.setUsername(userEntity.getUsername());
        userDto.setEmail(userEntity.getEmail());
        userDto.setImage(userEntity.getImage()); // image 필드가 DTO에 있는 경우
        userDto.setNickname(userEntity.getNickname());
        return userDto;
    }
}
