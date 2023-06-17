package com.example.namo2.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@AllArgsConstructor
public class SignUpReq {
    @NotBlank
    private String accessToken;
    @NotBlank
    private String refreshToken;
}