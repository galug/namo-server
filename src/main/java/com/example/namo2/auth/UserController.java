package com.example.namo2.auth;

import com.example.namo2.auth.dto.LogoutReq;
import com.example.namo2.config.exception.BaseException;
import com.example.namo2.config.response.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@Api(value = "User")
public class UserController {
}
