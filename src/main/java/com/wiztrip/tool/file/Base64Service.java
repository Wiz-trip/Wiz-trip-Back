package com.wiztrip.tool.file;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class Base64Service {
    public String decodeBase64ToFileAndStore(Base64Dto base64Dto, String directory) throws IOException {
        byte[] fileData = Base64.getDecoder().decode(base64Dto.getContent());
        Path destinationFile = Paths.get(directory, base64Dto.getFileName());

        Files.createDirectories(destinationFile.getParent());
        Files.write(destinationFile, fileData);

        return destinationFile.toString();
    }

    public String encodeFileToBase64(String filePath) {
        try {
            byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }
    }

}
