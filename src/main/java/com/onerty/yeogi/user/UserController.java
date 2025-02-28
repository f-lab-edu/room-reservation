package com.onerty.yeogi.user;


import com.onerty.yeogi.term.dto.TermResponse;
import com.onerty.yeogi.user.dto.NicknameResponse;
import com.onerty.yeogi.util.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/v1/terms/agree")
    public BaseResponse<TermResponse> getTerms() {
        return new BaseResponse.success<>(userService.getTerms());
    }

    @GetMapping("/v1/nicknames-recommendation")
    public BaseResponse<NicknameResponse> getNicknames() {
        return new BaseResponse.success<>(userService.generateRandomNicknames());
    }
}
