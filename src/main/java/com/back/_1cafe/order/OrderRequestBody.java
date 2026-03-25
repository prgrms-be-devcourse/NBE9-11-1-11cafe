package com.back._1cafe.order;

import jakarta.validation.constraints.*;

/*
원래 GusetId를 Body로 받던것을 Head로 받도록 수정, 바디에 있던 내용 삭제
 */
record OrderRequestBody(
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "이메일 형식에 맞게 입력해주세요.")
        String email,

        @NotEmpty(message = "주소는 필수입니다.")
        @Size(max = 200, message = "주소는 200자 이내로 입력해주세요.")
        String address,

        @NotBlank(message = "우편번호는 필수입니다.")
        @Pattern(regexp = "^\\d{5}$", message = "우편번호는 정확히 5자리 숫자여야 합니다.")
        String postcode
) {
}
