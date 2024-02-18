package com.example.namo2.domain.user.ui;

import com.example.namo2.domain.user.application.UserFacade;
import com.example.namo2.domain.user.application.impl.AuthService;
import com.example.namo2.domain.user.ui.dto.UserRequest;
import com.example.namo2.domain.user.ui.dto.UserResponse;
import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.global.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Auth", description = "로그인, 회원가입 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserFacade userFacade;


    @Operation(summary = "kakao 회원가입", description = "kakao 소셜 로그인을 통한 회원가입")
    @PostMapping(value = "/kakao/signup")
    public BaseResponse<UserResponse.SignUpDto> kakaoSignup(@Valid @RequestBody UserRequest.SocialSignUpDto signUpDto
    ) throws BaseException {
        UserResponse.SignUpDto signupDto = userFacade.signupKakao(signUpDto);
        return new BaseResponse<>(signupDto);
    }

    @Operation(summary = "naver 회원가입", description = "naver 소셜 로그인을 통한 회원가입")
    @PostMapping(value = "/naver/signup")
    public BaseResponse<UserResponse.SignUpDto> naverSignup(@Valid @RequestBody UserRequest.SocialSignUpDto signUpDto) throws BaseException {
        UserResponse.SignUpDto signupDto = userFacade.signupNaver(signUpDto);
        return new BaseResponse<>(signupDto);
    }

    @Operation(summary = "토큰 재발급", description = "토큰 재발급")
    @PostMapping(value = "/reissuance")
    public BaseResponse<UserResponse.SignUpDto> reissueAccessToken(@Valid @RequestBody UserRequest.SignUpDto signUpDto) throws BaseException {
        UserResponse.SignUpDto signupDto = authService.reissueAccessToken(signUpDto);
        return new BaseResponse<>(signupDto);
    }

    @Operation(summary = "로그아웃", description = "로그아웃")
    @PostMapping(value = "/logout")
    public BaseResponse logout(@Valid @RequestBody UserRequest.LogoutDto logoutDto) throws BaseException {
        authService.logout(logoutDto);
        return BaseResponse.ok();
    }
}
