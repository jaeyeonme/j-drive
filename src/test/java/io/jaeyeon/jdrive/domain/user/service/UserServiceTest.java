package io.jaeyeon.jdrive.domain.user.service;

import static org.mockito.ArgumentMatchers.any;

import io.jaeyeon.jdrive.domain.user.domain.User;
import io.jaeyeon.jdrive.domain.user.dto.request.LoginRequest;
import io.jaeyeon.jdrive.domain.user.dto.request.SignupRequest;
import io.jaeyeon.jdrive.domain.user.dto.request.UserUpdateRequest;
import io.jaeyeon.jdrive.domain.user.dto.response.TokenResponse;
import io.jaeyeon.jdrive.domain.user.dto.response.UserResponse;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
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

    @Test
    @DisplayName("로그인 성공")
    void login_success() {
        // given
        LoginRequest request = new LoginRequest("test@example.com", "password123");
        User user = User.createUser(
                request.email(),
                request.password(),
                "TestUser",
                passwordEncoder
        );

        given(userRepository.findByEmail(request.email())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.password(), user.getPassword())).willReturn(true);
        given(jwtTokenProvider.createAccessToken(user)).willReturn("accessToken");
        given(jwtTokenProvider.createRefreshToken(user)).willReturn("refreshToken");

        // when
        TokenResponse response = userService.login(request);

        // then
        assertThat(response.accessToken()).isEqualTo("accessToken");
        assertThat(response.refreshToken()).isEqualTo("refreshToken");
        verify(userRepository).findByEmail(request.email());
        verify(passwordEncoder).matches(request.password(), user.getPassword());
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 로그인 시도")
    void login_userNotFound() {
        // given
        LoginRequest request = new LoginRequest("test@example.com", "password123");
        given(userRepository.findByEmail(request.email())).willReturn(Optional.empty());

        // when & then
        assertThrows(BusinessException.class, () -> userService.login(request));
        verify(userRepository).findByEmail(request.email());
    }

    @Test
    @DisplayName("내 정보 조회 성공")
    void getMyInfo_success() {
        // given
        Long userId = 1L;
        User user = User.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .name("TestUser")
                .build();

        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));

        // when
        UserResponse response = userService.getMyInfo(userId);

        // then
        assertThat(response.email()).isEqualTo(user.getEmail());
        assertThat(response.name()).isEqualTo(user.getName());
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("회원 정보 수정 성공")
    void updateMyInfo_success() {
        // given
        String currentPassword = "password123";
        String encodedPassword = "encodedPassword";
        String newPassword = "newPassword123";
        String newEncodedPassword = "newEncodedPassword";

        Long userId = 1L;
        User user = User.builder()
                .email("test@example.com")
                .password(encodedPassword)
                .name("TestUser")
                .build();

        UserUpdateRequest request = new UserUpdateRequest(
                "NewName",
                currentPassword,
                newPassword
        );

        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));

        given(passwordEncoder.matches(currentPassword, encodedPassword))
                .willReturn(true);

        given(passwordEncoder.encode(newPassword))
                .willReturn(newEncodedPassword);

        // when
        UserResponse response = userService.updateMyInfo(userId, request);

        // then
        assertThat(response.name()).isEqualTo(request.name());
        verify(userRepository).findById(userId);
        verify(passwordEncoder).matches(currentPassword, encodedPassword);
        verify(passwordEncoder).encode(newPassword);
    }
}
