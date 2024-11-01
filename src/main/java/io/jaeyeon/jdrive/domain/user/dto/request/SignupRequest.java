package io.jaeyeon.jdrive.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        @NotBlank(message = "이메일은 필수 입력값입니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
                message = "비밀번호는 8자 이상의 영문자와 숫자 조합이어야 합니다.")
        String password,

        @NotBlank(message = "이름은 필수 입력값입니다.")
        @Size(min = 2, max = 10, message = "이름은 2자 이상 10자 이하여야 합니다.")
        String name
) {}
