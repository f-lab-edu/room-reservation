package com.onerty.yeogi.customer.auth;

import com.onerty.yeogi.customer.auth.dto.LoginRequest;
import com.onerty.yeogi.customer.auth.dto.LoginResponse;
import com.onerty.yeogi.customer.auth.dto.TokenRefreshRequest;
import com.onerty.yeogi.customer.auth.dto.TokenRefreshResponse;
import com.onerty.yeogi.customer.exception.ErrorType;
import com.onerty.yeogi.customer.exception.YeogiException;
import com.onerty.yeogi.customer.user.User;
import com.onerty.yeogi.customer.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final StringRedisTemplate redisTemplate;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUserIdentifier(request.email())
                .orElseThrow(() -> new YeogiException(ErrorType.USER_NOT_FOUND));

        if (!user.getUserPassword().equals(request.password())) {
            throw new YeogiException(ErrorType.INVALID_PASSWORD);
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        return new LoginResponse(accessToken, refreshToken, user);
    }

    public TokenRefreshResponse refreshToken(TokenRefreshRequest request) {
        String refreshToken = request.refreshToken();

        if (jwtTokenProvider.isInvalidToken(refreshToken)) {
            throw new YeogiException(ErrorType.INVALID_REFRESH_TOKEN);
        }

        if (Boolean.TRUE.equals(redisTemplate.hasKey(refreshToken))) {
            throw new YeogiException(ErrorType.LOGGED_OUT_TOKEN);
        }

        String userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        User user = userRepository.findByUserIdentifier(userId)
                .orElseThrow(() -> new YeogiException(ErrorType.USER_NOT_FOUND));

        String newAccessToken = jwtTokenProvider.generateAccessToken(user);
        return new TokenRefreshResponse(newAccessToken);
    }

    public void logout(String accessToken) {
        if (jwtTokenProvider.isInvalidToken(accessToken)) {
            throw new YeogiException(ErrorType.INVALID_ACCESS_TOKEN);
        }

        long expiration = jwtTokenProvider.getExpiration(accessToken);
        redisTemplate.opsForValue().set(accessToken, "BLACKLISTED", expiration, TimeUnit.MILLISECONDS);
    }
}
