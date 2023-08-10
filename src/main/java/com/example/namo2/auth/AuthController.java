package com.example.namo2.auth;

import com.example.namo2.config.exception.BaseException;
import com.example.namo2.config.response.BaseResponse;
import com.example.namo2.auth.dto.SignUpReq;
import com.example.namo2.auth.dto.SignUpRes;
import com.example.namo2.auth.dto.SocialSignUpReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Api(value = "Auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/kakao/signup")
    @ApiOperation(value = "카카오 로그인 ")
    public BaseResponse<SignUpRes> kakaoSignup(@Valid @RequestBody SocialSignUpReq signUpReq) throws BaseException {
        SignUpRes signupRes = authService.signupKakao(signUpReq);
        return new BaseResponse<>(signupRes);
    }

    @PostMapping(value = "/naver/signup")
    @ApiOperation(value = "네이버 로그인")
    public BaseResponse<SignUpRes> naverSignup(@Valid @RequestBody SocialSignUpReq signUpReq) throws BaseException {
        SignUpRes signupRes = authService.signupNaver(signUpReq);
        return new BaseResponse<>(signupRes);
    }

    @PostMapping(value = "/reissuance")
    @ApiOperation(value = "토큰 재발급")
    public BaseResponse<SignUpRes> reissueAccessToken(@Valid @RequestBody SignUpReq signUpReq) throws BaseException {
        SignUpRes signupRes = authService.reissueAccessToken(signUpReq);
        return new BaseResponse<>(signupRes);
    }

}
