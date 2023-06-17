package com.example.namo2.user.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class SocialSignUpReq {
    @NotBlank
    String accessToken;
}
