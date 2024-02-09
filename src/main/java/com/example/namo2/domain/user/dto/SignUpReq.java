package com.example.namo2.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
public class SignUpReq {
    @NotBlank
    private String accessToken;
    @NotBlank
    private String refreshToken;
}
