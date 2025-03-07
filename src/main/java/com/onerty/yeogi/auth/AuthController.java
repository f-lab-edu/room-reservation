package com.onerty.yeogi.auth;

import com.onerty.yeogi.auth.dto.LoginRequest;
import com.onerty.yeogi.auth.dto.LoginResponse;
import com.onerty.yeogi.auth.dto.TokenRefreshRequest;
import com.onerty.yeogi.auth.dto.TokenRefreshResponse;
import com.onerty.yeogi.util.BaseResponse;
import com.onerty.yeogi.util.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.onerty.yeogi.auth.JwtUtil.removeBearerPrefix;

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
