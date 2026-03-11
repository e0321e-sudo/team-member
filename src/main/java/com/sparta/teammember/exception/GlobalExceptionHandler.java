package com.sparta.teammember.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice //모든 컨트롤러에서 발생하는 예외를 한곳에서 잡는 어노테이션
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class) // 어떤 예외가 터지든 이 메서드 싱행.
    public ResponseEntity<String> hendleAllExceptions(Exception e) {
        log.error("[API - ERROR] 예외가 발생했습니다: ", e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버 내부 오류가 발생했습니다. 관리자에게 문의하세요.");
    }
}
