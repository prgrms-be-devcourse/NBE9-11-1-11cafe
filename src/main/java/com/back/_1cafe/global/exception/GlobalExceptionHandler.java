package com.back._1cafe.global.exception;

import com.back._1cafe.global.rsData.RsData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<RsData<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        RsData<Void> rsData = RsData.fail("잘못된 데이터 형식입니다.");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // 400 에러 코드로 반환
                .body(rsData);
    }
}