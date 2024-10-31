package io.jaeyeon.jdrive.global.error.exception;

import io.jaeyeon.jdrive.global.error.ErrorCode;

public class InvalidValueException extends BusinessException {
    public InvalidValueException(String value) {
        super(ErrorCode.INVALID_INPUT_VALUE);
    }
}
