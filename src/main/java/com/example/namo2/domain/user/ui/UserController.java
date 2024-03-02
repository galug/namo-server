package com.example.namo2.domain.user.ui;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User", description = "유저 관련 API")
@RestController
@RequestMapping("/user")
public class UserController {
}
