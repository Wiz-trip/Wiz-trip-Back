package com.wiztrip.config.spring_security.jwt;


import com.auth0.jwt.exceptions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class JwtExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        /**
         *      * AlgorithmMismatchException – if the algorithm stated in the token's header is not equal to the one defined in the JWTVerifier.
         *      * SignatureVerificationException – if the signature is invalid.
         *      * TokenExpiredException – if the token has expired.
         *      * MissingClaimException – if a claim to be verified is missing.
         *      * IncorrectClaimException - if a claim contained a different value than the expected one.
         */

        try {
            filterChain.doFilter(request, response);
        } catch (TokenExpiredException e) { //토큰 만료
            setErrorResponse(response, ErrorCode.TOKEN_EXPIRED);
        } catch (AlgorithmMismatchException | SignatureVerificationException | MissingClaimException |
                 IncorrectClaimException e) {
            setErrorResponse(response, ErrorCode.INVALID_TOKEN);
        }
    }

    private void setErrorResponse(
            HttpServletResponse response,
            ErrorCode errorCode
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now().toString(), errorCode.getValue(),
                errorCode.getHttpStatus().getReasonPhrase(), errorCode.getMessage());
        try {
            response.getWriter().write(objectMapper.registerModule(new JavaTimeModule()).writeValueAsString(errorResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Data
    public static class ErrorResponse {
        private final String timestamp;
        private final Integer status;
        private final String error;
        private final String message;
    }

    @AllArgsConstructor
    @Getter
    private enum ErrorCode {
        TOKEN_EXPIRED(BAD_REQUEST, 400, "JWT Token Expired"),
        INVALID_TOKEN(BAD_REQUEST, 400, "JWT Token Invalid");

        private final HttpStatus httpStatus;
        private final int value;
        private final String message;
    }

}
