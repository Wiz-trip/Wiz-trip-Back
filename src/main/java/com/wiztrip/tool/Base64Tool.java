package com.wiztrip.tool;

import lombok.*;
import org.apache.commons.net.util.Base64;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class Base64Tool {

    public static String inputStreamToBase64String(InputStream inputStream) {
        try {
            byte[] bytes = inputStream.readAllBytes();
            return Base64.encodeBase64String(bytes);
        } catch (IOException e) {
            throw new RuntimeException("변환 실패");
        }
    }


    public static Base64Dto base64StringToDto(String filename, String base64String) {
        return Base64Dto.builder()
                .fileName(filename)
                .content(base64String)
                .build();
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Base64Dto {
        private String fileName;
        private String content;
    }
}
