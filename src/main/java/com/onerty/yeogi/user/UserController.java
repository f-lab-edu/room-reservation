package com.onerty.yeogi.user;


import com.onerty.yeogi.term.dto.TermResponse;
import com.onerty.yeogi.user.dto.NicknameResponse;
import com.onerty.yeogi.user.dto.UserSignupRequest;
import com.onerty.yeogi.user.dto.UserSignupResponse;
import com.onerty.yeogi.util.BaseResponse;
import com.onerty.yeogi.util.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public BaseResponse<UserSignupResponse> signup(@RequestBody UserSignupRequest signupDto) {
        UserSignupResponse response = userService.registerUser(signupDto);
        return new BaseResponse.success<>(response);
    }

    @GetMapping("/v1/terms/agree")
    public BaseResponse<TermResponse> getTerms() {
        return new BaseResponse.success<>(userService.getTerms());
    }

    @GetMapping("/v1/nicknames-recommendation")
    public BaseResponse<NicknameResponse> getNicknames() {
        return new BaseResponse.success<>(userService.generateRandomNicknames());
    }

    @GetMapping("/v1/phone/certification")
    public BaseResponse<MessageResponse> sendCertification(@RequestParam String phoneNumber) {
        String responseMessage = userService.sendCertification(phoneNumber);
        return new BaseResponse.success<>(new MessageResponse(responseMessage));
    }

    @PostMapping("/v1/phone/certification")
    public BaseResponse<MessageResponse> verifyCertification(@RequestBody Map<String, String> payload) {
        userService.verifyCertification(payload);
        return new BaseResponse.success<>(new MessageResponse("인증되었습니다"));
    }
}
