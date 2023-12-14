package com.wiztrip.tool.file;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("ftptest")
public class FtpTestController {

    @Autowired
    private FtpTool ftpTool;

    @Autowired
    private Base64Tool base64Tool;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void uploadFile(@RequestPart MultipartFile multipartFile)  {
        ftpTool.uploadMultipartFile(multipartFile);
    }

    @GetMapping
    public Base64Dto getFile(@RequestParam String filename) {
        return base64Tool.base64StringToDto(filename, ftpTool.downloadFileAndConvertToBase64String(filename));
    }
}

