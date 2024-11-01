package io.jaeyeon.jdrive.domain.user.service;

import static org.mockito.ArgumentMatchers.any;

import io.jaeyeon.jdrive.domain.user.domain.User;
import io.jaeyeon.jdrive.domain.user.dto.request.SignupRequest;
import io.jaeyeon.jdrive.domain.user.dto.response.TokenResponse;
import io.jaeyeon.jdrive.domain.user.repository.UserRepository;
import io.jaeyeon.jdrive.global.error.exception.BusinessException;
import io.jaeyeon.jdrive.global.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원 가입 테스트 - 성공")
    void signup_success() {
        // given
        SignupRequest request = new SignupRequest("test@example.com", "password123", "TestUser");
        given(passwordEncoder.encode(any())).willReturn("encodedPassword");
        given(userRepository.existsByEmail(request.email())).willReturn(false);
        given(userRepository.save(any(User.class))).willAnswer(invocation -> invocation.getArgument(0));
        given(jwtTokenProvider.createAccessToken(any(User.class))).willReturn("accessToken");
        given(jwtTokenProvider.createRefreshToken(any(User.class))).willReturn("refreshToken");

        // when
        TokenResponse response = userService.signup(request);

        // then
        assertThat(response.accessToken()).isEqualTo("accessToken");
        assertThat(response.refreshToken()).isEqualTo("refreshToken");
        verify(userRepository).existsByEmail(request.email());
        verify(passwordEncoder).encode(request.password());
    }

    @Test
    @DisplayName("중복 이메일로 회원 가입 실패")
    void signup_duplicateEmail_failure() {
        // Given
        SignupRequest signupRequest = new SignupRequest("test@example.com", "password123", "TestUser");

        // Mock user repository - email already exists
        given(userRepository.existsByEmail(signupRequest.email())).willReturn(true);

        // When & Then
        assertThrows(BusinessException.class, () -> userService.signup(signupRequest));
    }
  
}
