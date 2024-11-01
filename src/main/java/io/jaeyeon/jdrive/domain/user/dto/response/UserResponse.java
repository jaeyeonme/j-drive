package io.jaeyeon.jdrive.domain.user.dto.response;

import io.jaeyeon.jdrive.domain.user.domain.User;

import java.time.LocalDateTime;

public record UserResponse(
        String email,
        String name,
        Long storageQuota,
        Long usedStorage,
        LocalDateTime createdAt
) {

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getEmail(),
                user.getName(),
                user.getStorageQuota(),
                user.getUsedStorage(),
                user.getCreatedAt()
        );
    }
}
