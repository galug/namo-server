package com.example.namo2.domain.auth;

import com.example.namo2.domain.auth.dto.LogoutReq;
import com.example.namo2.domain.auth.dto.SignUpReq;
import com.example.namo2.domain.auth.dto.SignUpRes;
import com.example.namo2.domain.auth.dto.SocialSignUpReq;
import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.global.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Auth", description = "로그인, 회원가입 관련 API")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "kakao 회원가입", description = "kakao 소셜 로그인을 통한 회원가입")
    @PostMapping(value = "/kakao/signup")
    public BaseResponse<SignUpRes> kakaoSignup(@Valid @RequestBody SocialSignUpReq signUpReq) throws BaseException {
        SignUpRes signupRes = authService.signupKakao(signUpReq);
        return new BaseResponse<>(signupRes);
    }

    @Operation(summary = "naver 회원가입", description = "naver 소셜 로그인을 통한 회원가입")
    @PostMapping(value = "/naver/signup")
    public BaseResponse<SignUpRes> naverSignup(@Valid @RequestBody SocialSignUpReq signUpReq) throws BaseException {
        SignUpRes signupRes = authService.signupNaver(signUpReq);
        return new BaseResponse<>(signupRes);
    }

    @Operation(summary = "토큰 재발급", description = "토큰 재발급")
    @PostMapping(value = "/reissuance")
    public BaseResponse<SignUpRes> reissueAccessToken(@Valid @RequestBody SignUpReq signUpReq) throws BaseException {
        SignUpRes signupRes = authService.reissueAccessToken(signUpReq);
        return new BaseResponse<>(signupRes);
    }

    @Operation(summary = "로그아웃", description = "로그아웃")
    @PostMapping(value = "/logout")
    public BaseResponse logout(@Valid @RequestBody LogoutReq logoutReq) throws BaseException {
        authService.logout(logoutReq);
        return BaseResponse.ok();
    }
}
