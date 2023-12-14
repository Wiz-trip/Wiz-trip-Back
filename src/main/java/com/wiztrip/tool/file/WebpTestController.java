package com.wiztrip.tool.file;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("webp-test")
public class WebpTestController {

    private final WebpConvertTool webpConvertTool;

    private final FtpTool ftpTool;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void convertWebp(@RequestPart MultipartFile multipartFile) {
        InputStream inputStream = webpConvertTool.multipartFileToWebpInputStream(multipartFile);
        ftpTool.uploadInputStream(multipartFile.getOriginalFilename().replace(".jpg",".webp"),inputStream);
    }
}
