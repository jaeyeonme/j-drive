package io.jaeyeon.jdrive.global.security.jwt;

import io.jaeyeon.jdrive.domain.user.domain.User;
import io.jaeyeon.jdrive.global.error.ErrorCode;
import io.jaeyeon.jdrive.global.error.exception.BusinessException;
import io.jaeyeon.jdrive.global.security.CustomUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token.expire-time}")
    private long accessTokenExpireTime;

    @Value("${jwt.refresh-token.expire-time}")
    private long refreshTokenExpireTime;

    private final CustomUserDetailsService userDetailsService;
    private SecretKey cachedSecretKey;

    @PostConstruct
    protected void init() {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(secretKey);
            this.cachedSecretKey = Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException e) {
            log.error("Error initializing JWT secret key", e);
            throw new IllegalStateException("JWT secret key initialization failed", e);
        }
    }

    public String createAccessToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + accessTokenExpireTime);

        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .issuedAt(now)
                .expiration(expiration)
                .signWith(cachedSecretKey, Jwts.SIG.HS256)
                .compact();
    }

    public String createRefreshToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + refreshTokenExpireTime);

        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .issuedAt(now)
                .expiration(expiration)
                .signWith(cachedSecretKey, Jwts.SIG.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        String userId = getUserId(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserId(String token) {
        return Jwts.parser()
                .verifyWith(cachedSecretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(cachedSecretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException | IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new BusinessException(ErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new BusinessException(ErrorCode.UNSUPPORTED_TOKEN);
        }
    }
}
