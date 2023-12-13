package com.wiztrip.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // 400 BAD_REQUEST: 잘못된 요청
    INVALID_PARAMETER(BAD_REQUEST, "입력 값을 확인해주세요."),

    // 401 UNAUTHORIZED: 인증되지 않은 사용자
    UNAUTHORIZED_MEMBER(UNAUTHORIZED, "존재하지 않는 회원입니다."),

    // 404 NOT_FOUND: 잘못된 리소스 접근
    MEMBER_NOT_FOUND(NOT_FOUND, "해당 회원 정보를 찾을 수 없습니다."),
    LANDMARK_NOT_EXIST(NOT_FOUND, "해당 여행지 정보를 찾을 수 없습니다."),

    // 409 CONFLICT: 중복된 리소스
    DUPLICATE_EMAIL(CONFLICT, "해당 이메일은 이미 존재합니다."),

    // 500 INTERNAL SERVER ERROR
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "내부 서버 에러입니다.");


    private final HttpStatus httpStatus;
    private final String message;

}