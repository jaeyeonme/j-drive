package io.jaeyeon.jdrive.domain.user.service;

import io.jaeyeon.jdrive.domain.user.domain.User;
import io.jaeyeon.jdrive.domain.user.dto.request.SignupRequest;
import io.jaeyeon.jdrive.domain.user.dto.response.TokenResponse;
import io.jaeyeon.jdrive.domain.user.repository.UserRepository;
import io.jaeyeon.jdrive.global.error.ErrorCode;
import io.jaeyeon.jdrive.global.error.exception.BusinessException;
import io.jaeyeon.jdrive.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse signup(SignupRequest request) {
        validateDuplicateEmail(request.email());
        User savedUser = saveUser(request);
        return createTokens(savedUser);
    }

    @Transactional(readOnly = true)
    public void validateDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATION);
        }
    }

    private User saveUser(SignupRequest request) {
        User user = User.createUser(
                request.email(),
                request.password(),
                request.name(),
                passwordEncoder
        );
        return userRepository.save(user);
    }

    private TokenResponse createTokens(User user) {
        return TokenResponse.of(
                jwtTokenProvider.createAccessToken(user),
                jwtTokenProvider.createRefreshToken(user)
        );
    }
}

