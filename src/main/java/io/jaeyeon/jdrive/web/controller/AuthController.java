package io.jaeyeon.jdrive.web.controller;

import io.jaeyeon.jdrive.domain.user.dto.request.LoginRequest;
import io.jaeyeon.jdrive.domain.user.dto.request.SignupRequest;
import io.jaeyeon.jdrive.domain.user.dto.response.TokenResponse;
import io.jaeyeon.jdrive.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<TokenResponse> signup(@Valid @RequestBody SignupRequest request) {
        return ResponseEntity.ok(userService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }
}
