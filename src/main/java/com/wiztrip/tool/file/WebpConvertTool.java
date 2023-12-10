package com.wiztrip.tool.file;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class WebpConvertTool {

    public byte[] multipartFileToWebpByteArray(MultipartFile multipartFile) {
        try {
            return inputStreamToWebpByteArray(multipartFile.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public InputStream multipartFileToWebpInputStream(MultipartFile multipartFile) {
        try {
            return inputStreamToWebpInputStream(multipartFile.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] inputStreamToWebpByteArray(InputStream inputStream) {
        try {
            BufferedImage image = ImageIO.read(inputStream);
            // webp로 변환
            BufferedImage webpImage = Scalr.apply(image, Scalr.OP_ANTIALIAS); // 크기 변경 없이 그대로 유지

            // WebP 이미지를 ByteArrayOutputStream에 쓰기
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(webpImage, "webp", byteArrayOutputStream);

            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("inputstream을 다시 확인해주세요");
        }
    }

    public InputStream inputStreamToWebpInputStream(InputStream inputStream) {
        byte[] bytes = inputStreamToWebpByteArray(inputStream);
        return new ByteArrayInputStream(bytes);
    }
}
