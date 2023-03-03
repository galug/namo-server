package com.example.namo2.user;

import com.example.namo2.config.BaseException;
import com.example.namo2.config.BaseResponse;
import com.example.namo2.user.dto.SignUpReq;
import com.example.namo2.user.dto.SignUpRes;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Api(value = "User")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/kakao/signup")
    public BaseResponse<SignUpRes> kakaoSignup(@RequestBody SignUpReq signUpReq) throws BaseException {
        try{
            SignUpRes signupRes = userService.signupKakao(signUpReq);
            return new BaseResponse<>(signupRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @PostMapping(value = "/naver/signup")
    public BaseResponse<SignUpRes> naverSignup(@RequestBody SignUpReq signUpReq) throws BaseException {
        try{
            SignUpRes signupRes = userService.signupNaver(signUpReq);
            return new BaseResponse<>(signupRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @PostMapping(value = "/reissuance")
    public BaseResponse<SignUpRes> reissueAccessToken(@RequestBody SignUpReq signUpReq) throws BaseException {
        try{
            SignUpRes signupRes = userService.reissueAccessToken(signUpReq);
            return new BaseResponse<>(signupRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
