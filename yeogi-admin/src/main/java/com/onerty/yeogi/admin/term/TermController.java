package com.onerty.yeogi.admin.term;

import com.onerty.yeogi.admin.term.dto.RegisterTermRequest;
import com.onerty.yeogi.admin.term.dto.TermResponse;
import com.onerty.yeogi.admin.term.dto.UpdateTermRequest;
import com.onerty.yeogi.common.util.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/api/terms")
@RequiredArgsConstructor
public class TermController {

    private final TermService termService;

    @PostMapping("")
    public BaseResponse<TermResponse> registerTerm(@RequestBody RegisterTermRequest createTermRequest) {
        createTermRequest.check();
        TermResponse response = termService.registerTerm(createTermRequest);
        return new BaseResponse.success<>(response);
    }

    @PatchMapping("")
    public BaseResponse<TermResponse> updateTerm(@RequestBody UpdateTermRequest updateTermRequestTermRequest) {
        updateTermRequestTermRequest.check();
        TermResponse response = termService.updateTermContent(updateTermRequestTermRequest);
        return new BaseResponse.success<>(response);
    }

}
