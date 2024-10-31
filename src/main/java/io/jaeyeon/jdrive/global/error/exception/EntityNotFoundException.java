package io.jaeyeon.jdrive.global.error.exception;

import io.jaeyeon.jdrive.global.error.ErrorCode;

public class EntityNotFoundException extends BusinessException {
    public EntityNotFoundException(String message) {
        super(ErrorCode.ENTITY_NOT_FOUND);
    }
}
