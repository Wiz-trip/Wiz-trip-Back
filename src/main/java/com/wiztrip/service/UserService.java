package com.wiztrip.service;

import com.wiztrip.domain.UserImageEntity;
import com.wiztrip.dto.UserDto;
import com.wiztrip.domain.UserEntity;
import com.wiztrip.dto.UserRegisterDto;
import com.wiztrip.repository.UserImageRepository;
import com.wiztrip.repository.UserRepository;
import com.wiztrip.tool.file.Base64Dto;
import com.wiztrip.tool.file.Base64Service;
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
    private final UserImageRepository userImageRepository;
    private final PasswordEncoder passwordEncoder;
    private final Base64Service base64Service;


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


    // 프로필 사진 추가
    @Transactional
    public UserDto.UserResponseDto updateProfilePicture(Long userId, Base64Dto base64Dto) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User id 를 찾을 수 없습니다 : " + userId));

        try {
            // "profile-pictures" -> 파일 저장할 서버 디렉토리 이름 / 경로
            String profileImagePath = base64Service.decodeBase64ToFileAndStore(base64Dto, "profile-path");
            Optional<UserImageEntity> existingImage = userImageRepository.findByUserId(userId);

            UserImageEntity userImage = existingImage.orElse(new UserImageEntity());
            userImage.setImageName(base64Dto.getFileName());
            userImage.setImagePath(profileImagePath);
            userImage.setUser(user);
            user.setImage(userImage);

            userImageRepository.save(userImage);

            // UserResponseDto로 변환하여 반환
            return convertToUserResponseDtoWithImage(user, userImage);
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패 원인 : ", e);
        }
    }

    // 프로필 사진 삭제
    @Transactional
    public String deleteProfilePicture(Long userId) {
        UserImageEntity userImage = userImageRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User id 를 찾을 수 없습니다 : " + userId));

        // 파일 시스템에서 이미지 삭제
        String filePath = userImage.getImagePath();
        if (filePath != null && !filePath.isEmpty()) {
            try {
                Path file = Paths.get(filePath);
                if (Files.exists(file)) {
                    Files.delete(file);
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete file", e);
            }
        }

        // 데이터베이스에서 이미지 정보 업데이트
        userImage.setImagePath(null);
        userImage.setImageName(null);

        UserEntity user = userImage.getUser();
        user.setImage(null); // UserEntity에서도 이미지 정보 제거
        userImageRepository.save(userImage);

        userImageRepository.delete(userImage);

        return "프로필 삭제 성공";
    }

    // 사용자 삭제
    @Transactional
    public String deleteUser(Long userId) {
        userRepository.deleteById(userId);
        return "User deleted successfully";
    }



    // UserEntity를 UserResponseDto로 변환
    private UserDto.UserResponseDto convertToUserResponseDto(UserEntity userEntity) {
        Base64Dto imageDto = null;

        UserImageEntity userImage = userEntity.getImage();
        if (userImage != null && userImage.getImagePath() != null && !userImage.getImagePath().isEmpty()) {
            String base64Content = base64Service.encodeFileToBase64(userImage.getImagePath());
            imageDto = new Base64Dto(userImage.getImageName(), base64Content);
        }

        return UserDto.UserResponseDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .nickname(userEntity.getNickname()) // 회원 닉네임
                .image(imageDto)
                .build();
    }

   //  UserImageEntity -> UserResponseDto로 변환 ( 프로필 사진 구현을 위함 )
    private UserDto.UserResponseDto convertToUserResponseDtoWithImage(UserEntity user, UserImageEntity userImage) {
        Base64Dto imageDto = null;
        if (userImage != null) {
            String base64Content = base64Service.encodeFileToBase64(userImage.getImagePath());
            imageDto = new Base64Dto(userImage.getImageName(),base64Content);
        }

        return UserDto.UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .image(imageDto)
                .nickname(user.getNickname())
                .email(user.getEmail())
                .build();
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


