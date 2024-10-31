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
    ;

    private final int status;
    private final String code;
    private final String message;
}
