package com.example.namo2.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpRes {
    private String accessToken;
    private String refreshToken;
}
