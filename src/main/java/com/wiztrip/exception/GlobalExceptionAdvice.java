package com.wiztrip.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    // CustomException
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {

        ErrorCode errorCode = e.getErrorCode();

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                errorCode.getHttpStatus().value(),
                errorCode.getHttpStatus().getReasonPhrase(),
                errorCode.getMessage()
        );

        log.error("Exception: {} time: {} ErrorCode: {} Message: {} Detail: {}",
                e.getClass().getSimpleName(), LocalDateTime.now(), errorCode.getMessage(), e.getMessage());

        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }

    // RuntimeException
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {

        ErrorCode errorCode = ErrorCode.SERVER_ERROR;

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                errorCode.getHttpStatus().value(),
                errorCode.getHttpStatus().getReasonPhrase(),
                errorCode.getMessage()
        );

        log.error("Exception: {} time: {} ErrorCode: {} Message: {} Detail: {}",
                e.getClass().getSimpleName(), LocalDateTime.now(), errorCode.getMessage(), e.getMessage());

        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }

    // NullPointerException: 실제 값이 아닌 null을 가지고 있는 변수 혹은 객체를 호출할 경우
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException e) {

        ErrorCode errorCode = ErrorCode.SERVER_ERROR;

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                errorCode.getHttpStatus().value(),
                errorCode.getHttpStatus().getReasonPhrase(),
                errorCode.getMessage()
        );

        log.error("Exception: {} time: {} ErrorCode: {} Message: {} Detail: {}",
                e.getClass().getSimpleName(), LocalDateTime.now(), errorCode.getMessage(), e.getMessage());

        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }

    // IllegalArgumentException: 사용자가 값을 잘못 입력한 경우
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {

        ErrorCode errorCode = ErrorCode.INVALID_PARAMETER;

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                errorCode.getHttpStatus().value(),
                errorCode.getHttpStatus().getReasonPhrase(),
                errorCode.getMessage()
        );

        log.error("Exception: {} time: {} ErrorCode: {} Message: {} Detail: {}",
                e.getClass().getSimpleName(), LocalDateTime.now(), errorCode.getMessage(), e.getMessage());

        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }

    // MethodArgumentNotValidException: 전달된 값이 유효하지 않은 경우
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        ErrorCode errorCode = ErrorCode.INVALID_PARAMETER;

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                errorCode.getHttpStatus().value(),
                errorCode.getHttpStatus().getReasonPhrase(),
                errorCode.getMessage()
        );

        log.error("Exception: {} time: {} ErrorCode: {} Message: {} Detail: {}",
                e.getClass().getSimpleName(), LocalDateTime.now(), errorCode.getMessage(), e.getMessage());

        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }

    // 추후 자주 발생하는 오류에 대해 추가
}