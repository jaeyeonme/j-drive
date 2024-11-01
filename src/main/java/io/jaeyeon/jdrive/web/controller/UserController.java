package io.jaeyeon.jdrive.web.controller;

import io.jaeyeon.jdrive.domain.user.dto.request.UserUpdateRequest;
import io.jaeyeon.jdrive.domain.user.dto.response.UserResponse;
import io.jaeyeon.jdrive.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        UserResponse response = userService.getMyInfo(Long.parseLong(userDetails.getUsername()));

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/me")
    public ResponseEntity<UserResponse> updateMyInfo(@AuthenticationPrincipal UserDetails userDetails,
                                                     @Valid @RequestBody UserUpdateRequest request) {
        UserResponse response = userService.updateMyInfo(Long.parseLong(userDetails.getUsername()), request);

        return ResponseEntity.ok(response);
    }
}
