package com.back._1cafe.order;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

record OrderRequestBody(
        @NotBlank(message = "guestId는 필수입니다.")
        String guestId,

        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "이메일 형식에 맞게 입력해주세요.")
        String email,

        @NotEmpty(message = "주소는 필수입니다.")
        @Size(max = 200, message = "주소는 200자 이내로 입력해주세요.")
        String address,

        @NotBlank(message = "우편번호는 필수입니다.")
        @Size(max = 5, message = "우편번호는 5자 이내로 입력해주세요.")
        String postcode
) {
}
