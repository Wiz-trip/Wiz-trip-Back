package com.wiztrip.tool;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Component
public class FtpTool {

    @Value("${ftp.server}")
    private String sever;

    @Value("${ftp.port}")
    private int port;

    @Value("${ftp.username}")
    private String username;

    @Value("${ftp.password}")
    private String password;

    @Value("${ftp.directory}")
    private String directory;

    private final FTPClient ftpClient = new FTPClient();

    @Autowired
    private Base64Tool base64Tool;

    /**
     * 파일 업로드
     */
    public void uploadMultipartFile(MultipartFile multipartFile) {
        connect();
        try (InputStream inputStream = multipartFile.getInputStream()) {
            if (!ftpClient.storeFile(multipartFile.getOriginalFilename(), inputStream))
                throw new Exception("FTP서버 업로드실패");
        } catch (Exception e) {
            if (e.getMessage().contains("not open")) {
                throw new RuntimeException("FTP서버 연결실패");
            }
        }
        disconnect();
    }

    /**
     * 여러개의 파일 업로드
     */
    public void uploadMultipartFileList(List<MultipartFile> multipartFileList) {
        connect();
        multipartFileList.forEach(o -> {
            try (InputStream inputStream = o.getInputStream()) {
                if (!ftpClient.storeFile(o.getOriginalFilename(), inputStream))
                    throw new RuntimeException("FTP서버 업로드 실패");
            } catch (IOException e) {
                throw new RuntimeException("파일 오류");
            }
        });
        disconnect();
    }

    public void uploadInputStream(String filename, InputStream inputStream) {
        connect();
        try {
            if (!ftpClient.storeFile(filename, inputStream))
                throw new Exception("FTP서버 업로드실패");
        } catch (Exception e) {
            if (e.getMessage().contains("not open")) {
                throw new RuntimeException("FTP서버 연결실패");
            }
        }
        disconnect();
    }

    public void uploadInputStreamList(Map<String, InputStream> filenameInputStreamMap) {
        connect();
        filenameInputStreamMap.keySet().forEach(o -> {
            try (InputStream inputStream = filenameInputStreamMap.get(o)) {
                if (!ftpClient.storeFile(o, inputStream))
                    throw new RuntimeException("FTP서버 업로드 실패");
            } catch (IOException e) {
                throw new RuntimeException("파일 오류");
            }
        });
        disconnect();
    }


    /**
     * 파일 다운로드
     *
     * @param filename -> 다운로드가 필요한 파일의 이름.
     * @return -> byte[]
     */
    public byte[] downloadFile(String filename) {
        connect();
        try (InputStream inputStream = ftpClient.retrieveFileStream(filename)) {
            byte[] bytes = inputStream.readAllBytes();
            disconnect();
            return bytes;
        } catch (Exception e) {
            throw new RuntimeException("다운로드 실패");
        }
    }

    /**
     * 파일 다운로드
     *
     * @param filename -> 다운로드가 필요한 파일의 이름.
     * @return -> Base64String
     */
    public String downloadFileAndConvertToBase64String(String filename) {
        byte[] bytes = downloadFile(filename);
        return base64Tool.byteArrayToBase64String(bytes);
    }

    /**
     * 파일 다운로드
     *
     * @param filename -> 다운로드가 필요한 파일의 이름.
     * @return -> Base64Dto
     */
    public Base64Dto downloadFileAndConvertToBase64Dto(String filename) {
        String base64String = downloadFileAndConvertToBase64String(filename);
        return new Base64Dto(filename, base64String);
    }


    // FTP 연결 및 설정
    private void connect() {
        try {
            ftpClient.setControlEncoding("euc-kr");    //FTP 인코딩 설정 //안하면 한글 깨짐

            ftpClient.connect(sever, port);            //FTP 연결

            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {    //응답코드가 정상적이지 않은 경우 연결 해제
                ftpClient.disconnect();
                throw new RuntimeException("FTP서버 연결실패");
            }

            ftpClient.setSoTimeout(1000 * 5);        //Timeout 설정
            if (!ftpClient.login(username, password)) { //로그인이 실패하는 경우
                ftpClient.logout();
                throw new RuntimeException("FTP서버 로그인실패");
            }

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);    //파일타입설정
            ftpClient.enterLocalPassiveMode();            //Active 모드 설정

            if (!ftpClient.changeWorkingDirectory(directory)) {    // result = False 는 저장파일경로가 존재하지 않음
                ftpClient.makeDirectory(directory);    //저장파일경로 생성
                ftpClient.changeWorkingDirectory(directory);
            }
        } catch (Exception e) {
            if (e.getMessage().contains("refused"))
                throw new RuntimeException("FTP서버 연결실패");
        }
    }

    // FTP 연결해제
    private void disconnect() {
        try {
            if (ftpClient.isConnected()) {
                ftpClient.disconnect();
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}