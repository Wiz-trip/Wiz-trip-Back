package com.wiztrip.tool.file;

import org.apache.commons.net.util.Base64;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class Base64Tool {

    public String inputStreamToBase64String(InputStream inputStream) {
        try {
            byte[] bytes = inputStream.readAllBytes();
            return Base64.encodeBase64String(bytes);
        } catch (IOException e) {
            throw new RuntimeException("변환 실패");
        }
    }

    public String byteArrayToBase64String(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }


    public Base64Dto base64StringToDto(String filename, String base64String) {
        return Base64Dto.builder()
                .fileName(filename)
                .content(base64String)
                .build();
    }

}
