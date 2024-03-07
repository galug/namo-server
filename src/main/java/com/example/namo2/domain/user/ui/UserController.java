package com.example.namo2.domain.user.ui;

import com.example.namo2.domain.user.application.UserFacade;
import com.example.namo2.domain.user.ui.dto.UserRequest;
import com.example.namo2.global.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "유저 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserFacade userFacade;

    @Operation(summary = "약관을 동의합니다. ", description = "약관 동의 API")
    @PostMapping("/term")
    public BaseResponse<Void> createTerm(@Valid @RequestBody UserRequest.TermDto termDto, HttpServletRequest request) {
        userFacade.createTerm(termDto, (Long) request.getAttribute("userId"));
        return BaseResponse.ok();
    }

}
