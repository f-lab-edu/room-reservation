package com.onerty.yeogi.customer.auth;

import com.onerty.yeogi.common.util.BaseResponse;
import com.onerty.yeogi.common.util.MessageResponse;
import com.onerty.yeogi.customer.auth.dto.LoginRequest;
import com.onerty.yeogi.customer.auth.dto.LoginResponse;
import com.onerty.yeogi.customer.auth.dto.TokenRefreshRequest;
import com.onerty.yeogi.customer.auth.dto.TokenRefreshResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.onerty.yeogi.common.security.JwtUtil.removeBearerPrefix;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/v1/auth/token")
    public BaseResponse<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        return new BaseResponse.success<>(loginResponse);
    }

    @PatchMapping("/v1/auth/token")
    public BaseResponse<TokenRefreshResponse> refreshAccessToken(@RequestBody TokenRefreshRequest tokenRefreshRequest) {
        TokenRefreshResponse tokenRefreshResponse = authService.refreshToken(tokenRefreshRequest);
        return new BaseResponse.success<>(tokenRefreshResponse);
    }

    @DeleteMapping("/v1/auth/token")
    public BaseResponse<MessageResponse> logout(@RequestHeader("Authorization") String bearerToken) {
        String token = removeBearerPrefix(bearerToken);
        authService.logout(token);
        return new BaseResponse.success<>(new MessageResponse("로그아웃에 성공했습니다"));
    }
}
