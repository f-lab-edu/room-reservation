package com.onerty.yeogi.customer.user;


import com.onerty.yeogi.customer.term.dto.TermResponse;
import com.onerty.yeogi.customer.user.dto.NicknameResponse;
import com.onerty.yeogi.customer.user.dto.UserSignupRequest;
import com.onerty.yeogi.customer.user.dto.UserSignupResponse;
import com.onerty.yeogi.customer.user.dto.VerifyCertificationRequest;
import com.onerty.yeogi.customer.util.BaseResponse;
import com.onerty.yeogi.customer.util.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public BaseResponse<UserSignupResponse> signup(@RequestBody UserSignupRequest signupDto) {
        signupDto.check();
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
    public BaseResponse<MessageResponse> verifyCertification(@RequestBody VerifyCertificationRequest request) {
        userService.verifyCertification(request);
        return new BaseResponse.success<>(new MessageResponse("인증되었습니다"));
    }
}
