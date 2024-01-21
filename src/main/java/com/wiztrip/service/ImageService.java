package com.wiztrip.service;


import com.wiztrip.domain.UserEntity;
import com.wiztrip.domain.UserImageEntity;
import com.wiztrip.exception.CustomException;
import com.wiztrip.exception.ErrorCode;
import com.wiztrip.repository.UserImageRepository;
import com.wiztrip.repository.UserRepository;
import com.wiztrip.tool.file.Base64Dto;
import com.wiztrip.tool.file.FtpTool;
import com.wiztrip.tool.file.WebpConvertTool;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final FtpTool ftpTool;

    private final WebpConvertTool webpConvertTool;

    private final UserRepository userRepository;

    private final UserImageRepository userImageRepository;

    @Value("${ftp.directory}")
    private String directory;


    @Transactional
    public Base64Dto uploadProfilePicture(Long userId, MultipartFile originalImage) {

        try {
            String webpFileName = generateRandomFileName();

            // MultipartFile을 WebP 형식의 InputStream으로 변환
            InputStream webpInputStream = webpConvertTool.multipartFileToWebpInputStream(originalImage);

            // FTP 서버로 webp 이미지 업로드
            ftpTool.uploadInputStream(webpFileName, webpInputStream);


            UserEntity user = userRepository.findById(userId)
                    .orElseThrow( () -> new CustomException(ErrorCode.USER_NOT_FOUND));

            UserImageEntity imageEntity = user.getImage();
            if(imageEntity == null) {
                imageEntity = new UserImageEntity();
                imageEntity.setUser(user);
            }

            String imagePath = directory + "/" + webpFileName;
            imageEntity.setImageName(webpFileName);
            imageEntity.setImagePath(imagePath);
            imageEntity.setEmail(user.getEmail());
            imageEntity.setNickname(user.getNickname());

            userImageRepository.save(imageEntity);
            userRepository.save(user);


            // 업로드된 파일 다운 & Base64Dto 로 변환
            return ftpTool.downloadFileAndConvertToBase64Dto(webpFileName);
        } catch (CustomException e) {
            throw new CustomException(ErrorCode.WRONG_MULTIPARTFILE);
        }
    }

    private String generateRandomFileName() {
        // 고유한 이름 부여
        return "profile_image_" + UUID.randomUUID()+ ".webp";
    }


    public void deleteProfilePicture(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow( () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        UserImageEntity imageEntity = user.getImage();

        if(imageEntity != null) {
            if (imageEntity.getImageName() != null) {
                ftpTool.deleteFile(imageEntity.getImageName());
            }
            user.setImage(null);
            userImageRepository.delete(imageEntity);
            userRepository.save(user);
        } else {
            throw new CustomException(ErrorCode.PROFILE_IMAGE_NOT_FOUND);
        }
    }


    @Transactional
    public Base64Dto editProfilePicture(Long userId, MultipartFile newImage) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 기존 이미지 삭제
        UserImageEntity existingImage = user.getImage();
        if (existingImage != null) {
            ftpTool.deleteFile(existingImage.getImageName());
        }

        // 새 이미지 업로드
        return uploadProfilePicture(userId, newImage);
    }




}
