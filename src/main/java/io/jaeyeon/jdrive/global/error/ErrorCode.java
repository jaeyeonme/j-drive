package io.jaeyeon.jdrive.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(400, "C001", "잘못된 입력값입니다."),
    ENTITY_NOT_FOUND(400, "C002", "엔티티를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(500, "C003", "서버 오류가 발생했습니다."),

    // Member
    MEMBER_NOT_FOUND(404, "M001", "회원을 찾을 수 없습니다."),
    EMAIL_DUPLICATION(400, "M002", "이미 사용중인 이메일입니다."),
    INVALID_PASSWORD(400, "M003", "잘못된 비밀번호입니다."),

    // JWT
    INVALID_TOKEN(401, "J001", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(401, "J002", "만료된 토큰입니다."),
    UNSUPPORTED_TOKEN(401, "J003", "지원되지 않는 토큰입니다."),
    ;

    private final int status;
    private final String code;
    private final String message;
}
