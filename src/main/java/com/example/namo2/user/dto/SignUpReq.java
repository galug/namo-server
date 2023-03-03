package com.example.namo2.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class SignUpReq {
    private String accessToken;
    private String refreshToken;
}