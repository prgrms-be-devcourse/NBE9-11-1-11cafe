package com.back._1cafe.global.exception;

import com.back._1cafe.global.exception.customExcetpion.CartItemNotFoundException;
import com.back._1cafe.global.exception.customExcetpion.CartNotFoundException;
import com.back._1cafe.global.exception.customExcetpion.ProductNotFoundException;
import com.back._1cafe.global.rsData.RsData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 잘못된 JSON 형식과 데이터 타입 처리
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<RsData<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        RsData<Void> rsData = RsData.fail("잘못된 데이터 형식입니다.");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // 400 에러 코드로 반환
                .body(rsData);
    }

    // 요청값 검증 실패 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RsData<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("잘못된 요청입니다.");

        RsData<Void> rsData = RsData.fail(message);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(rsData);
    }
    //존재하지 않는 장바구니 요청처리
    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<RsData<Void>> handleCartNotFoundException(CartNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(RsData.fail(e.getMessage()));
    }
    // 상품 없음 예외 처리
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<RsData<Void>> handleProductNotFoundException(ProductNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(RsData.fail(e.getMessage()));
    }

    //존재하지않는 장바구니 아이템
    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<RsData<Void>> handleCartItemNotFoundException(CartItemNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(RsData.fail(e.getMessage()));
    }


    //잘못된 인자 처리 유지
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<RsData<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        RsData<Void> rsData = RsData.fail(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(rsData);
    }
}
