package com.back._1cafe.global.rsData;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RsData<T> {
    private final boolean success; // 성공 여부
    private final String message;  // 응답 메시지
    private final T data;          // 실제 응답 데이터 (내용물이 없을 땐 null)

    // 성공 응답을 만드는 정적 메서드
    public static <T> RsData<T> of(String message, T data) {
        return new RsData<>(true, message, data);
    }
    // 실패 응답을 쉽게 만드는 정적메서드
    public static <T> RsData<T> fail(String message) {
        return new RsData<>(false, message, null);
    }
}