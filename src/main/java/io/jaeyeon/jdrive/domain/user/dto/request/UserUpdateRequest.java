package io.jaeyeon.jdrive.domain.user.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(

    @Size(min = 2, max = 10, message = "이름은 2자 이상 10자 이하여야 합니다.")
    String name,

    String currentPassword,

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "비밀번호는 8자 이상의 영문자와 숫자 조합이어야 합니다.")
    String newPassword

) {
    public boolean hasPasswordUpdate() {
        return currentPassword != null && newPassword != null;
    }
}
